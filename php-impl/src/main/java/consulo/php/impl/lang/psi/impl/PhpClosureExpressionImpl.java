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

import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;

/**
 * Like PhpStorm's {@code CLOSURE} expression: the expression of a closure or an arrow function, its child is the
 * {@link com.jetbrains.php.lang.psi.elements.Function}.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class PhpClosureExpressionImpl extends PhpElementImpl implements PhpExpression
{
	public PhpClosureExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpType.CLOSURE;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
