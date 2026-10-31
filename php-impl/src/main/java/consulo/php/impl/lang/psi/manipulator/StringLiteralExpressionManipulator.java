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

package consulo.php.impl.lang.psi.manipulator;

import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.elements.impl.StringLiteralExpressionImpl;
import consulo.annotation.access.RequiredWriteAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.psi.AbstractElementManipulator;
import consulo.language.util.IncorrectOperationException;
import jakarta.annotation.Nonnull;

/**
 * Like PhpStorm's: the contents of a string between its quotes, so references contributed to strings - like the route
 * names of the Symfony plugin - have their range and can be renamed.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class StringLiteralExpressionManipulator extends AbstractElementManipulator<StringLiteralExpressionImpl> {
    @Nonnull
    @Override
    public TextRange getRangeInElement(@Nonnull StringLiteralExpressionImpl element) {
        return element.getValueRange();
    }

    @RequiredWriteAction
    @Override
    public StringLiteralExpressionImpl handleContentChange(@Nonnull StringLiteralExpressionImpl element, @Nonnull TextRange range, String newContent)
        throws IncorrectOperationException {
        String oldText = element.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        StringLiteralExpression literal = PhpPsiElementFactory.createPhpPsiFromText(element.getProject(), StringLiteralExpression.class,
            newText + ";");
        return (StringLiteralExpressionImpl) element.replace(literal);
    }

    @Nonnull
    @Override
    public Class<StringLiteralExpressionImpl> getElementClass() {
        return StringLiteralExpressionImpl.class;
    }
}
