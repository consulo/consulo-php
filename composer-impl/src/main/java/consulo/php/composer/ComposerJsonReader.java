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

package consulo.php.composer;

import consulo.annotation.access.RequiredReadAction;
import consulo.json.jom.JomFileElement;
import consulo.json.jom.JomManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.php.composer.importer.ComposerPackage;
import consulo.php.composer.jom.ComposerAutoloadElement;
import consulo.php.composer.jom.ComposerJsonRootElement;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Reads {@code composer.json} through its JOM model into a {@link ComposerPackage}
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public final class ComposerJsonReader {
    private ComposerJsonReader() {
    }

    @Nullable
    @RequiredReadAction
    public static ComposerPackage read(@Nonnull Project project, @Nonnull VirtualFile composerJson) {
        PsiFile file = PsiManager.getInstance(project).findFile(composerJson);
        if (file == null) {
            return null;
        }
        JomFileElement<ComposerJsonRootElement> element = JomManager.getInstance(project).getFileElement(file);
        if (element == null) {
            return null;
        }

        ComposerJsonRootElement root = element.getRootElement();
        return new ComposerPackage(root.getName(), root.getRequire(), root.getRequireDev(), psr4(root.getAutoload()), psr4(root.getAutoloadDev()));
    }

    @Nonnull
    private static Map<String, List<String>> psr4(@Nullable ComposerAutoloadElement autoload) {
        return autoload == null ? Map.of() : autoload.getPsr4();
    }
}
