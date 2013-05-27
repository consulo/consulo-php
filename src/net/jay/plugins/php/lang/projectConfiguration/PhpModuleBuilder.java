package net.jay.plugins.php.lang.projectConfiguration;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Maxim
 */
public class PhpModuleBuilder extends ModuleBuilder implements SourcePathsBuilder
{
	private Sdk mySdk;
	private String myContentRootPath;
	private List<Pair<String, String>> mySourcePathes;

	public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException
	{
		modifiableRootModel.setSdk(mySdk);

		final String moduleRootPath = getContentEntryPath();

		if(moduleRootPath != null)
		{
			final VirtualFile moduleContentRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(moduleRootPath.replace('\\', '/'));

			if(moduleContentRoot != null)
			{
				final ContentEntry contentEntry = modifiableRootModel.addContentEntry(moduleContentRoot);
				final List<Pair<String, String>> list = getSourcePaths();

				if(list != null)
				{
					for(Pair<String, String> p : list)
					{
						final VirtualFile relativeFile = VfsUtil.findRelativeFile(p.first, null);
						if(relativeFile != null)
							contentEntry.addSourceFolder(relativeFile, false);
					}
				}
			}
		}
	}

	public ModuleType getModuleType()
	{
		return PhpModuleType.getInstance();
	}

	@Nullable
	public String getContentEntryPath()
	{
		return myContentRootPath;
	}

	public void setContentEntryPath(String moduleRootPath)
	{
		myContentRootPath = moduleRootPath;
	}

	public List<Pair<String, String>> getSourcePaths()
	{
		return mySourcePathes;
	}

	public void setSourcePaths(List<Pair<String, String>> sourcePaths)
	{
		mySourcePathes = sourcePaths;
	}

	public void addSourcePath(Pair<String, String> sourcePathInfo)
	{
		if(mySourcePathes == null)
			mySourcePathes = new ArrayList<Pair<String, String>>();
		mySourcePathes.add(sourcePathInfo);
	}

	public void setSdk(Sdk sdk)
	{
		mySdk = sdk;
	}

	public void setSdkPath(String path)
	{
		mySdk = PhpSdkType.getInstance().createOrGetPhpSdk(path);
	}
}
