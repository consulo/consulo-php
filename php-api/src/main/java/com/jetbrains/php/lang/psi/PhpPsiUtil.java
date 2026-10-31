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
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.lang.function.Condition;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * PSI navigation helpers in the shape of PhpStorm's {@code PhpPsiUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpPsiUtil
{
	private PhpPsiUtil()
	{
	}

	/**
	 * Named classes, interfaces, traits and enums declared in the file, also inside namespaces and conditional blocks
	 */
	@Nonnull
	@RequiredReadAction
	public static List<PhpClass> findAllClasses(@Nonnull PhpFile file)
	{
		return findClasses(file, Condition.TRUE);
	}

	@Nonnull
	@RequiredReadAction
	public static List<PhpClass> findClasses(@Nonnull PhpFile file, @Nonnull Condition<? super PhpClass> condition)
	{
		List<PhpClass> result = new ArrayList<>();
		for(PhpClass phpClass : PsiTreeUtil.findChildrenOfType(file, PhpClass.class))
		{
			if(!phpClass.isAnonymous() && condition.value(phpClass))
			{
				result.add(phpClass);
			}
		}
		return result;
	}

	public static boolean isOfType(@Nullable PsiElement element, @Nonnull IElementType type)
	{
		if(element == null)
		{
			return false;
		}
		ASTNode node = element.getNode();
		return node != null && node.getElementType() == type;
	}

	public static boolean isOfType(@Nullable PsiElement element, @Nonnull TokenSet types)
	{
		if(element == null)
		{
			return false;
		}
		ASTNode node = element.getNode();
		return node != null && types.contains(node.getElementType());
	}

	public static boolean isOfType(@Nullable PsiElement element, @Nonnull IElementType... types)
	{
		return isOfType(element, TokenSet.create(types));
	}

	/**
	 * First child (including leaves) matching the condition
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends PsiElement> T getChildByCondition(@Nullable PsiElement element, @Nonnull Condition<? super PsiElement> condition)
	{
		if(element == null)
		{
			return null;
		}
		for(PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(condition.value(child))
			{
				return (T) child;
			}
		}
		return null;
	}

	@Nullable
	public static <T extends PsiElement> T getChildOfClass(@Nullable PsiElement element, @Nonnull Class<T> aClass)
	{
		return getChildByCondition(element, aClass::isInstance);
	}

	@Nullable
	public static <T extends PsiElement> T getChildOfType(@Nullable PsiElement element, @Nonnull IElementType type)
	{
		return getChildByCondition(element, child -> isOfType(child, type));
	}

	@Nullable
	public static <T extends PsiElement> T getChildOfType(@Nullable PsiElement element, @Nonnull TokenSet types)
	{
		return getChildByCondition(element, child -> isOfType(child, types));
	}

	/**
	 * Direct children (including leaves) matching the condition
	 */
	@Nonnull
	@SuppressWarnings("unchecked")
	public static <T extends PsiElement> List<T> getChildren(@Nullable PsiElement element, @Nonnull Condition<? super PsiElement> condition)
	{
		if(element == null)
		{
			return List.of();
		}
		List<T> result = new ArrayList<>();
		for(PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(condition.value(child))
			{
				result.add((T) child);
			}
		}
		return result;
	}

	/**
	 * First following sibling that does NOT match {@code skip}
	 */
	@Nullable
	public static PsiElement getNextSibling(@Nullable PsiElement element, @Nonnull Condition<? super PsiElement> skip)
	{
		if(element == null)
		{
			return null;
		}
		PsiElement sibling = element.getNextSibling();
		while(sibling != null && skip.value(sibling))
		{
			sibling = sibling.getNextSibling();
		}
		return sibling;
	}

	/**
	 * First preceding sibling that does NOT match {@code skip}
	 */
	@Nullable
	public static PsiElement getPrevSibling(@Nullable PsiElement element, @Nonnull Condition<? super PsiElement> skip)
	{
		if(element == null)
		{
			return null;
		}
		PsiElement sibling = element.getPrevSibling();
		while(sibling != null && skip.value(sibling))
		{
			sibling = sibling.getPrevSibling();
		}
		return sibling;
	}

	/**
	 * The name of a named argument {@code name: value} - the name and the colon are the previous siblings of the value
	 * in the parameter list
	 */
	@Nullable
	public static String getArgumentName(@Nonnull PsiElement argument)
	{
		PsiElement colon = getPrevSiblingIgnoreWhitespace(argument, true);
		if(colon == null || colon.getNode().getElementType() != PhpTokenTypes.opCOLON)
		{
			return null;
		}
		PsiElement name = getPrevSiblingIgnoreWhitespace(colon, true);
		return name == null ? null : name.getText();
	}

	@Nullable
	public static PsiElement getNextSiblingIgnoreWhitespace(@Nullable PsiElement element, boolean ignoreComments)
	{
		return getNextSibling(element, sibling -> isWhitespaceOrComment(sibling, ignoreComments));
	}

	@Nullable
	public static PsiElement getPrevSiblingIgnoreWhitespace(@Nullable PsiElement element, boolean ignoreComments)
	{
		return getPrevSibling(element, sibling -> isWhitespaceOrComment(sibling, ignoreComments));
	}

	/**
	 * @param strict    start from the parent instead of the element itself
	 * @param condition parent to find
	 * @param stop      stops the search with null when matched before {@code condition}
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends PsiElement> T getParentByCondition(@Nullable PsiElement element,
																boolean strict,
																@Nonnull Condition<? super PsiElement> condition,
																@Nullable Condition<? super PsiElement> stop)
	{
		PsiElement current = strict && element != null ? element.getParent() : element;
		while(current != null)
		{
			if(condition.value(current))
			{
				return (T) current;
			}
			if(current instanceof PsiFile || stop != null && stop.value(current))
			{
				return null;
			}
			current = current.getParent();
		}
		return null;
	}

	@Nullable
	public static <T extends PsiElement> T getParentByCondition(@Nullable PsiElement element, @Nonnull Condition<? super PsiElement> condition)
	{
		return getParentByCondition(element, true, condition, null);
	}

	@Nullable
	public static <T extends PsiElement> T getParentOfClass(@Nullable PsiElement element, boolean strict, @Nonnull Class<T> aClass)
	{
		return getParentByCondition(element, strict, aClass::isInstance, null);
	}

	private static boolean isWhitespaceOrComment(PsiElement element, boolean ignoreComments)
	{
		return element instanceof PsiWhiteSpace || ignoreComments && element instanceof PsiComment;
	}
}
