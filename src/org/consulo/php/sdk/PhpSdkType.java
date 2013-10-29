package org.consulo.php.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.consulo.lombok.annotations.Logger;
import org.consulo.php.PhpBundle;
import org.consulo.php.PhpIcons2;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
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
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Maxim
 */
@Logger
public class PhpSdkType extends SdkType
{
	public static String getExecutableFile(String home)
	{
		return home + File.separator + (SystemInfo.isWindows ? "php.exe" : "php");
	}

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
		return new File(getExecutableFile(path)).exists();
	}

	public static String getVersion(String home)
	{
		List<String> args = new ArrayList<String>(2);
		args.add(getExecutableFile(home));
		args.add("--version");
		try
		{
			ProcessOutput processOutput = ExecUtil.execAndGetOutput(args, home);
			List<String> stdoutLines = processOutput.getStdoutLines();
			return !stdoutLines.isEmpty() ? stdoutLines.get(0) : null;
		}
		catch(ExecutionException e)
		{
			return null;
		}
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return getVersion(s);
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
