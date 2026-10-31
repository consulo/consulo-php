// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.jetbrains.php.lang.psi.elements;

import com.jetbrains.php.lang.parser.PhpElementTypes;
import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents both Declaration and Call parameter list.
 */
public interface ParameterList extends PhpPsiElement
{
	Condition<PsiElement> INSTANCEOF = use -> use instanceof ParameterList;

	@Nonnull
	PsiElement[] getParameters();

	@Nullable
	default PsiElement getParameter(int index)
	{
		PsiElement[] parameters = getParameters();
		return index >= 0 && index < parameters.length ? parameters[index] : null;
	}

	/**
	 * The value of the named argument {@code name: value}, or else the positional argument at the index
	 */
	@Nullable
	default PsiElement getParameter(@Nonnull String name, int index)
	{
		int position = 0;
		PsiElement positional = null;
		boolean named = false;
		for(PsiElement child = getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(!(child instanceof PhpPsiElement))
			{
				continue;
			}
			String argumentName = PhpPsiUtil.getArgumentName(child);
			if(argumentName != null)
			{
				named = true;
				if(name.equals(argumentName))
				{
					return child;
				}
			}
			else if(!named && child.getNode().getElementType() != PhpElementTypes.CALLABLE_PLACEHOLDER)
			{
				// positional arguments are before the named ones
				if(position++ == index)
				{
					positional = child.getNode().getElementType() == PhpElementTypes.VARIADIC_ARGUMENT ? null : child;
				}
			}
		}
		return positional;
	}
}
