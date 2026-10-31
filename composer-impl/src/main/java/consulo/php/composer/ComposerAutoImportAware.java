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

package consulo.php.composer;

import consulo.externalSystem.service.project.autoimport.ExternalSystemAutoImportAware;
import consulo.php.composer.setting.ComposerProjectSettings;
import consulo.project.Project;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

/**
 * A package is out of date when its {@code composer.json} or {@code composer.lock} changes - the platform tracks
 * these files and offers to load the changes.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerAutoImportAware implements ExternalSystemAutoImportAware {
    @Override
    public @Nullable String getAffectedExternalProjectPath(String changedFileOrDirPath, Project project) {
        Path changed = Path.of(changedFileOrDirPath);
        Path fileName = changed.getFileName();
        if (fileName == null) {
            return null;
        }
        String name = fileName.toString();
        if (!ComposerConstants.COMPOSER_JSON.equals(name) && !ComposerConstants.COMPOSER_LOCK.equals(name)) {
            return null;
        }

        Path directory = changed.getParent();
        if (directory == null) {
            return null;
        }
        ComposerProjectSettings settings = ComposerUtil.findProjectSettings(project, directory.toString());
        return settings == null ? null : settings.getExternalProjectPath();
    }

    @Override
    public List<Path> getAffectedExternalProjectFilePaths(String projectPath, Project project) {
        Path directory = ComposerUtil.toPackageDirectory(Path.of(projectPath));
        return List.of(directory.resolve(ComposerConstants.COMPOSER_JSON), directory.resolve(ComposerConstants.COMPOSER_LOCK));
    }
}
