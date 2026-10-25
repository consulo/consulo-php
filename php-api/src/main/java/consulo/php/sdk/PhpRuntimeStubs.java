/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.php.sdk;

import consulo.util.io.URLUtil;
import consulo.virtualFileSystem.StandardFileSystems;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jspecify.annotations.Nullable;

import java.net.URL;

/**
 * Declarations of PHP itself and its extensions (JetBrains phpstorm-stubs), bundled in this plugin.
 * {@link PhpSdkType} adds them as the roots of PHP SDKs. Refreshed by {@code php-api/sync-runtime-stubs.sh}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpRuntimeStubs {
    public static final String RESOURCE_ROOT = "/phpstorm-stubs";

    private PhpRuntimeStubs() {
    }

    /**
     * @return stubs directory inside the plugin jar, {@code null} if it can not be found
     */
    public static @Nullable VirtualFile getRoot() {
        URL url = PhpRuntimeStubs.class.getResource(RESOURCE_ROOT);
        if (url == null) {
            return null;
        }
        VirtualFile root = VirtualFileManager.getInstance().findFileByUrl(toVirtualFileUrl(url));
        return root != null && root.isValid() ? root : null;
    }

    /**
     * Whether the root URL is runtime stubs of some plugin location, also of the old stubs directory next to the plugin jars
     */
    public static boolean isRuntimeStubsUrl(String url) {
        return url.endsWith("!" + RESOURCE_ROOT) || url.endsWith("/php-runtime-stubs");
    }

    /**
     * The {@code jar://} file system comes with the Java plugin, the {@code zip://} one is always there: open the plugin jar with it
     */
    static String toVirtualFileUrl(URL url) {
        String vfsUrl = VirtualFileUtil.convertFromUrl(url);
        String jarPrefix = StandardFileSystems.JAR_PROTOCOL + URLUtil.SCHEME_SEPARATOR;
        return vfsUrl.startsWith(jarPrefix) ? StandardFileSystems.ZIP_PROTOCOL_PREFIX + vfsUrl.substring(jarPrefix.length()) : vfsUrl;
    }
}
