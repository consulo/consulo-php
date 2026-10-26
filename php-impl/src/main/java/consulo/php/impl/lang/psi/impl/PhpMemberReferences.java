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

import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiElement;
import org.jspecify.annotations.Nullable;

/**
 * Qualifier and access kind of {@code $a->b}, {@code $a?->b}, {@code A::b}
 *
 * @author VISTALL
 * @since 2026-10-26
 */
final class PhpMemberReferences
{
	private static final TokenSet ACCESS = TokenSet.create(PhpTokenTypes.ARROW, PhpTokenTypes.NULLSAFE_ARROW, PhpTokenTypes.SCOPE_RESOLUTION);

	private PhpMemberReferences()
	{
	}

	/**
	 * @return the element before the access token
	 */
	@Nullable
	static PhpExpression qualifier(PsiElement reference)
	{
		for(PsiElement child = reference.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(ACCESS.contains(child.getNode().getElementType()))
			{
				return null;
			}
			if(child instanceof PhpPsiElement)
			{
				return child instanceof PhpExpression expression ? expression : null;
			}
		}
		return null;
	}

	/**
	 * @return the class reference of a static access ({@code A::b}), null for {@code $a->b}
	 */
	@Nullable
	static ClassReference staticClassReference(PsiElement reference)
	{
		return qualifier(reference) instanceof ClassReference classReference && reference.getNode().findChildByType(PhpTokenTypes.SCOPE_RESOLUTION) != null
			? classReference : null;
	}

	static PhpModifier.State referenceType(PsiElement reference)
	{
		if(reference.getNode().findChildByType(PhpTokenTypes.SCOPE_RESOLUTION) == null)
		{
			return PhpModifier.State.DYNAMIC;
		}
		ClassReference classReference = staticClassReference(reference);
		return classReference != null && "parent".equalsIgnoreCase(classReference.getText()) ? PhpModifier.State.PARENT : PhpModifier.State.STATIC;
	}
}
