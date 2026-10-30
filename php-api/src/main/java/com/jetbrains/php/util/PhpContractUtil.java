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

package com.jetbrains.php.util;

import com.jetbrains.php.lang.PhpLangUtil;
import org.jspecify.annotations.Nullable;

/**
 * Argument contracts of the PHP API.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpContractUtil
{
	private PhpContractUtil()
	{
	}

	/**
	 * @throws AssertionError when the name is not fully qualified ({@code \Foo\Bar})
	 */
	public static void assertFqn(@Nullable String fqn)
	{
		if(!PhpLangUtil.isFqn(fqn))
		{
			throw new AssertionError("Fully qualified name expected: " + fqn);
		}
	}
}
