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

package consulo.php.impl.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.WriteAction;
import consulo.application.dumb.DumbAware;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.php.sdk.PhpSdkType;
import consulo.project.Project;
import consulo.project.startup.BackgroundStartupActivity;
import consulo.ui.UIAccess;

import java.util.List;

/**
 * Points PHP SDKs at the runtime stubs of the installed plugin: the stubs are inside the plugin jar, whose path changes with
 * plugin updates, and older SDKs pointed at a stubs directory next to it.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtensionImpl
public class PhpRuntimeStubsSdkUpdater implements BackgroundStartupActivity, DumbAware {
    @Override
    public void runActivity(Project project, UIAccess uiAccess) {
        List<Sdk> outdated = SdkTable.getInstance().getSdksOfType(PhpSdkType.getInstance()).stream()
            .filter(PhpSdkType::isRuntimeStubsOutdated)
            .toList();
        if (outdated.isEmpty()) {
            return;
        }
        uiAccess.give(() -> WriteAction.run(() -> {
            for (Sdk sdk : outdated) {
                PhpSdkType.updateRuntimeStubs(sdk);
            }
        }));
    }
}
