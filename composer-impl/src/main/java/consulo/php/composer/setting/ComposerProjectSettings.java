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

package consulo.php.composer.setting;

import consulo.externalSystem.setting.ExternalProjectSettings;
import org.jspecify.annotations.Nullable;

/**
 * Settings of one linked package - the directory holding {@code composer.json}.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerProjectSettings extends ExternalProjectSettings {
    private @Nullable String myComposerExecutable;

    /**
     * @return path of the {@code composer} executable or of a {@code composer.phar}; {@code null} for {@code composer}
     * from {@code PATH}
     */
    public @Nullable String getComposerExecutable() {
        return myComposerExecutable;
    }

    public void setComposerExecutable(@Nullable String composerExecutable) {
        myComposerExecutable = composerExecutable == null || composerExecutable.isBlank() ? null : composerExecutable.trim();
    }

    @Override
    public ComposerProjectSettings clone() {
        ComposerProjectSettings clone = new ComposerProjectSettings();
        copyTo(clone);
        clone.myComposerExecutable = myComposerExecutable;
        return clone;
    }
}
