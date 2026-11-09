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

import consulo.annotation.component.ActionImpl;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionRef;
import consulo.application.ReadAction;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.repository.ui.PackagesEditorManager;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.ex.action.IdeActions;

@ActionImpl(id = "Composer.ManagePackages", parents = @ActionParentRef(@ActionRef(id = IdeActions.GROUP_PROJECT_VIEW_POPUP)))
public class ManageComposerPackagesAction extends DumbAwareAction implements AnActionWithSyncUpdate {
    public ManageComposerPackagesAction() {
        super(LocalizeValue.localizeTODO("Manage Composer Packages"), LocalizeValue.empty(), ComposerIconGroup.composer());
    }

    @RequiredUIAccess
    @Override
    public void actionPerformed(AnActionEvent e) {
        Module module = e.getData(Module.KEY);
        if (module == null) {
            return;
        }
        PackagesEditorManager.getInstance(module.getProject()).open(ComposerPackageManagementServiceProvider.class, module.getName());
    }

    @Override
    public void update(AnActionEvent e) {
        Module module = e.getData(Module.KEY);
        e.getPresentation().setEnabledAndVisible(module != null
            && ReadAction.compute(() -> ComposerPackageManagementServiceProvider.findComposerJson(module)) != null);
    }
}
