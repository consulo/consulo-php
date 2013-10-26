package org.consulo.php.sdk;

import javax.swing.Icon;

import org.consulo.lombok.annotations.Logger;
import org.consulo.php.PhpBundle;
import org.consulo.php.PhpIcons2;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Maxim
 */
@Logger
public class PhpSdkType extends SdkType
{
	public PhpSdkType()
	{
		super("PHP SDK");
	}

	@Override
	@Nullable
	public String suggestHomePath()
	{
		if(SystemInfo.isMacOSLeopard || SystemInfo.isMacOSTiger)
		{
			return "/usr/bin";
		}
		return null;
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		final VirtualFile file = VfsUtil.findRelativeFile(path, null);
		return isValidPhpSdkHomeDirectory(file);
	}

	public static boolean isValidPhpSdkHomeDirectory(VirtualFile file)
	{
		boolean correctHome = false;

		for(VirtualFile child : file.getChildren())
		{
			if(child.getNameWithoutExtension().equals("php"))
			{
				if((SystemInfo.isWindows && "exe".equals(child.getExtension())) || (!SystemInfo.isWindows && child.getExtension() == null))
				{
					correctHome = true;
					break;
				}
			}
		}
		return correctHome;
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return "5.3";
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return PhpBundle.message("default.php.sdk.name");
	}

	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}

	@Nullable
	public static String getExecutablePath(Sdk sdk)
	{
		return sdk.getHomePath() + "php" + (SystemInfo.isWindows ? ".exe" : "");
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData additionalData, Element additional)
	{
	}

	@Override
	@NotNull
	public String getPresentableName()
	{
		return PhpBundle.message("php.sdk.type.name");
	}


	public static PhpSdkType getInstance()
	{
		return findInstance(PhpSdkType.class);
	}

	@Override
	public boolean setupSdkPaths(Sdk sdk, SdkModel sdkModel)
	{
		final SdkModificator sdkModificator = sdk.getSdkModificator();

		String path = PathManager.getPreInstalledPluginsPath() + "/php/stubs";
		VirtualFile stubsDir = LocalFileSystem.getInstance().findFileByPath(path);
		if(stubsDir == null)
		{
			path = PathManager.getPluginsPath() + "/php/stubs";
			stubsDir = LocalFileSystem.getInstance().findFileByPath(path);
		}

		if(stubsDir == null)
		{
			LOGGER.warn("Cant find stubs for path: " + path);
		}
		else
		{
			sdkModificator.addRoot(stubsDir, OrderRootType.CLASSES);
			sdkModificator.addRoot(stubsDir, OrderRootType.SOURCES);
		}

		sdkModificator.commitChanges();
		return true;
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == OrderRootType.CLASSES || type == OrderRootType.SOURCES;
	}

	@Override
	public Icon getIcon()
	{
		return PhpIcons2.Php;
	}

	@Nullable
	@Override
	public Icon getGroupIcon()
	{
		return PhpIcons2.Php;
	}
}
