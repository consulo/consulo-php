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

package consulo.php.composer.jom;

import consulo.annotation.component.ExtensionImpl;
import consulo.json.jom.JomFileDescriptor;
import consulo.language.psi.PsiFile;
import consulo.php.composer.ComposerFileTypeFactory;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerJsonFileDescriptor extends JomFileDescriptor<ComposerJsonRootElement> {
    public ComposerJsonFileDescriptor() {
        super(ComposerJsonRootElement.class);
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return ComposerIconGroup.composer();
    }

    @Override
    public boolean isMyFile(@Nonnull PsiFile psiFile) {
        return ComposerFileTypeFactory.COMPOSER_JSON.equals(psiFile.getName());
    }

    /**
     * The model only reads what the import needs, the file is validated by {@link ComposerJsonSchemaProviderFactory}
     */
    @Override
    public boolean isDescribingFile() {
        return false;
    }
}
