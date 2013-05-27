package net.jay.plugins.php.cache;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.startup.StartupManagerEx;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.psi.PsiManager;

/**
 * @author jay
 * @date May 21, 2008 12:01:58 AM
 */
public class PhpModuleCacheManager implements ModuleComponent
{
	@NonNls
	private static final String PHP_CACHE_DIR = "php_caches";

	protected Module module;
	protected PhpModuleFilesCache moduleFilesCache;
	protected ModuleRootManager rootManager;

	//PsiManager should build caches thus register prestarup activity thus create it's instance
	//  before the cache manager
	@SuppressWarnings({
			"UnusedDeclaration",
			"UnusedParameters"
	})
	public PhpModuleCacheManager(@NotNull final Module module, @NotNull final ModuleRootManager manager, @NotNull final PsiManager psiManger)
	{
		this.module = module;
		rootManager = manager;
	}

	/**
	 * @return Path to file where cached information is saved.
	 */
	@NotNull
	protected String generateCacheFilePath()
	{
		return PathManager.getSystemPath() + "/" + PHP_CACHE_DIR + "/" +
				module.getProject().getName() + "/" + module.getName() + "_" + module.getModuleFilePath().hashCode();
	}

	public void projectOpened()
	{
	}

	public void projectClosed()
	{
		moduleFilesCache.saveCacheToDisk();
	}

	/**
	 * Unique name of this component. If there is another component with the same name or
	 * name is null internal assertion will occur.
	 *
	 * @return the name of this component
	 */
	@NonNls
	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.ModuleCacheManager";
	}

	public void initComponent()
	{
		final Project project = module.getProject();
		final Runnable initAction = new Runnable()
		{
			public void run()
			{
				moduleFilesCache.initFileCacheAndRegisterListeners();
			}
		};
		final Runnable setupAction = new Runnable()
		{
			public void run()
			{
				registerDeclarationsIndexAndInitFilesCache(module, moduleFilesCache);
			}
		};

		final boolean projectIsOpened = project.isOpen();

		moduleFilesCache = new PhpModuleFilesCache(module, rootManager);
		moduleFilesCache.setCacheFilePath(generateCacheFilePath());
		moduleFilesCache.setCacheRootURLs(ModuleRootManager.getInstance(module).getContentRootUrls());

		if(projectIsOpened)
		{
			//FacetDetector works in pre or startup activities.
			//so here we cant register preStartupActivity
			initAction.run();
		}
		else
		{
			StartupManagerEx.getInstanceEx(project).registerPreStartupActivity(initAction);
		}
		StartupManager.getInstance(project).runWhenProjectIsInitialized(setupAction);
	}

	public void disposeComponent()
	{
	}

	@NotNull
	public PhpModuleFilesCache getFilesCache()
	{
		return moduleFilesCache;
	}

	@NotNull
	public DeclarationsIndex getDeclarationsIndex()
	{
		return moduleFilesCache.getDeclarationsIndex();
	}

	public void moduleAdded()
	{
		//do nothing
	}

	/**
	 * Invoke it only after PhpModuleCacheManager.initComponent()
	 *
	 * @param provider Provides files for caches. By default all module's php files are included.
	 */
	public void registerScanForFilesProvider(final CacheScannerFilesProvider provider)
	{
		moduleFilesCache.registerScanForFilesProvider(provider);
	}

	public void unregisterScanForFilesProvider(final CacheScannerFilesProvider provider)
	{
		moduleFilesCache.unregisterScanForFilesProvider(provider);
	}

	public static void registerDeclarationsIndexAndInitFilesCache(@NotNull final Module module, @NotNull final PhpModuleFilesCache moduleFilesCache)
	{
		final Project project = module.getProject();

		final DeclarationsIndex index = new DeclarationsIndex(project);
		// Associating wordsIndex with moduleFilesCache
		moduleFilesCache.registerDeclarationsIndex(index);

		//This code runs Action immediatly if project is initialized, otherwise registers post startup activity
		//if we add new module to existing project, action will run immediatly
		StartupManager.getInstance(project).runWhenProjectIsInitialized(new Runnable()
		{
			public void run()
			{
				moduleFilesCache.setupFileCache(true);
			}
		});
	}
}
