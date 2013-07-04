package net.jay.plugins.php.lang.projectConfiguration;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;
import org.jdom.Element;
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

	@Nullable
	public String suggestHomePath()
	{
		if(SystemInfo.isMacOSLeopard || SystemInfo.isMacOSTiger)
		{
			return "/usr/bin";
		}
		return null;
	}

	public boolean isValidSdkHome(String path)
	{
		final VirtualFile file = VfsUtil.findRelativeFile(path, null);
	//	return file != null && PhpModuleType.isValidPhpSdkHomeDirectory(file);
		return true;
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return null;
	}

	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return PHPBundle.message("default.php.sdk.name");
	}

	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}

	@Nullable
	public static String getVMExecutablePath(Sdk sdk)
	{
		return sdk.getHomePath() + "php" + (SystemInfo.isWindows ? ".exe" : "");
	}

	public void saveAdditionalData(SdkAdditionalData additionalData, Element additional)
	{
	}

	public String getPresentableName()
	{
		return PHPBundle.message("php.sdk.type.name");
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
		sdkModificator.setVersionString(PHPBundle.message("default.php.sdk.version"));
		sdkModificator.commitChanges();
		return b;
	}

	@Override
	public Icon getIcon()
	{
		return PHPIcons.PHP_ICON;
	}
}
