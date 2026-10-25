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

package com.jetbrains.php.lang.psi;


import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiParserFacade;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Editing of classes, in the shape of PhpStorm's {@code PhpCodeEditUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpCodeEditUtil
{
	private PhpCodeEditUtil()
	{
	}

	/**
	 * @return class containing the caret, or the only class of the file
	 */
	@Nullable
	@RequiredReadAction
	public static PhpClass findClassAtCaret(@Nonnull Editor editor, @Nonnull PsiFile file)
	{
		PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
		PhpClass phpClass = PsiTreeUtil.getParentOfType(element, PhpClass.class, false);
		if(phpClass != null || !(file instanceof PhpFile phpFile))
		{
			return phpClass;
		}
		List<PhpClass> classes = PhpPsiUtil.findAllClasses(phpFile);
		return classes.size() == 1 ? classes.get(0) : null;
	}

	/**
	 * Adds the member (method, field list, constant list) at the end of the class body
	 *
	 * @return the added member
	 */
	@Nonnull
	@RequiredWriteAction
	public static PsiElement insertClassMember(@Nonnull PhpClass phpClass, @Nonnull PsiElement member)
	{
		PsiElement rbrace = PhpPsiUtil.getChildOfType(phpClass, PhpTokenTypes.RBRACE);
		PsiParserFacade parserFacade = PsiParserFacade.getInstance(phpClass.getProject());
		if(rbrace == null)
		{
			return phpClass.add(member);
		}
		PsiElement added = phpClass.addBefore(member, rbrace);
		phpClass.addBefore(parserFacade.createWhiteSpaceFromText("\n\n"), added);
		phpClass.addAfter(parserFacade.createWhiteSpaceFromText("\n"), added);
		return added;
	}
}
