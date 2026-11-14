package consulo.php.impl.xdebug;

import consulo.platform.Platform;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class PhpDebugUtil {
    private PhpDebugUtil() {
    }

    /**
     * The uri of the file as Xdebug knows it - the path of the machine of the file ({@code file:///C:/a.php} on Windows).
     */
    @Nonnull
    public static String toFileUri(@Nonnull VirtualFile file) {
        return toFileUri(file.toNioPath().toString());
    }

    @Nonnull
    public static String toFileUri(@Nonnull String path) {
        String independent = path.replace('\\', '/');
        return independent.startsWith("/") ? "file://" + independent : "file:///" + independent;
    }

    /**
     * The file of an uri of Xdebug - a path of {@code platform}, the machine of the script.
     */
    @Nullable
    public static VirtualFile toVirtualFile(@Nonnull String fileUri, @Nonnull Platform platform) {
        try {
            String path;
            if (fileUri.startsWith("file://")) {
                URI uri = new URI(fileUri);
                path = uri.getPath();
                // file:///C:/a.php - the path of a drive
                if (path.length() > 2 && path.charAt(0) == '/' && path.charAt(2) == ':') {
                    path = path.substring(1);
                }
            }
            else {
                path = fileUri;
            }
            return VirtualFileManager.getInstance().findFileByNioPath(platform.fs().getPath(path));
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String decodeBase64(@Nullable String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return null;
        }
        try {
            return new String(Base64.getDecoder().decode(encoded.trim()), StandardCharsets.UTF_8);
        }
        catch (IllegalArgumentException e) {
            return encoded;
        }
    }

    @Nonnull
    public static String makeBreakpointKey(@Nonnull String fileUri, int line) {
        return fileUri + ":" + line;
    }
}
