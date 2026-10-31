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

package consulo.php.composer.importProvider;

import consulo.disposer.Disposable;
import consulo.externalSystem.service.setting.AbstractExternalProjectSettingsConfigurable;
import consulo.externalSystem.service.setting.ExternalSystemSettingsPlace;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.php.composer.setting.ComposerProjectSettings;
import consulo.ui.Component;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerProjectSettingsConfigurable extends AbstractExternalProjectSettingsConfigurable<ComposerProjectSettings> {
    private @Nullable TextBox myExecutableBox;

    public ComposerProjectSettingsConfigurable(ComposerProjectSettings settings, ExternalSystemSettingsPlace place) {
        super(settings, place);
    }

    @Override
    @RequiredUIAccess
    protected Component createExtraUIComponent(Disposable uiDisposable) {
        String executable = getSettings().getComposerExecutable();
        TextBox executableBox = TextBox.create(executable == null ? "" : executable);
        executableBox.setPlaceholder(ComposerLocalize.settingsExecutablePlaceholder(ComposerConstants.DEFAULT_EXECUTABLE));
        myExecutableBox = executableBox;

        return FormBuilder.create()
            .addLabeled(ComposerLocalize.settingsExecutableLabel(), executableBox)
            .build();
    }

    @Override
    @RequiredUIAccess
    protected boolean isExtraModified() {
        return !Objects.equals(getExecutable(), getSettings().getComposerExecutable());
    }

    @Override
    @RequiredUIAccess
    protected void applyExtra() {
        if (myExecutableBox == null) {
            return;
        }
        getSettings().setComposerExecutable(getExecutable());
    }

    @RequiredUIAccess
    private @Nullable String getExecutable() {
        String value = myExecutableBox == null ? null : myExecutableBox.getValue();
        return value == null || value.isBlank() ? null : value.trim();
    }
}
