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
package consulo.php.composer.packages;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ReadAction;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.project.Project;
import consulo.repository.ui.PackageManagementService;
import consulo.repository.ui.PackageManagementServiceProvider;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

/**
 * The Composer requirements of a module - the target is the name of the module, the requirements are the ones of the composer.json
 * of its content root.
 */
@ExtensionImpl
public class ComposerPackageManagementServiceProvider implements PackageManagementServiceProvider {
    @Override
    public String getId() {
        return "composer";
    }

    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Composer");
    }

    @Override
    public Image getIcon() {
        return ComposerIconGroup.composer();
    }

    @Override
    public @Nullable PackageManagementService create(Project project, String target) {
        return ReadAction.compute(() -> {
            Module module = ModuleManager.getInstance(project).findModuleByName(target);
            if (module == null) {
                return null;
            }
            VirtualFile composerJson = findComposerJson(module);
            return composerJson == null ? null : new ComposerPackageManagementService(module, composerJson);
        });
    }

    @RequiredReadAction
    static @Nullable VirtualFile findComposerJson(Module module) {
        for (VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots()) {
            VirtualFile composerJson = contentRoot.findChild(ComposerConstants.COMPOSER_JSON);
            if (composerJson != null) {
                return composerJson;
            }
        }
        return null;
    }
}
