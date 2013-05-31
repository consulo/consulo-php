package net.jay.plugins.php.lang.projectConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;

import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.ProjectSdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Maxim
 */
public class PhpSdkType extends SdkType implements ApplicationComponent
{
	public PhpSdkType()
	{
		super("PHP SDK Type");
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
	public String getBinPath(Sdk sdk)
	{
		return sdk.getHomePath();
	}

	@Nullable
	public String getToolsPath(Sdk sdk)
	{
		return sdk.getHomePath();
	}

	@Nullable
	public String getVMExecutablePath(Sdk sdk)
	{
		return sdk.getHomePath() + "php" + (SystemInfo.isWindows ? ".exe" : "");
	}

	@Nullable
	public String getRtLibraryPath(Sdk sdk)
	{
		return null;
	}

	public void saveAdditionalData(SdkAdditionalData additionalData, Element additional)
	{
	}

	public String getPresentableName()
	{
		return PHPBundle.message("php.sdk.type.name");
	}

	@NonNls
	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.PhpSdkType";
	}

	public void initComponent()
	{
	}

	public void disposeComponent()
	{
	}

	public static PhpSdkType getInstance()
	{
		return ApplicationManager.getApplication().getComponent(PhpSdkType.class);
	}

	public Sdk[] getSdks()
	{
		final List<Sdk> sdks = new ArrayList<Sdk>();
		for(Sdk sdk : ProjectSdkTable.getInstance().getAllSdks())
		{
			if(sdk.getSdkType() == this)
				sdks.add(sdk);
		}
		return sdks.toArray(new Sdk[sdks.size()]);
	}

	public Sdk createOrGetPhpSdk(final String path)
	{
		return ApplicationManager.getApplication().runWriteAction(new Computable<Sdk>()
		{
			public Sdk compute()
			{
				for(Sdk sdk : getSdks())
				{
					if(Comparing.equal(sdk.getHomePath(), path))
						return sdk;
				}

				Sdk sdk = new ProjectJdkImpl(PHPBundle.message("default.php.sdk.name"), PhpSdkType.this);
				final SdkModificator sdkModificator = sdk.getSdkModificator();
				sdkModificator.setVersionString(PHPBundle.message("default.php.sdk.version"));
				sdkModificator.setHomePath(path);
				sdkModificator.commitChanges();
				ProjectSdkTable.getInstance().addSdk(sdk);
				return sdk;
			}
		});
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
