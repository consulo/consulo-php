package consulo.php.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
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
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.File;
import java.util.*;

/**
 * @author Maxim
 */
@ExtensionImpl
public class PhpSdkType extends SdkType {
    private static final Logger LOGGER = Logger.getInstance(PhpSdkType.class);

    private static final Set<String> ourAllowedRootTypeIds = Set.of(BinariesOrderRootType.ID, SourcesOrderRootType.ID);
    
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
        SdkModificator sdkModificator = sdk.getSdkModificator();

        // the declarations of PHP itself: classes, functions and constants of the runtime and its extensions
        VirtualFile stubs = PhpRuntimeStubs.getRoot();
        if (stubs == null) {
            LOGGER.warn("Cant find bundled PHP runtime stubs: " + PhpRuntimeStubs.class.getResource(PhpRuntimeStubs.RESOURCE_ROOT));
        }
        else {
            sdkModificator.addRoot(stubs, BinariesOrderRootType.ID);
            sdkModificator.addRoot(stubs, SourcesOrderRootType.ID);
        }

        sdkModificator.commitChanges();
    }

    /**
     * Whether the SDK misses the runtime stubs of this plugin, or has the stubs of another plugin location:
     * the stubs are inside the plugin jar, which moves with plugin updates
     */
    public static boolean isRuntimeStubsOutdated(Sdk sdk) {
        VirtualFile stubs = PhpRuntimeStubs.getRoot();
        if (stubs == null) {
            return false;
        }
        for (String rootType : ourAllowedRootTypeIds) {
            String[] urls = sdk.getRootProvider().getUrls(rootType);
            if (!Arrays.asList(urls).contains(stubs.getUrl())) {
                return true;
            }
            for (String url : urls) {
                if (PhpRuntimeStubs.isRuntimeStubsUrl(url) && !url.equals(stubs.getUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Replaces the runtime stub roots of the SDK by the stubs of this plugin, other roots stay. Needs the write action.
     */
    public static void updateRuntimeStubs(Sdk sdk) {
        VirtualFile stubs = PhpRuntimeStubs.getRoot();
        if (stubs == null) {
            return;
        }
        SdkModificator sdkModificator = sdk.getSdkModificator();
        for (String rootType : ourAllowedRootTypeIds) {
            String[] urls = sdk.getRootProvider().getUrls(rootType);
            sdkModificator.removeRoots(rootType);
            for (String url : urls) {
                if (!PhpRuntimeStubs.isRuntimeStubsUrl(url)) {
                    sdkModificator.addRoot(url, rootType);
                }
            }
            sdkModificator.addRoot(stubs, rootType);
        }
        sdkModificator.commitChanges();
    }

    @Override
    public boolean isRootTypeApplicable(String type) {
        return ourAllowedRootTypeIds.contains(type);
    }
}
