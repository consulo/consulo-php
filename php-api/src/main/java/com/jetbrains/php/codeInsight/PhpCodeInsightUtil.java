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

package com.jetbrains.php.codeInsight;

import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpGroupUseElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.PhpUseList;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Imports ({@code use} statements) of a namespace, in the shape of PhpStorm's {@code PhpCodeInsightUtil}.
 * <p>
 * The scope for {@code use} is the statement group of the enclosing namespace, or of the file without namespaces.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpCodeInsightUtil
{
	private PhpCodeInsightUtil()
	{
	}

	/**
	 * @return statement group which holds the {@code use} statements visible at the element
	 */
	@Nullable
	@RequiredReadAction
	public static PhpPsiElement findScopeForUseOperator(@Nonnull PsiElement element)
	{
		PhpNamespace namespace = PsiTreeUtil.getParentOfType(element, PhpNamespace.class, false);
		if(namespace != null)
		{
			return namespace.getStatements();
		}
		PsiFile file = element.getContainingFile();
		return file instanceof PhpFile ? PsiTreeUtil.getChildOfType(file, GroupStatement.class) : null;
	}

	@Nonnull
	@RequiredReadAction
	public static List<PhpUseList> collectImports(@Nonnull PhpPsiElement scope)
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(scope, PhpUseList.class);
	}

	/**
	 * @return class imports: alias (or short name) to the imported FQN with the leading backslash
	 */
	@Nonnull
	@RequiredReadAction
	public static Map<String, String> getAliasesInScope(@Nonnull PhpPsiElement scope)
	{
		Map<String, String> result = new LinkedHashMap<>();
		for(PhpUseList useList : collectImports(scope))
		{
			for(PhpUse use : useList.getDeclarations())
			{
				String fqn = use.getFQN();
				String name = use.getAliasName() != null ? use.getAliasName() : use.getName();
				if(use.isClassImport() && fqn != null && name != null)
				{
					result.put(name, fqn);
				}
			}
		}
		return result;
	}

	/**
	 * @return alias (or short name) under which the FQN is imported, null when not imported
	 */
	@Nullable
	@RequiredReadAction
	public static String findImportedName(@Nonnull PhpPsiElement scope, @Nonnull String fqn, @Nonnull PhpGroupUseElement.PhpUseKeyword keyword)
	{
		String expected = PhpLangUtil.toFQN(fqn);
		for(PhpUseList useList : collectImports(scope))
		{
			for(PhpUse use : useList.getDeclarations())
			{
				if(use.getUseKeyword() != keyword || !sameName(keyword, expected, use.getFQN()))
				{
					continue;
				}
				return use.getAliasName() != null ? use.getAliasName() : use.getName();
			}
		}
		return null;
	}

	/**
	 * Whether a {@code use} for the FQN can be added: its name is not taken by another import
	 */
	@RequiredReadAction
	public static boolean canImport(@Nonnull PhpPsiElement scope, @Nonnull String fqn, @Nonnull PhpGroupUseElement.PhpUseKeyword keyword)
	{
		String name = PhpLangUtil.toName(fqn);
		for(PhpUseList useList : collectImports(scope))
		{
			for(PhpUse use : useList.getDeclarations())
			{
				String importedName = use.getAliasName() != null ? use.getAliasName() : use.getName();
				if(use.getUseKeyword() == keyword && importedName != null && importedName.equalsIgnoreCase(name)
					&& !sameName(keyword, PhpLangUtil.toFQN(fqn), use.getFQN()))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Shortest name of the FQN usable in the scope: an import alias, a name relative to the current namespace, or the FQN itself.
	 *
	 * @param context           element the name is for, may be null
	 * @param isFunctionOrConst functions and constants of the global namespace are found without a qualifier
	 */
	@Nonnull
	@RequiredReadAction
	public static String createQualifiedName(@Nonnull PhpPsiElement scope, @Nonnull String fqn, @Nullable PsiElement context, boolean isFunctionOrConst)
	{
		String target = PhpLangUtil.toFQN(fqn);

		// imported: 'use A\B as C' makes \A\B\D available as C\D
		for(Map.Entry<String, String> entry : getAliasesInScope(scope).entrySet())
		{
			String imported = entry.getValue();
			if(PhpLangUtil.equalsClassNames(imported, target))
			{
				return entry.getKey();
			}
			if(startsWithIgnoreCase(target, imported + "\\"))
			{
				return entry.getKey() + target.substring(imported.length());
			}
		}

		String namespace = getNamespaceFQN(scope);
		String prefix = PhpLangUtil.isGlobalNamespaceFQN(namespace) ? namespace : namespace + "\\";
		if(startsWithIgnoreCase(target, prefix))
		{
			return target.substring(prefix.length());
		}
		if(isFunctionOrConst && target.lastIndexOf('\\') == 0)
		{
			return target.substring(1);
		}
		return target;
	}

	/**
	 * @return FQN of the namespace of the scope, {@code \} for the global namespace
	 */
	@Nonnull
	@RequiredReadAction
	public static String getNamespaceFQN(@Nonnull PsiElement scope)
	{
		PhpNamespace namespace = PsiTreeUtil.getParentOfType(scope, PhpNamespace.class, false);
		String name = namespace == null ? null : namespace.getName();
		return name == null || name.isEmpty() ? PhpLangUtil.GLOBAL_NAMESPACE_NAME : PhpLangUtil.toFQN(name);
	}

	private static boolean sameName(PhpGroupUseElement.PhpUseKeyword keyword, String expected, @Nullable String actual)
	{
		if(actual == null)
		{
			return false;
		}
		// constants are case-sensitive, classes and functions are not
		return keyword == PhpGroupUseElement.PhpUseKeyword.CONST ? expected.equals(actual) : expected.equalsIgnoreCase(actual);
	}

	private static boolean startsWithIgnoreCase(String text, String prefix)
	{
		return text.regionMatches(true, 0, prefix, 0, prefix.length());
	}
}
