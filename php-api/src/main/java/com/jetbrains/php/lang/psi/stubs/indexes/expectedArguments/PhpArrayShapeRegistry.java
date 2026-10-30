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

package com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Array shapes of function parameters, from their PHPDoc: {@code @param array{size?: 'sm'|'md'} $options}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpArrayShapeRegistry
{
	public static final PhpArrayShapeRegistry INSTANCE = new PhpArrayShapeRegistry();

	private PhpArrayShapeRegistry()
	{
	}

	/**
	 * @return the shape entries of the parameter at the index, empty without a shape
	 */
	@RequiredReadAction
	public List<PhpShapeEntriesIndex.PhpShapeEntry> getParameterValues(Function function, int index)
	{
		Parameter parameter = function.getParameter(index);
		PhpDocParamTag tag = parameter == null ? null : parameter.getDocTag();
		if(tag == null)
		{
			return List.of();
		}
		List<PhpShapeEntriesIndex.PhpShapeEntry> result = new ArrayList<>();
		for(PhpDocType type : PsiTreeUtil.getChildrenOfTypeAsList(tag, PhpDocType.class))
		{
			result.addAll(parseShape(type.getText()));
		}
		return result;
	}

	/**
	 * @param type {@code array{...}}, {@code list{...}}, {@code non-empty-array{...}}
	 * @return the entries, empty when the type is no shape
	 */
	public static List<PhpShapeEntriesIndex.PhpShapeEntry> parseShape(String type)
	{
		String text = stripDocLines(type).trim();
		int open = text.indexOf('{');
		if(open <= 0 || !text.endsWith("}") || !isShapeName(text.substring(0, open).trim()))
		{
			return List.of();
		}
		List<PhpShapeEntriesIndex.PhpShapeEntry> result = new ArrayList<>();
		int position = 0;
		for(String entry : split(text.substring(open + 1, text.length() - 1), ','))
		{
			String part = entry.trim();
			if(part.isEmpty() || part.equals("..."))
			{
				continue;
			}
			int colon = keyColon(part);
			PhpExpectedFunctionArgument key;
			boolean optional = false;
			String value;
			if(colon < 0)
			{
				key = new PhpExpectedFunctionScalarArgument(position, String.valueOf(position));
				position++;
				value = part;
			}
			else
			{
				String keyText = part.substring(0, colon).trim();
				if(keyText.endsWith("?"))
				{
					optional = true;
					keyText = keyText.substring(0, keyText.length() - 1).trim();
				}
				key = keyArgument(position, keyText);
				value = part.substring(colon + 1).trim();
			}
			for(String member : split(value, '|'))
			{
				String memberType = member.trim();
				result.add(new PhpShapeEntriesIndex.PhpShapeEntry(key, memberType, optional, parseShape(memberType)));
			}
		}
		return result;
	}

	private static PhpExpectedFunctionArgument keyArgument(int position, String keyText)
	{
		if(keyText.length() >= 2 && (keyText.charAt(0) == '\'' || keyText.charAt(0) == '"'))
		{
			return new PhpExpectedFunctionScalarArgument(position, keyText);
		}
		if(keyText.matches("-?\\d+") || keyText.matches("[A-Za-z_][A-Za-z0-9_-]*"))
		{
			return new PhpExpectedFunctionScalarArgument(position, keyText.matches("-?\\d+") ? keyText : "'" + keyText + "'");
		}
		// Foo::BAR and other computed keys
		return new PhpAnyExpressionArgument(position, keyText);
	}

	private static boolean isShapeName(String name)
	{
		String lower = name.toLowerCase(java.util.Locale.ROOT);
		return lower.equals("array") || lower.equals("list") || lower.equals("non-empty-array") || lower.equals("non-empty-list");
	}

	/**
	 * @return the colon of {@code key: value}, not of {@code Foo::BAR}; -1 without a key
	 */
	private static int keyColon(String entry)
	{
		int depth = 0;
		char quote = 0;
		for(int i = 0; i < entry.length(); i++)
		{
			char c = entry.charAt(i);
			if(quote != 0)
			{
				if(c == quote)
				{
					quote = 0;
				}
				continue;
			}
			if(c == '\'' || c == '"')
			{
				quote = c;
			}
			else if(c == '{' || c == '<' || c == '(' || c == '[')
			{
				depth++;
			}
			else if(c == '}' || c == '>' || c == ')' || c == ']')
			{
				depth--;
			}
			else if(c == ':' && depth == 0)
			{
				boolean doubleColon = i + 1 < entry.length() && entry.charAt(i + 1) == ':' || i > 0 && entry.charAt(i - 1) == ':';
				if(!doubleColon)
				{
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Splits at the separator outside of brackets and quotes
	 */
	private static List<String> split(String text, char separator)
	{
		List<String> result = new ArrayList<>();
		int depth = 0;
		char quote = 0;
		int start = 0;
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(quote != 0)
			{
				if(c == quote)
				{
					quote = 0;
				}
				continue;
			}
			if(c == '\'' || c == '"')
			{
				quote = c;
			}
			else if(c == '{' || c == '<' || c == '(' || c == '[')
			{
				depth++;
			}
			else if(c == '}' || c == '>' || c == ')' || c == ']')
			{
				depth--;
			}
			else if(c == separator && depth == 0)
			{
				result.add(text.substring(start, i));
				start = i + 1;
			}
		}
		result.add(text.substring(start));
		return result;
	}

	/**
	 * Multi-line types keep the leading {@code *} of each doc line
	 */
	private static String stripDocLines(String text)
	{
		StringBuilder builder = new StringBuilder();
		for(String line : text.split("\n"))
		{
			String trimmed = line.trim();
			builder.append(trimmed.startsWith("*") ? trimmed.substring(1) : line).append(' ');
		}
		return builder.toString();
	}
}
