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

package consulo.php.composer.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.externalSystem.service.module.extension.ExternalSystemModuleExtensionImpl;
import consulo.externalSystem.service.module.extension.ExternalSystemMutableModuleExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.ui.image.Image;
import org.jspecify.annotations.Nullable;

/**
 * Marks modules imported from a {@code composer.json} - its id must be the id of the
 * {@link ComposerConstants#SYSTEM_ID system}.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerModuleExtensionProvider implements ModuleExtensionProvider<ExternalSystemModuleExtensionImpl> {
    @Override
    public String getId() {
        return ComposerConstants.SYSTEM_ID.getId();
    }

    @Override
    public @Nullable String getParentId() {
        return null;
    }

    @Override
    public boolean isSystemOnly() {
        return true;
    }

    @Override
    public LocalizeValue getName() {
        return ComposerLocalize.systemName();
    }

    @Override
    public Image getIcon() {
        return ComposerIconGroup.composer();
    }

    @Override
    public ModuleExtension<ExternalSystemModuleExtensionImpl> createImmutableExtension(ModuleRootLayer moduleRootLayer) {
        return new ExternalSystemModuleExtensionImpl(getId(), moduleRootLayer, ComposerConstants.SYSTEM_ID);
    }

    @Override
    public MutableModuleExtension<ExternalSystemModuleExtensionImpl> createMutableExtension(ModuleRootLayer moduleRootLayer) {
        return new ExternalSystemMutableModuleExtensionImpl(getId(), moduleRootLayer, ComposerConstants.SYSTEM_ID);
    }
}
