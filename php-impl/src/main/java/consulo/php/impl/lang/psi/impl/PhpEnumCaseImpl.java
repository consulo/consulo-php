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

package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpEnumCase;
import consulo.application.util.function.Processor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.php.lang.lexer.PhpTokenTypes;
import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpEnumCaseImpl extends PhpNamedElementImpl implements PhpEnumCase {
    public PhpEnumCaseImpl(ASTNode node) {
        super(node);
    }

    @Override
    public void accept(PhpElementVisitor visitor) {
        visitor.visitPhpElement(this);
    }

    @Override
    public @Nullable PhpClass getContainingClass() {
        return PsiTreeUtil.getParentOfType(this, PhpClass.class);
    }

    @Override
    public @Nullable PsiElement getValue() {
        PsiElement assign = findChildByType(PhpTokenTypes.opASGN);
        if (assign == null) {
            return null;
        }
        PsiElement value = assign.getNextSibling();
        while (value != null && value.getNode().getElementType() == PhpTokenTypes.WHITE_SPACE) {
            value = value.getNextSibling();
        }
        return value;
    }

    @Override
    public String getNamespaceName() {
        PhpClass phpClass = getContainingClass();
        return phpClass == null ? "" : phpClass.getNamespaceName();
    }

    @Override
    public @Nullable ASTNode getNameNode() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : identifier.getNode();
    }

    @Override
    public void processDocs(Processor<PhpDocComment> processor) {
        PhpDocComment docComment = getDocComment();
        if (docComment != null) {
            processor.process(docComment);
        }
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getFQN() {
        PhpClass phpClass = getContainingClass();
        return phpClass == null ? getName() : phpClass.getFQN() + "." + getName();
    }
}
