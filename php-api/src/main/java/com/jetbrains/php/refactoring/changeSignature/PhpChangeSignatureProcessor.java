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
package com.jetbrains.php.refactoring.changeSignature;

import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiParserFacade;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Signature changes in the shape of PhpStorm's {@code PhpChangeSignatureProcessor}; only the declaration is changed, not the callers.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpChangeSignatureProcessor
{
	private PhpChangeSignatureProcessor()
	{
	}

	/**
	 * @return the last parameter before the optional and variadic parameters, a new required parameter goes after it
	 */
	@Nullable
	@RequiredReadAction
	public static Parameter findParameterToInsertAfter(@Nonnull Method method)
	{
		Parameter result = null;
		for(Parameter parameter : method.getParameters())
		{
			if(parameter.isOptional() || parameter.isVariadic())
			{
				break;
			}
			result = parameter;
		}
		return result;
	}

	/**
	 * Adds the parameter at the end of the parameter list
	 *
	 * @return the added parameter
	 */
	@Nullable
	@RequiredWriteAction
	public static Parameter appendParameterToParameterList(@Nonnull Function function, @Nonnull Parameter parameter)
	{
		ParameterList parameterList = function.getParameterList();
		if(parameterList == null)
		{
			return null;
		}

		Parameter[] parameters = function.getParameters();
		if(parameters.length == 0)
		{
			// the list has the parentheses
			PsiElement rparen = PhpPsiUtil.getChildOfType(parameterList, PhpTokenTypes.chRPAREN);
			return (Parameter) (rparen == null ? parameterList.add(parameter) : parameterList.addBefore(parameter, rparen));
		}
		return insertAfter(parameterList, parameter, parameters[parameters.length - 1]);
	}

	/**
	 * Adds the new parameters before the optional and variadic parameters of the function
	 */
	@RequiredWriteAction
	public static void addParameterToFunctionSignature(@Nonnull Project project, @Nonnull Function function, @Nonnull List<PhpParameterInfo> parameters)
	{
		ParameterList parameterList = function.getParameterList();
		if(parameterList == null)
		{
			return;
		}

		for(PhpParameterInfo info : parameters)
		{
			Parameter parameter = PhpPsiElementFactory.createComplexParameter(project, getParameterText(info));

			Parameter anchor = null;
			Parameter firstOptional = null;
			for(Parameter existing : function.getParameters())
			{
				if(existing.isOptional() || existing.isVariadic())
				{
					firstOptional = existing;
					break;
				}
				anchor = existing;
			}

			if(info.getDefaultValue() == null && firstOptional != null)
			{
				// a required parameter goes before the optional ones
				if(anchor != null)
				{
					insertAfter(parameterList, parameter, anchor);
				}
				else
				{
					insertBefore(parameterList, parameter, firstOptional);
				}
			}
			else
			{
				appendParameterToParameterList(function, parameter);
			}
		}
	}

	@Nonnull
	private static String getParameterText(@Nonnull PhpParameterInfo info)
	{
		StringBuilder builder = new StringBuilder();
		String typeText = info.getTypeText();
		if(StringUtil.isEmpty(typeText) && !info.getType().isEmpty())
		{
			typeText = info.getType().toString();
		}
		if(!StringUtil.isEmpty(typeText))
		{
			builder.append(typeText).append(' ');
		}
		builder.append('$').append(info.getName());
		if(info.getDefaultValue() != null)
		{
			builder.append(" = ").append(info.getDefaultValue());
		}
		return builder.toString();
	}

	@Nonnull
	private static Parameter insertAfter(@Nonnull ParameterList parameterList, @Nonnull Parameter parameter, @Nonnull Parameter anchor)
	{
		// anchor, new
		PsiElement comma = parameterList.addAfter(PhpPsiElementFactory.createComma(parameterList.getProject()), anchor);
		Parameter added = (Parameter) parameterList.addAfter(parameter, comma);
		parameterList.addBefore(createSpace(parameterList), added);
		return added;
	}

	private static void insertBefore(@Nonnull ParameterList parameterList, @Nonnull Parameter parameter, @Nonnull Parameter anchor)
	{
		// new, anchor
		Parameter added = (Parameter) parameterList.addBefore(parameter, anchor);
		PsiElement comma = parameterList.addAfter(PhpPsiElementFactory.createComma(parameterList.getProject()), added);
		parameterList.addAfter(createSpace(parameterList), comma);
	}

	@Nonnull
	private static PsiElement createSpace(@Nonnull PsiElement context)
	{
		return PsiParserFacade.getInstance(context.getProject()).createWhiteSpaceFromText(" ");
	}
}
