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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generates names from name parts in a naming style: {@code [foo, Bar, Car]} gives {@code fooBarCar, barCar, car}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public enum PhpNameStyle
{
	/**
	 * Names starting with a lower case letter: variables, properties, methods
	 */
	DECAPITALIZE,
	/**
	 * Names starting with an upper case letter: classes
	 */
	CAPITALIZE;

	public static final String CAMEL_CASE = "CAMEL_CASE";
	public static final String SNAKE_CASE = "SNAKE_CASE";

	/**
	 * @param namingStyle {@link #CAMEL_CASE} or {@link #SNAKE_CASE}, see {@code PhpCodeStyleSettings}
	 */
	public NameGenerator withStyle(String namingStyle)
	{
		return new NameGenerator(this, SNAKE_CASE.equalsIgnoreCase(namingStyle));
	}

	public static final class NameGenerator
	{
		private final PhpNameStyle myStyle;
		private final boolean mySnakeCase;

		private NameGenerator(PhpNameStyle style, boolean snakeCase)
		{
			myStyle = style;
			mySnakeCase = snakeCase;
		}

		/**
		 * @return one name per suffix of the parts, the longest first
		 */
		public List<String> generateNames(List<String> parts)
		{
			List<String> result = new ArrayList<>();
			for(int start = 0; start < parts.size(); start++)
			{
				result.add(join(parts.subList(start, parts.size())));
			}
			return result;
		}

		private String join(List<String> parts)
		{
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < parts.size(); i++)
			{
				String part = parts.get(i);
				if(part.isEmpty())
				{
					continue;
				}
				if(mySnakeCase)
				{
					if(builder.length() > 0)
					{
						builder.append('_');
					}
					builder.append(part.toLowerCase(Locale.ROOT));
				}
				else if(builder.length() == 0)
				{
					builder.append(myStyle == CAPITALIZE ? capitalize(part) : decapitalize(part));
				}
				else
				{
					builder.append(capitalize(part.toLowerCase(Locale.ROOT)));
				}
			}
			return builder.toString();
		}

		private static String capitalize(String text)
		{
			return Character.toUpperCase(text.charAt(0)) + text.substring(1);
		}

		private static String decapitalize(String text)
		{
			return text.toLowerCase(Locale.ROOT);
		}
	}
}
