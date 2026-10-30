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

import com.jetbrains.php.config.PhpLanguageFeature;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpTypeDeclaration;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiParserFacade;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Locale;

/**
 * Refactoring helpers, in the shape of PhpStorm's {@code PhpRefactoringUtil}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpRefactoringUtil
{
	private PhpRefactoringUtil()
	{
	}

	/**
	 * For each parameter of a method: adds the field with the name of the parameter when the class has none,
	 * and assigns the parameter to it at the end of the method: {@code $this->foo = $foo;}
	 */
	@RequiredWriteAction
	public static void initializeFieldsByParameters(@Nonnull PhpClass phpClass, @Nonnull List<Parameter> parameters, @Nonnull PhpModifier.Access access)
	{
		Project project = phpClass.getProject();
		boolean typedProperties = PhpLanguageLevel.current(project).hasFeature(PhpLanguageFeature.TYPED_PROPERTIES);
		String modifier = access.name().toLowerCase(Locale.ROOT);

		for(Parameter parameter : parameters)
		{
			String name = parameter.getName();
			Function function = PsiTreeUtil.getParentOfType(parameter, Function.class);
			if(name == null || function == null)
			{
				continue;
			}

			if(phpClass.findOwnFieldByName(name, false) == null)
			{
				PhpTypeDeclaration typeDeclaration = parameter.getTypeDeclaration();
				String type = typedProperties && typeDeclaration != null ? typeDeclaration.getText() + " " : "";
				PhpClassFieldsList field = PhpPsiElementFactory.createPhpPsiFromText(project, PhpClassFieldsList.class,
					"class Dummy { " + modifier + " " + type + "$" + name + "; }");
				insertField(phpClass, field);
			}

			GroupStatement body = PsiTreeUtil.getChildOfType(function, GroupStatement.class);
			if(body != null)
			{
				appendStatement(body, getIndent(function) + "    ", "$this->" + name + " = $" + name + ";");
			}
		}
	}

	/**
	 * Whitespace from the line start to the element
	 */
	@Nonnull
	public static String getIndent(@Nonnull PsiElement element)
	{
		PsiElement previous = element.getPrevSibling();
		if(previous instanceof PsiWhiteSpace)
		{
			String text = previous.getText();
			int lineStart = text.lastIndexOf('\n');
			if(lineStart >= 0)
			{
				return text.substring(lineStart + 1);
			}
		}
		return "";
	}

	/**
	 * Adds the statement at the end of the statement list of a body; the braces of the body are outside of the list
	 */
	private static void appendStatement(@Nonnull GroupStatement body, @Nonnull String indent, @Nonnull String statementText)
	{
		Project project = body.getProject();
		Function dummy = PhpPsiElementFactory.createPhpPsiFromText(project, Function.class, "function dummy() {\n" + statementText + "\n}");
		GroupStatement dummyBody = PsiTreeUtil.getChildOfType(dummy, GroupStatement.class);
		if(dummyBody == null || dummyBody.getFirstChild() == null)
		{
			return;
		}

		PsiParserFacade parserFacade = PsiParserFacade.getInstance(project);
		boolean empty = body.getFirstChild() == null;
		if(!empty)
		{
			body.add(parserFacade.createWhiteSpaceFromText("\n" + indent));
		}
		body.addRange(dummyBody.getFirstChild(), dummyBody.getLastChild());
		if(!empty)
		{
			return;
		}

		// an empty list sits between the line breaks of the braces: {\n<list>}
		String closingIndent = indent.length() >= 4 ? indent.substring(4) : "";
		PsiElement before = body.getPrevSibling();
		if(before instanceof PsiWhiteSpace)
		{
			before.replace(parserFacade.createWhiteSpaceFromText("\n" + indent));
		}
		else
		{
			body.getParent().addBefore(parserFacade.createWhiteSpaceFromText("\n" + indent), body);
		}
		body.getParent().addAfter(parserFacade.createWhiteSpaceFromText("\n" + closingIndent), body);
	}

	/**
	 * After the last field list, or before the first method
	 */
	private static void insertField(@Nonnull PhpClass phpClass, @Nonnull PhpClassFieldsList field)
	{
		PsiParserFacade parserFacade = PsiParserFacade.getInstance(phpClass.getProject());
		List<PhpClassFieldsList> fieldLists = PsiTreeUtil.getChildrenOfTypeAsList(phpClass, PhpClassFieldsList.class);
		if(!fieldLists.isEmpty())
		{
			PhpClassFieldsList last = fieldLists.get(fieldLists.size() - 1);
			PsiElement added = phpClass.addAfter(field, last);
			phpClass.addBefore(parserFacade.createWhiteSpaceFromText("\n" + getIndent(last)), added);
			return;
		}

		Method[] methods = phpClass.getOwnMethods();
		PsiElement anchor = methods.length > 0 ? methods[0] : PhpPsiUtil.getChildOfType(phpClass, PhpTokenTypes.RBRACE);
		PsiElement added = anchor == null ? phpClass.add(field) : phpClass.addBefore(field, anchor);
		String indent = methods.length > 0 ? getIndent(methods[0]) : "    ";
		phpClass.addAfter(parserFacade.createWhiteSpaceFromText("\n\n" + indent), added);
	}
}
