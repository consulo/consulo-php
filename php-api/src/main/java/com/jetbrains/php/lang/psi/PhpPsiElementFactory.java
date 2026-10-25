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

import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpAttributesList;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Locale;

/**
 * Creates PHP PSI from text, in the shape of PhpStorm's {@code PhpPsiElementFactory}.
 * Text without an opening {@code <?php} tag is treated as PHP code.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpPsiElementFactory
{
	private static final String DUMMY_FILE_NAME = "dummy.php";

	private PhpPsiElementFactory()
	{
	}

	/**
	 * Generated code is not bound to a module: it is parsed with the highest language level, which accepts every construct
	 */
	@Nonnull
	@RequiredReadAction
	public static PhpFile createPsiFileFromText(@Nonnull Project project, @Nonnull String text)
	{
		return (PhpFile) PsiFileFactory.getInstance(project).createFileFromText(DUMMY_FILE_NAME, PhpLanguage.INSTANCE, PhpLanguageLevel.DEFAULT, text);
	}

	/**
	 * First element of the class in the code; {@code <?php} is added when missing
	 */
	@Nonnull
	@RequiredReadAction
	public static <T extends PsiElement> T createPhpPsiFromText(@Nonnull Project project, @Nonnull Class<T> aClass, @Nonnull String text)
	{
		T element = PsiTreeUtil.findChildOfType(createPsiFileFromText(project, withOpenTag(text)), aClass);
		if(element == null)
		{
			throw new IllegalArgumentException("no " + aClass.getSimpleName() + " in: " + text);
		}
		return element;
	}

	@Nonnull
	@RequiredReadAction
	public static <T extends PsiElement> T createFromText(@Nonnull Project project, @Nonnull Class<T> aClass, @Nonnull String text)
	{
		return createPhpPsiFromText(project, aClass, text);
	}

	@Nullable
	@RequiredReadAction
	public static <T extends PsiElement> T createFirstFromText(@Nonnull Project project, @Nonnull Class<T> aClass, @Nonnull String text)
	{
		return PsiTreeUtil.findChildOfType(createPsiFileFromText(project, withOpenTag(text)), aClass);
	}

	/**
	 * First element of the type in the code, leaves included; {@code <?php} is added when missing
	 */
	@Nullable
	@RequiredReadAction
	@SuppressWarnings("unchecked")
	public static <T extends PsiElement> T createFromText(@Nonnull Project project, @Nonnull IElementType type, @Nonnull String text)
	{
		PsiElement[] result = new PsiElement[1];
		PsiTreeUtil.processElements(createPsiFileFromText(project, withOpenTag(text)), element -> {
			if(PhpPsiUtil.isOfType(element, type))
			{
				result[0] = element;
				return false;
			}
			return true;
		});
		return (T) result[0];
	}

	/**
	 * @param text method declaration: {@code public function foo() {}}
	 */
	@Nonnull
	@RequiredReadAction
	public static Method createMethod(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, Method.class, "<?php\nclass Dummy {\n" + text + "\n}");
	}

	/**
	 * @param text function declaration: {@code function foo() {}}
	 */
	@Nonnull
	@RequiredReadAction
	public static Function createFunction(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, Function.class, text);
	}

	@Nonnull
	@RequiredReadAction
	public static MethodReference createMethodReference(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, MethodReference.class, text);
	}

	@Nonnull
	@RequiredReadAction
	public static FunctionReference createFunctionReference(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, FunctionReference.class, text);
	}

	/**
	 * @param text parameter declaration with modifiers, attributes or a default value: {@code private Foo $foo = null}
	 */
	@Nonnull
	@RequiredReadAction
	public static Parameter createComplexParameter(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, Parameter.class, "<?php\nclass Dummy { public function __construct(" + text + ") {} }");
	}

	/**
	 * @param text attributes without the brackets: {@code Route('/foo'), Deprecated}
	 */
	@Nonnull
	@RequiredReadAction
	public static PhpAttributesList createAttributesList(@Nonnull Project project, @Nonnull String text)
	{
		return createPhpPsiFromText(project, PhpAttributesList.class, "<?php\n#[" + text + "]\nfunction dummy() {}");
	}

	@Nonnull
	@RequiredReadAction
	public static PsiElement createComma(@Nonnull Project project)
	{
		PsiElement comma = createFromText(project, PhpTokenTypes.opCOMMA, "dummy(1, 2);");
		assert comma != null;
		return comma;
	}

	private static String withOpenTag(String text)
	{
		return text.stripLeading().toLowerCase(Locale.ROOT).startsWith("<?php") ? text : "<?php\n" + text;
	}
}
