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

package consulo.php.composer.importer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * The data of a {@code composer.json}
 *
 * @param name               package name, like {@code symfony/skeleton}
 * @param require            {@code require}: package name to version constraint
 * @param requireDev         {@code require-dev}
 * @param psr4Autoload       {@code autoload.psr-4}: namespace prefix to directories relative to the package root
 * @param psr4AutoloadDev    {@code autoload-dev.psr-4}
 * @author VISTALL
 * @since 2026-10-31
 */
public record ComposerPackage(@Nullable String name,
                              @Nonnull Map<String, String> require,
                              @Nonnull Map<String, String> requireDev,
                              @Nonnull Map<String, List<String>> psr4Autoload,
                              @Nonnull Map<String, List<String>> psr4AutoloadDev) {
    /**
     * @return whether the package is required, in {@code require} or {@code require-dev}
     */
    public boolean requires(@Nonnull String packageName) {
        return require.containsKey(packageName) || requireDev.containsKey(packageName);
    }

    /**
     * @return whether a package with the name prefix is required, like {@code symfony/}
     */
    public boolean requiresAnyWithPrefix(@Nonnull String prefix) {
        return require.keySet().stream().anyMatch(name -> name.startsWith(prefix))
            || requireDev.keySet().stream().anyMatch(name -> name.startsWith(prefix));
    }
}
