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

package com.jetbrains.php.lang;

import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * PHP name rules in the shape of PhpStorm's {@code PhpLangUtil}: class, function and method names are case-insensitive,
 * fully qualified names start with {@code \}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpLangUtil
{
	public static final String GLOBAL_NAMESPACE_NAME = "\\";
	public static final char NAMESPACE_SEPARATOR = '\\';

	private PhpLangUtil()
	{
	}

	public static boolean equalsClassNames(@Nullable CharSequence first, @Nullable CharSequence second)
	{
		return first == null || second == null ? first == second : StringUtil.equalsIgnoreCase(first, second);
	}

	public static boolean equalsFunctionNames(@Nullable CharSequence first, @Nullable CharSequence second)
	{
		return equalsClassNames(first, second);
	}

	public static boolean equalsMethodNames(@Nullable CharSequence first, @Nullable CharSequence second)
	{
		return equalsClassNames(first, second);
	}

	public static boolean equalsNamespaceNames(@Nullable CharSequence first, @Nullable CharSequence second)
	{
		return equalsClassNames(first, second);
	}

	public static boolean isFqn(@Nullable String name)
	{
		return name != null && name.startsWith(GLOBAL_NAMESPACE_NAME);
	}

	public static boolean isGlobalNamespaceFQN(@Nullable String fqn)
	{
		return GLOBAL_NAMESPACE_NAME.equals(fqn);
	}

	public static boolean isGlobalNamespaceName(@Nullable String name)
	{
		return StringUtil.isEmpty(name) || GLOBAL_NAMESPACE_NAME.equals(name);
	}

	/**
	 * {@code Foo\Bar} becomes {@code \Foo\Bar}
	 */
	@Nonnull
	public static String toFQN(@Nonnull String name)
	{
		return isFqn(name) ? name : GLOBAL_NAMESPACE_NAME + name;
	}

	/**
	 * {@code \Foo\Bar} becomes {@code Foo\Bar}
	 */
	@Nonnull
	public static String toPresentableFQN(@Nonnull String fqn)
	{
		return StringUtil.trimStart(fqn, GLOBAL_NAMESPACE_NAME);
	}

	/**
	 * {@code \Foo\Bar} becomes {@code Bar}
	 */
	@Nonnull
	public static String toName(@Nonnull String fqn)
	{
		return fqn.substring(fqn.lastIndexOf(NAMESPACE_SEPARATOR) + 1);
	}

	/**
	 * Namespace FQN with the trailing separator: {@code \Foo\Bar} becomes {@code \Foo\}, {@code \Bar} becomes {@code \}
	 */
	@Nonnull
	public static String getParentNamespaceFQN(@Nonnull String fqn)
	{
		String result = toFQN(fqn);
		return result.substring(0, result.lastIndexOf(NAMESPACE_SEPARATOR) + 1);
	}

	@Nonnull
	public static String toFQN(@Nonnull String namespaceName, @Nonnull String name)
	{
		String namespace = toFQN(StringUtil.trimEnd(namespaceName, GLOBAL_NAMESPACE_NAME));
		return isGlobalNamespaceFQN(namespace) ? namespace + name : namespace + NAMESPACE_SEPARATOR + name;
	}
}
