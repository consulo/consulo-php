package consulo.php.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.OrderRootType;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.PlatformAwareSdkType;
import consulo.content.bundle.SdkType;
import consulo.logging.Logger;
import consulo.php.icon.PhpIconGroup;
import consulo.php.localize.PhpLocalize;
import consulo.platform.Platform;
import consulo.util.lang.StringUtil;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * @author Maxim
 */
@ExtensionImpl
public class PhpSdkType extends PlatformAwareSdkType {
    private static final Logger LOGGER = Logger.getInstance(PhpSdkType.class);

    private static final Pattern VERSION_PATTERN = Pattern.compile("^PHP\\s+(\\S+)");

    private static final Set<String> ourAllowedRootTypeIds = Set.of(BinariesOrderRootType.ID, SourcesOrderRootType.ID);
    
    @Nonnull
    public static PhpSdkType getInstance() {
        return Application.get().getExtensionPoint(SdkType.class).findExtensionOrFail(PhpSdkType.class);
    }

    public PhpSdkType() {
        super("PHP SDK", PhpLocalize.phpSdkTypeName(), PhpIconGroup.filetypesPhp());
    }

    /**
     * The PHP executable of the SDK home: {@code /usr/bin/php}
     */
    @Nonnull
    public static Path getExecutable(@Nonnull Platform platform, @Nonnull Path home) {
        return home.resolve(platform.os().isWindows() ? "php.exe" : "php");
    }

    /**
     * The PHP executable of the SDK - of the machine of the SDK
     */
    @Nonnull
    public static Path getExecutable(@Nonnull Sdk sdk) {
        return getExecutable(sdk.getPlatform(), sdk.getHomeNioPath());
    }

    @Override
    public void collectHomePaths(Platform platform, Consumer<Path> pathConsumer) {
        Set<Path> homes = new LinkedHashSet<>();
        if (!platform.os().isWindows()) {
            for (String directory : new String[]{"/usr/bin", "/usr/local/bin", "/opt/homebrew/bin"}) {
                homes.add(platform.fs().getPath(directory));
            }
        }
        // the interpreters of the path - also the ones of Windows, which has no standard location
        String path = platform.os().getEnvironmentVariable("PATH");
        if (path != null) {
            for (String entry : StringUtil.split(path, platform.fs().getPathListSeparator())) {
                homes.add(platform.fs().getPath(entry));
            }
        }
        for (Path home : homes) {
            if (Files.isRegularFile(getExecutable(platform, home))) {
                pathConsumer.accept(home);
            }
        }
    }

    /**
     * Every PHP interpreter found on a machine becomes an SDK, so a project always has one to choose
     */
    @Override
    public boolean canCreatePredefinedSdks(Platform platform) {
        return true;
    }

    @Override
    public boolean isValidSdkHome(Platform platform, Path path) {
        return Files.exists(getExecutable(platform, path));
    }


    /**
     * The version of the first line of {@code php --version}: {@code 8.5.11} of {@code PHP 8.5.11 (cli) (built: ...) (NTS)}
     */
    @Nullable
    public static String parseVersion(String line) {
        Matcher matcher = VERSION_PATTERN.matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }

    @Nullable
    @Override
    public String getVersionString(Platform platform, Path path) {
        // asked on the machine of the interpreter
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withPlatform(platform);
        commandLine.withExecutablePath(getExecutable(platform, path));
        commandLine.withWorkingDirectory(path);
        commandLine.addParameter("--version");
        try {
            ProcessOutput processOutput = CapturingProcessUtil.execAndGetOutput(commandLine);
            List<String> stdoutLines = processOutput.getStdoutLines();
            return stdoutLines.isEmpty() ? null : parseVersion(stdoutLines.get(0));
        }
        catch (ExecutionException e) {
            return null;
        }
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
