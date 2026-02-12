package consulo.php.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.container.plugin.PluginManager;
import consulo.content.OrderRootType;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkType;
import consulo.logging.Logger;
import consulo.php.icon.PhpIconGroup;
import consulo.php.localize.PhpLocalize;
import consulo.platform.Platform;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim
 */
@ExtensionImpl
public class PhpSdkType extends SdkType {
    private static final Logger LOGGER = Logger.getInstance(PhpSdkType.class);

    @Nonnull
    public static PhpSdkType getInstance() {
        return Application.get().getExtensionPoint(SdkType.class).findExtensionOrFail(PhpSdkType.class);
    }

    @Nonnull
    public static String getExecutableFile(String home) {
        return home + File.separator + (Platform.current().os().isWindows() ? "php.exe" : "php");
    }

    public PhpSdkType() {
        super("PHP SDK", PhpLocalize.phpSdkTypeName(), PhpIconGroup.filetypesPhp());
    }

    @Nonnull
    @Override
    public Collection<String> suggestHomePaths() {
        if (Platform.current().os().isMac()) {
            return Collections.singletonList("/usr/bin");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isValidSdkHome(String path) {
        return new File(getExecutableFile(path)).exists();
    }

    public static String getVersion(String home) {
        List<String> args = new ArrayList<>(2);
        args.add(getExecutableFile(home));
        args.add("--version");
        try {
            ProcessOutput processOutput = CapturingProcessUtil.execAndGetOutput(new GeneralCommandLine(args).withWorkDirectory(home));
            List<String> stdoutLines = processOutput.getStdoutLines();
            return !stdoutLines.isEmpty() ? stdoutLines.get(0) : null;
        }
        catch (ExecutionException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public String getVersionString(String s) {
        return getVersion(s);
    }

    @Override
    public void setupSdkPaths(Sdk sdk) {
        final SdkModificator sdkModificator = sdk.getSdkModificator();

        File pluginPath = PluginManager.getPluginPath(PhpSdkType.class);

        File stubDirectory = new File(pluginPath, "php-runtime-stubs");
        VirtualFile stubsDir = LocalFileSystem.getInstance().findFileByIoFile(stubDirectory);

        if (stubsDir == null) {
            LOGGER.warn("Cant find stubs for path: " + stubDirectory);
        }
        else {
            sdkModificator.addRoot(stubsDir, BinariesOrderRootType.getInstance());
            sdkModificator.addRoot(stubsDir, SourcesOrderRootType.getInstance());
        }

        sdkModificator.commitChanges();
    }

    @Override
    public boolean isRootTypeApplicable(OrderRootType type) {
        return type == BinariesOrderRootType.getInstance() || type == SourcesOrderRootType.getInstance();
    }
}
