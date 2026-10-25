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

package com.jetbrains.php.lang.psi.stubs.indexes;

import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import consulo.language.psi.stub.DefaultFileTypeSpecificInputFilter;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.StubIndexKey;

/**
 * Stub index of {@code define()} constants by name.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpConstantNameIndex
{
	public static final StubIndexKey<String, PhpDefine> KEY = StubIndexKey.createIndexKey("php.defines");

	/**
	 * Input filter of PHP files, for file indexes over PHP code
	 */
	public static final FileBasedIndex.InputFilter PHP_INPUT_FILTER = new DefaultFileTypeSpecificInputFilter(PhpFileType.INSTANCE);

	private PhpConstantNameIndex()
	{
	}
}
