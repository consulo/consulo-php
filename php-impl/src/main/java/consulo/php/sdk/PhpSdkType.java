package consulo.php.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.php.PhpBundle;
import consulo.php.PhpIcons;
import consulo.roots.types.BinariesOrderRootType;
import consulo.roots.types.SourcesOrderRootType;
import consulo.ui.image.Image;

/**
 * @author Maxim
 */
public class PhpSdkType extends SdkType
{
	private static final Logger LOGGER = Logger.getInstance(PhpSdkType.class);

	public static PhpSdkType getInstance()
	{
		return EP_NAME.findExtension(PhpSdkType.class);
	}

	public static String getExecutableFile(String home)
	{
		return home + File.separator + (SystemInfo.isWindows ? "php.exe" : "php");
	}

	public PhpSdkType()
	{
		super("PHP SDK");
	}

	@Nonnull
	@Override
	public Collection<String> suggestHomePaths()
	{
		if(SystemInfo.isMacOSLeopard || SystemInfo.isMacOSTiger)
		{
			return Collections.singletonList("/usr/bin");
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return new File(getExecutableFile(path)).exists();
	}

	public static String getVersion(String home)
	{
		List<String> args = new ArrayList<>(2);
		args.add(getExecutableFile(home));
		args.add("--version");
		try
		{
			ProcessOutput processOutput = new CapturingProcessHandler(new GeneralCommandLine(args).withWorkDirectory(home)).runProcess();
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
	@Nonnull
	public String getPresentableName()
	{
		return PhpBundle.message("php.sdk.type.name");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		final SdkModificator sdkModificator = sdk.getSdkModificator();

		File pluginPath = PluginManager.getPluginPath(PhpSdkType.class);

		File stubDirectory = new File(pluginPath, "php-runtime-stubs");
		VirtualFile stubsDir = LocalFileSystem.getInstance().findFileByIoFile(stubDirectory);

		if(stubsDir == null)
		{
			LOGGER.warn("Cant find stubs for path: " + stubDirectory);
		}
		else
		{
			sdkModificator.addRoot(stubsDir, BinariesOrderRootType.getInstance());
			sdkModificator.addRoot(stubsDir, SourcesOrderRootType.getInstance());
		}

		sdkModificator.commitChanges();
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == BinariesOrderRootType.getInstance() || type == SourcesOrderRootType.getInstance();
	}

	@Override
	public Image getIcon()
	{
		return PhpIcons.Php;
	}
}
