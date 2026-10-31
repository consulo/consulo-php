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

import consulo.annotation.component.ExtensionImpl;
import consulo.externalSystem.ExternalSystemManager;
import consulo.externalSystem.model.ProjectSystemId;
import consulo.externalSystem.service.project.ExternalSystemProjectResolver;
import consulo.externalSystem.service.project.autoimport.CachingExternalSystemAutoImportAware;
import consulo.externalSystem.service.project.autoimport.ExternalSystemAutoImportAware;
import consulo.externalSystem.task.ExternalSystemTaskManager;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.php.composer.project.ComposerProjectResolver;
import consulo.php.composer.setting.ComposerExecutionSettings;
import consulo.php.composer.setting.ComposerLocalSettings;
import consulo.php.composer.setting.ComposerProjectSettings;
import consulo.php.composer.setting.ComposerSettings;
import consulo.php.composer.setting.ComposerSettingsListener;
import consulo.php.composer.task.ComposerTaskManager;
import consulo.project.Project;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Composer as an external system: {@code composer.json} is imported as a module, changes of it and of
 * {@code composer.lock} are tracked, and Composer commands and scripts are tasks.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerManager implements ExternalSystemAutoImportAware,
    ExternalSystemManager<ComposerProjectSettings, ComposerSettingsListener, ComposerSettings, ComposerLocalSettings,
        ComposerExecutionSettings> {

    private final ComposerAutoImportAware myAutoImportAware = new ComposerAutoImportAware();
    private final ExternalSystemAutoImportAware myAutoImportDelegate = new CachingExternalSystemAutoImportAware(myAutoImportAware);

    @Override
    public ProjectSystemId getSystemId() {
        return ComposerConstants.SYSTEM_ID;
    }

    @Override
    public Function<Project, ComposerSettings> getSettingsProvider() {
        return ComposerSettings::getInstance;
    }

    @Override
    public Function<Project, ComposerLocalSettings> getLocalSettingsProvider() {
        return ComposerLocalSettings::getInstance;
    }

    @Override
    public Function<Pair<Project, String>, ComposerExecutionSettings> getExecutionSettingsProvider() {
        return pair -> ComposerUtil.createExecutionSettings(pair.getFirst(), pair.getSecond());
    }

    @Override
    public Supplier<? extends ExternalSystemProjectResolver<ComposerExecutionSettings>> getProjectResolverFactory() {
        return ComposerProjectResolver::new;
    }

    @Override
    public Supplier<? extends ExternalSystemTaskManager<ComposerExecutionSettings>> getTaskManagerFactory() {
        return ComposerTaskManager::new;
    }

    @Override
    public FileChooserDescriptor getExternalProjectDescriptor() {
        return new FileChooserDescriptor(true, true, false, false, false, false) {
            @Override
            public boolean isFileSelectable(VirtualFile file) {
                return file.isDirectory() || ComposerConstants.COMPOSER_JSON.equals(file.getName());
            }
        };
    }

    @Override
    public @Nullable String getAffectedExternalProjectPath(String changedFileOrDirPath, Project project) {
        return myAutoImportDelegate.getAffectedExternalProjectPath(changedFileOrDirPath, project);
    }

    @Override
    public List<Path> getAffectedExternalProjectFilePaths(String projectPath, Project project) {
        return myAutoImportAware.getAffectedExternalProjectFilePaths(projectPath, project);
    }
}
