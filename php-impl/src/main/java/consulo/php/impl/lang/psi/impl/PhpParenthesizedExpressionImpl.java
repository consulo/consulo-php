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

import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2026-10-26
 */
public class PhpParenthesizedExpressionImpl extends PhpElementImpl implements ParenthesizedExpression
{
	public PhpParenthesizedExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PhpPsiElement getArgument()
	{
		return PhpPsiChildren.phpChild(this, 0);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
