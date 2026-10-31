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

package consulo.php.composer.jom;

import consulo.json.jom.JomElement;
import consulo.json.jom.JomPropertyGetter;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

/**
 * {@code autoload} and {@code autoload-dev} of {@code composer.json}
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public interface ComposerAutoloadElement extends JomElement {
    /**
     * Namespace prefix to directories, a single directory is a list of one
     */
    @Nonnull
    @JomPropertyGetter("psr-4")
    Map<String, List<String>> getPsr4();
}
