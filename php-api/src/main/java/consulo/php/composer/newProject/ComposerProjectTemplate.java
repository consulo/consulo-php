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

package consulo.php.composer.newProject;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

import java.util.List;

/**
 * A template of the "Composer" new project wizard: the project is created by
 * {@code composer create-project <package>} and then imported, like {@code symfony/skeleton}.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface ComposerProjectTemplate {
    /**
     * @return unique id of the template
     */
    String getId();

    LocalizeValue getName();

    Image getIcon();

    /**
     * @return the package to create the project from, like {@code symfony/skeleton}; a version constraint may follow
     * after a colon, like {@code symfony/skeleton:^7.0}
     */
    String getPackageName();

    /**
     * @return packages {@code composer require}d into the created project, like the {@code webapp} pack of Symfony on
     * top of {@code symfony/skeleton}
     */
    default List<String> getRequiredPackages() {
        return List.of();
    }

    default LocalizeValue getDescription() {
        return LocalizeValue.empty();
    }
}
