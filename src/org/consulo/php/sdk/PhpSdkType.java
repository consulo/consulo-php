package org.consulo.php.sdk;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.consulo.php.PhpBundle;
import org.consulo.php.PhpIcons2;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Maxim
 */
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
		return null;
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
		final boolean b = super.setupSdkPaths(sdk, sdkModel);
		final SdkModificator sdkModificator = sdk.getSdkModificator();
		sdkModificator.setVersionString(PhpBundle.message("default.php.sdk.version"));
		sdkModificator.commitChanges();
		return b;
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
