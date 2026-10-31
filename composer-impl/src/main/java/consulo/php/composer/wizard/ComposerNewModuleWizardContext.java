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

import consulo.content.bundle.Sdk;
import consulo.module.creation.NewModuleWizardContextBase;
import consulo.php.composer.newProject.ComposerProjectTemplate;
import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerNewModuleWizardContext extends NewModuleWizardContextBase {
    private @Nullable Sdk myPhpSdk;
    private final @Nullable ComposerProjectTemplate myTemplate;
    private @Nullable String myComposerExecutable;

    public ComposerNewModuleWizardContext(boolean isNewProject, @Nullable ComposerProjectTemplate template) {
        super(isNewProject);
        myTemplate = template;
    }

    public @Nullable Sdk getPhpSdk() {
        return myPhpSdk;
    }

    public void setPhpSdk(@Nullable Sdk phpSdk) {
        myPhpSdk = phpSdk;
    }

    /**
     * @return the template to create the project from, {@code null} for an empty project
     */
    public @Nullable ComposerProjectTemplate getTemplate() {
        return myTemplate;
    }

    /**
     * @return {@code null} for {@code composer} from {@code PATH}
     */
    public @Nullable String getComposerExecutable() {
        return myComposerExecutable;
    }

    public void setComposerExecutable(@Nullable String composerExecutable) {
        myComposerExecutable = composerExecutable == null || composerExecutable.isBlank() ? null : composerExecutable.trim();
    }
}
