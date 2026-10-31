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

package consulo.php.composer.wizard;

import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.module.creation.ui.UnifiedProjectOrModuleNameStep;
import consulo.module.ui.BundleBox;
import consulo.module.ui.BundleBoxBuilder;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.php.composer.newProject.ComposerProjectTemplate;
import consulo.php.sdk.PhpSdkType;
import consulo.ui.Label;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import org.jspecify.annotations.Nullable;


/**
 * Name and location, the PHP SDK, the template to create the project from and the Composer executable.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerNewModuleSetupStep extends UnifiedProjectOrModuleNameStep<ComposerNewModuleWizardContext> {
    private final @Nullable ComposerProjectTemplate myTemplate;

    private @Nullable BundleBox myBundleBox;
    private @Nullable TextBox myExecutableBox;

    public ComposerNewModuleSetupStep(ComposerNewModuleWizardContext context) {
        super(context);
        myTemplate = context.getTemplate();
    }

    @RequiredUIAccess
    @Override
    protected void extend(FormBuilder builder, Disposable uiDisposable) {
        super.extend(builder, uiDisposable);

        BundleBoxBuilder bundleBoxBuilder = BundleBoxBuilder.create(uiDisposable);
        bundleBoxBuilder.withSdkTypeFilterByClass(PhpSdkType.class);
        BundleBox bundleBox = bundleBoxBuilder.build();
        bundleBox.getComponent().selectFirst();
        builder.addLabeled(ComposerLocalize.newProjectPhpSdkLabel(), bundleBox.getComponent());
        myBundleBox = bundleBox;

        TextBox executableBox = TextBox.create();
        executableBox.setPlaceholder(ComposerLocalize.settingsExecutablePlaceholder(ComposerConstants.DEFAULT_EXECUTABLE));
        builder.addLabeled(ComposerLocalize.settingsExecutableLabel(), executableBox);
        myExecutableBox = executableBox;

        builder.addLabeled(LocalizeValue.empty(), Label.create(describe(myTemplate)));
    }

    private static LocalizeValue describe(@Nullable ComposerProjectTemplate template) {
        if (template == null) {
            return ComposerLocalize.newProjectTemplateEmptyDescription();
        }
        LocalizeValue description = template.getDescription();
        return description == LocalizeValue.empty()
            ? ComposerLocalize.newProjectTemplateDescription(template.getPackageName())
            : description;
    }

    @Override
    public void onStepLeave(ComposerNewModuleWizardContext context) {
        super.onStepLeave(context);
        BundleBox.BundleBoxItem item = myBundleBox == null ? null : myBundleBox.getComponent().getValue();
        context.setPhpSdk(item == null ? null : item.getBundle());
        context.setComposerExecutable(myExecutableBox == null ? null : myExecutableBox.getValue());
    }
}
