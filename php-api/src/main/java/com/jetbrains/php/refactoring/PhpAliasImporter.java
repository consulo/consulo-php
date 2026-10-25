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

package com.jetbrains.php.refactoring;

import com.jetbrains.php.codeInsight.PhpCodeInsightUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpUseList;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiParserFacade;
import consulo.language.psi.PsiWhiteSpace;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Adds {@code use} statements, in the shape of PhpStorm's {@code PhpAliasImporter}.
 *
 * @author VISTALL
 * @since 2026-10-25
 * @see PhpCodeInsightUtil#findScopeForUseOperator(PsiElement)
 */
public final class PhpAliasImporter
{
	private PhpAliasImporter()
	{
	}

	@RequiredWriteAction
	public static void insertUseStatement(@Nonnull String fqn, @Nonnull PhpPsiElement scope)
	{
		insertUseStatement(fqn, null, scope);
	}

	/**
	 * Adds {@code use fqn as alias;} after the last import of the scope, or at its start
	 *
	 * @param alias the alias, null or the short name of the FQN for a plain import
	 */
	@RequiredWriteAction
	public static void insertUseStatement(@Nonnull String fqn, @Nullable String alias, @Nonnull PhpPsiElement scope)
	{
		Project project = scope.getProject();
		String name = PhpLangUtil.toPresentableFQN(fqn);
		boolean aliased = alias != null && !alias.isEmpty() && !alias.equals(PhpLangUtil.toName(fqn));
		PhpUseList useList = PhpPsiElementFactory.createPhpPsiFromText(project, PhpUseList.class,
			"use " + name + (aliased ? " as " + alias : "") + ";");

		PsiElement newLine = PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n");
		List<PhpUseList> imports = PhpCodeInsightUtil.collectImports(scope);
		if(!imports.isEmpty())
		{
			PsiElement last = imports.get(imports.size() - 1);
			PsiElement added = scope.addAfter(useList, last);
			scope.addBefore(newLine, added);
			return;
		}

		// '<?php' of a file without namespace belongs to its statement group
		PsiElement anchor = scope.getFirstChild();
		if(PhpPsiUtil.isOfType(anchor, PhpTokenTypes.tsPHP_OPENING_TAGS))
		{
			PsiElement added = scope.addAfter(useList, anchor);
			scope.addBefore(PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n"), added);
			return;
		}

		while(anchor instanceof PsiWhiteSpace)
		{
			anchor = anchor.getNextSibling();
		}
		if(anchor == null)
		{
			scope.add(useList);
			return;
		}
		PsiElement added = scope.addBefore(useList, anchor);
		scope.addAfter(PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n"), added);
	}
}
