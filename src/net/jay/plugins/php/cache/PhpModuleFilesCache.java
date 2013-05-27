package net.jay.plugins.php.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.util.PhpVirtualFileScanner;
import net.jay.plugins.php.util.VirtualFileUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ProjectTopics;
import com.intellij.ide.startup.CacheUpdater;
import com.intellij.ide.startup.FileContent;
import com.intellij.ide.startup.FileSystemSynchronizer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.ModuleAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import com.intellij.openapi.vfs.ex.VirtualFileManagerEx;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.util.messages.MessageBusConnection;

/**
 * @author jay
 * @date May 21, 2008 12:12:00 AM
 */
public class PhpModuleFilesCache implements Disposable, CacheUpdater
{

	private final Project project;
	private Module module;
	@SuppressWarnings({
			"FieldCanBeLocal",
			"UnusedDeclaration"
	})
	private ModuleRootManager moduleRootManager;
	private PhpPomModelListener pomModelListener;

	private final static Logger LOG = Logger.getInstance(PhpModuleFilesCache.class.getName());

	private DeclarationsIndex declarationsIndex;

	private String cacheFilePath;
	private String[] cacheRootURLs;

	private PhpFileStorage fileStorage;

	private List<PhpFileCacheListener> cacheChangedListeners = new ArrayList<PhpFileCacheListener>();
	protected List<CacheScannerFilesProvider> scanProvidersList = new ArrayList<CacheScannerFilesProvider>();

	private final Object LOCK = new Object();

	private boolean wasClosed;
	private String name;

	public PhpModuleFilesCache(@NotNull final Module module, @NotNull final ModuleRootManager manager)
	{
		this.project = module.getProject();
		this.name = module.getName();
		this.module = module;
		this.moduleRootManager = manager;
		registerScanForFilesProvider(new CacheScannerFilesProvider()
		{
			public void scanAndAdd(final String[] rootUrls, final Collection<VirtualFile> files, final ModuleRootManager moduleRootManager)
			{
				PhpVirtualFileScanner.searchFileCacheFiles(moduleRootManager, files);
			}
		});
	}

	public void registerScanForFilesProvider(final CacheScannerFilesProvider provider)
	{
		scanProvidersList.add(provider);
	}

	public void unregisterScanForFilesProvider(final CacheScannerFilesProvider provider)
	{
		scanProvidersList.remove(provider);
	}

	/**
	 * Adds pom model listener to files cache
	 */
  /*private void registerPomListener() {
	final PomModel pomModel = module.getUserData()
    pomModelListener = new PhpPomModelListener(module, pomModel) {
      protected synchronized void processEvent(final VirtualFile vFile) {
        ProgressManager.getInstance().checkCanceled();
        regenerateFileInfo(vFile);
      }
    };
    pomModel.addModelListener(pomModelListener, module);
  }*/
	private void registerModuleDeleteListener()
	{
		final MessageBusConnection messageBusConnection = module.getMessageBus().connect(this);
		messageBusConnection.subscribe(ProjectTopics.MODULES, new ModuleAdapter()
		{
			public void beforeModuleRemoved(final Project project, final Module removedModule)
			{
				if(module == removedModule)
				{
					onClose();
				}
			}
		});
	}

  /*private void unregisterPomListener() {
    module.getPom().getModel().removeModelListener(pomModelListener);
  }*/

	public void setCacheFilePath(@NotNull final String dataFilePath)
	{
		this.cacheFilePath = dataFilePath;
	}

	public void registerDeclarationsIndex(@NotNull final DeclarationsIndex wordsIndex)
	{
		declarationsIndex = wordsIndex;
		declarationsIndex.setFileCache(this);
	}

	@NotNull
	public DeclarationsIndex getDeclarationsIndex()
	{
		return declarationsIndex;
	}

	public void setCacheRootURLs(@NotNull String[] newRoots)
	{
		cacheRootURLs = newRoots;
	}

	public String[] getCacheRootURLs()
	{
		return cacheRootURLs;
	}

	public void setupFileCache(final boolean runProcessWithProgressSynchronously)
	{
		if(declarationsIndex != null)
		{
			declarationsIndex.build(runProcessWithProgressSynchronously);
		}
	}

	public String getName()
	{
		return name;
	}

	public void initFileCacheAndRegisterListeners()
	{
		final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
		if(indicator != null)
		{
			indicator.setText(PHPBundle.message("progress.indicator.title.cache.loading", getName()));
		}
		fileStorage = loadCacheFromDisk();
		if(fileStorage == null)
		{
			fileStorage = new PhpFileStorage();
		}

		if(indicator != null)
		{
			indicator.setText("");
		}

		registerAsCacheUpdater();
		addVirtualFileListener();
		registerDisposer();
		//registerPomListener();
		registerModuleDeleteListener();
		registerPsiTreeListener();
	}

	private void registerPsiTreeListener()
	{
		PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeAdapter()
		{
			@Override
			public void childAdded(PsiTreeChangeEvent event)
			{
				PsiElement element = event.getChild();
				if(element instanceof PHPFile && element.isPhysical())
				{
					VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
					if(virtualFile != null && isInContent(virtualFile))
					{
						regenerateFileInfo(virtualFile);
						return;
					}
				}
				process(event);
			}

			@Override
			public void childRemoved(PsiTreeChangeEvent event)
			{
				PsiElement element = event.getChild();
				if(element instanceof PHPFile && element.isPhysical())
				{
					VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
					if(virtualFile != null && isInContent(virtualFile))
					{
						processFileDeleted(virtualFile.getUrl());
						return;
					}
				}
				process(event);
			}

			@Override
			public void childReplaced(PsiTreeChangeEvent event)
			{
				process(event);
			}

			@Override
			public void childrenChanged(PsiTreeChangeEvent event)
			{
				process(event);
			}

			private void process(PsiTreeChangeEvent event)
			{
				final PsiElement psiElement = event.getParent();

				if(psiElement != null && psiElement.isValid())
				{
					final PsiFile psiFile = psiElement.getContainingFile();

					if(psiFile instanceof PHPFile && psiFile.isPhysical())
					{
						final VirtualFile file = psiFile.getVirtualFile();
						if(file != null && isInContent(file))
						{
							regenerateFileInfo(file);
						}
					}
				}
			}
		});
	}

	protected void registerDisposer()
	{
		Disposer.register(module, this);
	}

	public void dispose()
	{
		synchronized(LOCK)
		{
			if(wasClosed)
			{
				return;
			}
		}
		try
		{
			wasClosed = true;
			onClose();
		}
		catch(Exception ex)
		{
			//Do nothing.
		}
	}

	protected void onClose()
	{
		synchronized(LOCK)
		{
			wasClosed = true;
			cacheChangedListeners.clear();
		}
		unregisterAsCacheUpdater();
		//    unregisterPomListener();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// File operations
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Returns up2date cached info for file
	 * if forceUpdate is true, then if file info in cache has timestamp older than file, file regenerates automatically
	 *
	 * @param file VirtualFile to get cache for
	 * @return PhpFileInfo info by the file
	 */
	@Nullable
	public PhpFileInfo getUp2DateFileInfo(@NotNull final VirtualFile file)
	{
		if(fileStorage == null)
		{
			LOG.error("FilesStorage cannot be null. Maybe cache wasn't initialized. Cache: " + toString() + ", file: " + file);
		}

		final PhpFileInfo fileInfo = fileStorage.getInfoByUrl(file.getUrl());
		return fileInfo != null ? fileInfo : createFileInfo(file);
	}


	/**
	 * Changes PhpFileInfo for file according corresponding PHPFile and fires event
	 * that cache for file was added or updated.
	 *
	 * @param file VirtualFile
	 * @return new PhpFileInfo for file or null.
	 */
	@SuppressWarnings({"UnusedReturnValue"})
	@Nullable
	protected PhpFileInfo regenerateFileInfo(@NotNull final VirtualFile file)
	{
		final String url = file.getUrl();
		final boolean wasRemoved = removeFileInfo(url);
		final PhpFileInfo fileInfo = createFileInfo(file);
		if(wasRemoved)
		{
			fireFileUpdated(url);
		}
		else
		{
			if(fileInfo != null)
			{
				fireFileAdded(url);
			}
		}
		return fileInfo;
	}

	/**
	 * Removes PhpFileInfo and cached data for file
	 *
	 * @param url path for file
	 * @return true if cache contained file or false otherwise
	 */
	protected boolean removeFileInfo(@NotNull final String url)
	{
		final PhpFileInfo fileInfo = fileStorage.removeInfoByUrl(url);
		if(declarationsIndex != null)
		{
			declarationsIndex.removeFileInfoFromIndex(fileInfo);
		}
		return fileInfo != null;
	}

	/**
	 * Creates PhpFileInfo for file according corresponding PHPFile
	 *
	 * @param file VirtualFile
	 * @return new PhpFileInfo for file or null.
	 */
	@Nullable
	protected PhpFileInfo createFileInfo(@NotNull final VirtualFile file)
	{
		final String url = file.getUrl();
		fileStorage.addUrl(url);
		final PhpFileInfo fileInfo = PhpFileInfoFactory.createFileInfo(project, file);
		if(fileInfo == null)
		{
			return null;
		}
		fileStorage.addInfo(fileInfo);

		if(declarationsIndex != null)
		{
			declarationsIndex.addFileInfoToIndex(fileInfo);
		}
		return fileInfo;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Loading and saving cache to disk
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Tries to load cache data from disk!
	 *
	 * @return PhpFileStorage object - if something loaded, null otherwise
	 */
	@Nullable
	private PhpFileStorage loadCacheFromDisk()
	{
		final File moduleDataFile = new File(cacheFilePath);
		PhpFileStorage storage = null;
		if(!moduleDataFile.exists())
		{
			return null;
		}

		try
		{
			ObjectInputStream ois = null;
			try
			{
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(moduleDataFile)));
				final Object data = ois.readObject();
				if(data instanceof PhpFileStorage)
				{
					storage = (PhpFileStorage) data;
					storage.init(project);
				}
			}
			finally
			{
				if(ois != null)
				{
					ois.close();
				}
			}
		}
		catch(ClassNotFoundException e)
		{
			LOG.debug(e);
		}
		catch(IOException e)
		{
			LOG.debug(e);
		}
		return storage;
	}

	/**
	 * Saves serialized cache data to dataFile
	 */
	public void saveCacheToDisk()
	{
		final File dataFile = new File(cacheFilePath);
		try
		{
			if(!dataFile.exists())
			{
				dataFile.mkdirs();
			}

			if(!dataFile.exists())
			{
				return;
			}

			if(dataFile.isDirectory())
			{
				dataFile.delete();
			}

			dataFile.createNewFile();
			ObjectOutputStream oos = null;
			try
			{
				oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
				oos.writeObject(fileStorage);
			}
			catch(Exception e)
			{
				LOG.error(e);
			}
			finally
			{
				oos.close();
			}
		}
		catch(IOException e)
		{
			LOG.info(e);
		}
	}

	public void removeCacheFile()
	{
		final File dataFile = new File(cacheFilePath);
		try
		{
			if(dataFile.exists())
			{
				final File parentDir = dataFile.getParentFile();
				dataFile.delete();
				parentDir.delete();
			}
		}
		catch(SecurityException e)
		{
			LOG.warn("Cache file [" + dataFile.getPath() + "] wasn't deleted.");
			LOG.warn(e);
		}
	}

	public boolean containsUrl(@NotNull final String url)
	{
		return fileStorage.containsUrl(url);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Internal functions
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@NotNull
	public List<String> getAllRelativeUrlsForDirectory(@Nullable final VirtualFile directory)
	{
		if(directory == null)
		{
			return Collections.emptyList();
		}
		assert directory.isDirectory();
		return PhpVirtualFileScanner.getRelativeUrls(directory);
	}

	@NotNull
	public Set<String> getAllUrls()
	{
		if(fileStorage != null)
		{
			return fileStorage.getAllUrls();
		}
		return Collections.emptySet();
	}

	protected Collection<VirtualFile> scanForFiles(@NotNull final String[] rootUrls)
	{
		VirtualFileManager fManager = VirtualFileManager.getInstance();
		final Set<VirtualFile> filesToAdd = new HashSet<VirtualFile>();
		for(String rootUrl : rootUrls)
		{
			final VirtualFile root = fManager.findFileByUrl(rootUrl);
			if(root != null)
			{
				PhpVirtualFileScanner.addFiles(root, filesToAdd);
			}
		}
		return filesToAdd;
	}

	/**
	 * @param fileOrDir file or directory
	 * @return whether file(or directory) is in cache content directories
	 */
	protected boolean isInContent(@Nullable final VirtualFile fileOrDir)
	{
		if(fileOrDir == null)
		{
			return false;
		}
		final String url = fileOrDir.getUrl();
		for(String rootUrl : cacheRootURLs)
		{
			if(url.startsWith(rootUrl))
			{
				return true;
			}
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CacheUpdaterFunctionality
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public VirtualFile[] queryNeededFiles()
	{
		final Set<String> urls2Remove = new HashSet<String>(fileStorage.getAllUrls());
		final Collection<VirtualFile> foundFiles = scanForFiles(cacheRootURLs);
		final Set<VirtualFile> neededFiles = new HashSet<VirtualFile>();

		for(VirtualFile file : foundFiles)
		{
			urls2Remove.remove(file.getUrl());
			if(fileStorage.getFileStatus(file) != PhpFileStorage.FileStatus.UP_TO_DATE)
			{
				neededFiles.add(file);
			}
			// Add url to storage even if information will be generated later!!!
			// Needed to retrieve include links in non module cache files (sdk for example)
			fileStorage.addUrl(file.getUrl());
		}

		for(String url : urls2Remove)
		{
			processFileDeleted(url);
		}

		return neededFiles.toArray(new VirtualFile[neededFiles.size()]);
	}

	public void processFile(@NotNull final FileContent fileContent)
	{
		regenerateFileInfo(fileContent.getVirtualFile());
	}

	public void updatingDone()
	{
	}

	public void canceled()
	{
	}

	protected void processFileDeleted(@NotNull final String url)
	{
		if(removeFileInfo(url))
		{
			fireFileRemoved(url);
		}
	}

	private void addVirtualFileListener()
	{
		VirtualFileListener listener = new VirtualFileAdapter()
		{

			@SuppressWarnings({"ConstantConditions"})
			public void propertyChanged(final VirtualFilePropertyEvent event)
			{
				if(VirtualFile.PROP_NAME.equals(event.getPropertyName()))
				{
					final VirtualFile file = event.getFile();
					if(processDir(file, event.getParent()))
					{
						return;
					}
					final String oldName = (String) event.getOldValue();
					processFileDeleted(VirtualFileUtil.constructUrl(event.getParent(), oldName));
					processFileAdded(file);
				}
			}

			public void contentsChanged(final VirtualFileEvent event)
			{
				final VirtualFile file = event.getFile();
				if(processDir(file, event.getParent()))
				{
					return;
				}
				processFileAdded(file);
			}

			public void fileCreated(final VirtualFileEvent event)
			{
				final VirtualFile file = event.getFile();
				if(processDir(file, event.getParent()))
				{
					return;
				}
				processFileAdded(file);
			}

			public void fileDeleted(final VirtualFileEvent event)
			{
				final VirtualFile file = event.getFile();
				final VirtualFile parent = event.getParent();
				if(processDir(file, parent))
				{
					return;
				}
				if(parent != null)
				{
					//if parent is removed, then this method will be invoked for parent
					//bug fix for : [RUBY-531] http://www.jetbrains.net/jira/browse/RUBY-531
					processFileDeleted(VirtualFileUtil.constructUrl(parent, file.getName()));
				}
			}

			public void fileMoved(final VirtualFileMoveEvent event)
			{
				final VirtualFile file = event.getFile();
				if(processDir(file, event.getParent()))
				{
					return;
				}
				processFileDeleted(VirtualFileUtil.constructUrl(event.getOldParent(), file.getName()));
				processFileAdded(file);
			}

			private void processFileAdded(final VirtualFile file)
			{
				if(PhpVirtualFileScanner.isPhpFile(file) && isInContent(file))
				{
					regenerateFileInfo(file);
				}
			}

			private boolean processDir(@NotNull final VirtualFile file, @Nullable final VirtualFile parent)
			{
				if(file.isDirectory() && isInContent(parent))
				{
					//After internal file creation and deletion cache updater isn't invoked
					//Thus we should perform update manually
					forceUpdate();
					return true;
				}
				return false;
			}
		};
		VirtualFileManager.getInstance().addVirtualFileListener(listener, this);
	}

	private void registerAsCacheUpdater()
	{
		final FileSystemSynchronizer fileSystemSynchronizer = StartupManager.getInstance(project).getFileSystemSynchronizer();
		if(fileSystemSynchronizer != null)
		{
			fileSystemSynchronizer.registerCacheUpdater(this);
		}
		ProjectRootManagerEx.getInstanceEx(project).registerChangeUpdater(this);
		((VirtualFileManagerEx) VirtualFileManagerEx.getInstance()).registerRefreshUpdater(this);
	}

	private void unregisterAsCacheUpdater()
	{
		ProjectRootManagerEx.getInstanceEx(project).unregisterChangeUpdater(this);
		((VirtualFileManagerEx) VirtualFileManagerEx.getInstance()).unregisterRefreshUpdater(this);
	}

	/**
	 * Used in debug puproses
	 */
	public void forceUpdate()
	{
		final FileSystemSynchronizer synchronizer = new FileSystemSynchronizer();
		synchronizer.registerCacheUpdater(this);
		if(!ApplicationManager.getApplication().isUnitTestMode() && project.isOpen())
		{
			Runnable process = new Runnable()
			{
				public void run()
				{
					synchronizer.execute();
				}
			};
			ProgressManager.getInstance().runProcessWithProgressSynchronously(process, PHPBundle.message("progress.indicator.title.roots.changed"), false, project);
		}
		else
		{
			synchronizer.execute();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Listeners
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void addCacheChangedListener(@NotNull final PhpFileCacheListener listener, @NotNull final Disposable parentDisposable)
	{
		synchronized(LOCK)
		{
			cacheChangedListeners.add(listener);
		}
		Disposer.register(parentDisposable, new Disposable()
		{
			public void dispose()
			{
				removeCacheChangedListener(listener);
			}
		});
	}

	public void removeCacheChangedListener(@NotNull final PhpFileCacheListener listener)
	{
		synchronized(LOCK)
		{
			cacheChangedListeners.remove(listener);
		}
	}

	private void fireFileRemoved(@NotNull final String url)
	{
		synchronized(LOCK)
		{
			// To prevent ConcurrentModification of listeners by event handling
			final ArrayList<PhpFileCacheListener> listeners = new ArrayList<PhpFileCacheListener>(cacheChangedListeners);
			for(PhpFileCacheListener listener : listeners)
			{
				listener.fileRemoved(url);
			}
		}
	}

	private void fireFileAdded(@NotNull final String url)
	{
		synchronized(LOCK)
		{
			// To prevent ConcurrentModification of listeners by event handling
			final ArrayList<PhpFileCacheListener> listeners = new ArrayList<PhpFileCacheListener>(cacheChangedListeners);
			for(PhpFileCacheListener listener : listeners)
			{
				listener.fileAdded(url);
			}
		}
	}

	private void fireFileUpdated(@NotNull final String url)
	{
		synchronized(LOCK)
		{
			// To prevent ConcurrentModification of listeners by event handling
			final ArrayList<PhpFileCacheListener> listeners = new ArrayList<PhpFileCacheListener>(cacheChangedListeners);
			for(PhpFileCacheListener listener : listeners)
			{
				listener.fileUpdated(url);
			}
		}
	}

	public String toString()
	{
		final StringBuilder st = new StringBuilder();
		st.append("Php Files cache[").append(getName()).append("]. Urls=[");
		for(String url : getAllUrls())
		{
			st.append("\"").append(url).append("\"\n");
		}
		st.append("].");
		return st.toString();
	}

}
