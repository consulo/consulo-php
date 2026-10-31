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

package consulo.php.composer.ui;

import consulo.annotation.component.ExtensionImpl;
import consulo.externalSystem.ui.AbstractExternalSystemToolWindowFactory;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import consulo.ui.ex.toolWindow.ToolWindowAnchor;
import consulo.ui.image.Image;

/**
 * The "Composer" tool window: linked packages with their modules and tasks (install, update, dump-autoload and the
 * scripts of {@code composer.json}), refresh, link and unlink.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerToolWindowFactory extends AbstractExternalSystemToolWindowFactory {
    public ComposerToolWindowFactory() {
        super(ComposerConstants.SYSTEM_ID);
    }

    @Override
    public ToolWindowAnchor getAnchor() {
        return ToolWindowAnchor.RIGHT;
    }

    @Override
    public Image getIcon() {
        return ComposerIconGroup.composer();
    }

    /**
     * Also for PHP modules whose package is not linked yet - the tool window is where it gets linked.
     */
    @Override
    public boolean validate(Project project) {
        return super.validate(project) || ModuleExtensionHelper.getInstance(project).hasModuleExtension(PhpModuleExtension.class);
    }
}
