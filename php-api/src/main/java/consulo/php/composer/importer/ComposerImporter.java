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

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.module.Module;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

/**
 * Configures the module of an imported Composer project from its {@code composer.json}, like Maven importers:
 * frameworks enable their module extensions for their packages.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface ComposerImporter {
    /**
     * @return whether the importer handles the package, like when it requires {@code symfony/framework-bundle}
     */
    boolean isApplicable(@Nonnull ComposerPackage composerPackage);

    /**
     * Called before the model is committed; the PHP module extension is already enabled
     *
     * @param packageRoot directory of the {@code composer.json}
     */
    @RequiredReadAction
    void process(@Nonnull Module module,
                 @Nonnull ModifiableRootModel model,
                 @Nonnull ComposerPackage composerPackage,
                 @Nonnull VirtualFile packageRoot);
}
