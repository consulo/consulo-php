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

import consulo.externalSystem.model.setting.ExternalSystemExecutionSettings;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * What the task manager needs to run {@code composer} for a package - resolved on the IDE side, because the PHP
 * interpreter of a {@code composer.phar} is a bundle of the IDE.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerExecutionSettings extends ExternalSystemExecutionSettings {
    private final String myComposerExecutable;
    private final @Nullable String myPhpExecutable;

    public ComposerExecutionSettings(String composerExecutable, @Nullable String phpExecutable) {
        myComposerExecutable = composerExecutable;
        myPhpExecutable = phpExecutable;
    }

    /**
     * @return the {@code composer} executable or {@code composer.phar}; a name only is looked up in {@code PATH}
     */
    public String getComposerExecutable() {
        return myComposerExecutable;
    }

    /**
     * @return the PHP interpreter to run a {@code composer.phar} with
     */
    public @Nullable String getPhpExecutable() {
        return myPhpExecutable;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + myComposerExecutable.hashCode();
        result = 31 * result + Objects.hashCode(myPhpExecutable);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof ComposerExecutionSettings that) || !super.equals(o)) {
            return false;
        }
        return myComposerExecutable.equals(that.myComposerExecutable) && Objects.equals(myPhpExecutable, that.myPhpExecutable);
    }
}
