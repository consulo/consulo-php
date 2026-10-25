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

import com.jetbrains.php.config.PhpLanguageLevel;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validation of PHP names in the shape of PhpStorm's {@code PhpNameUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpNameUtil
{
	private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_\\x80-\\uffff][a-zA-Z0-9_\\x80-\\uffff]*");

	/**
	 * Reserved words: not usable as a namespace segment before PHP 8.0, which made namespaced names single tokens
	 */
	private static final Set<String> KEYWORDS = Set.of("abstract", "and", "array", "as", "break", "callable", "case", "catch", "class",
		"clone", "const", "continue", "declare", "default", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach",
		"endif", "endswitch", "endwhile", "eval", "exit", "die", "extends", "final", "finally", "fn", "for", "foreach", "function",
		"global", "goto", "if", "implements", "include", "include_once", "instanceof", "insteadof", "interface", "isset", "list",
		"match", "namespace", "new", "or", "print", "private", "protected", "public", "readonly", "require", "require_once",
		"return", "static", "switch", "throw", "trait", "try", "unset", "use", "var", "while", "xor", "yield", "__halt_compiler");

	/**
	 * Names which can not be a class name: keywords and reserved type names
	 */
	private static final Set<String> RESERVED_CLASS_NAMES = Set.of("self", "parent", "static", "int", "float", "bool", "string",
		"true", "false", "null", "void", "iterable", "object", "mixed", "never", "array", "callable", "enum");

	private PhpNameUtil()
	{
	}

	public static boolean isValidName(@Nullable String name)
	{
		return name != null && IDENTIFIER.matcher(name).matches();
	}

	public static boolean isValidClassName(@Nullable String name)
	{
		if(!isValidName(name))
		{
			return false;
		}
		String lower = name.toLowerCase(Locale.ROOT);
		return !KEYWORDS.contains(lower) && !RESERVED_CLASS_NAMES.contains(lower);
	}

	public static boolean isValidNamespaceFullName(@Nullable String fullName, @Nonnull PhpLanguageLevel languageLevel)
	{
		return isValidNamespaceFullName(fullName, false, languageLevel);
	}

	/**
	 * @param allowLeadingBackslash accept fully qualified names like {@code \Foo\Bar}
	 */
	public static boolean isValidNamespaceFullName(@Nullable String fullName, boolean allowLeadingBackslash, @Nonnull PhpLanguageLevel languageLevel)
	{
		if(fullName == null || fullName.isEmpty())
		{
			return false;
		}
		String name = allowLeadingBackslash && fullName.startsWith("\\") ? fullName.substring(1) : fullName;
		if(name.isEmpty())
		{
			return false;
		}
		boolean keywordsAllowed = languageLevel.isAtLeast(PhpLanguageLevel.PHP_8_0);
		for(String segment : name.split("\\\\", -1))
		{
			if(!isValidName(segment))
			{
				return false;
			}
			if(!keywordsAllowed && KEYWORDS.contains(segment.toLowerCase(Locale.ROOT)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Words of a camel case or underscored name: {@code fooBarHTTPClient} becomes {@code foo, Bar, HTTP, Client}
	 */
	@Nonnull
	public static List<String> splitName(@Nonnull String name)
	{
		List<String> result = new ArrayList<>();
		int start = 0;
		int length = name.length();
		for(int i = 1; i <= length; i++)
		{
			boolean boundary = i == length || name.charAt(i) == '_' || isWordStart(name, i);
			if(boundary)
			{
				if(i > start)
				{
					result.add(name.substring(start, i));
				}
				start = i < length && name.charAt(i) == '_' ? i + 1 : i;
			}
		}
		return result;
	}

	private static boolean isWordStart(String name, int i)
	{
		char current = name.charAt(i);
		char previous = name.charAt(i - 1);
		if(Character.isDigit(current) != Character.isDigit(previous) && previous != '_')
		{
			return true;
		}
		if(!Character.isUpperCase(current))
		{
			return false;
		}
		// fooBar, or the last capital of an acronym followed by a word: HTTPClient
		return Character.isLowerCase(previous) || i + 1 < name.length() && Character.isUpperCase(previous) && Character.isLowerCase(name.charAt(i + 1));
	}
}
