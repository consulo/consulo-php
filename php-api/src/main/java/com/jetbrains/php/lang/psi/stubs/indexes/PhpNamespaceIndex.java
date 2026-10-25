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

import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.language.psi.stub.StubIndexKey;

/**
 * Stub index of namespace declarations by lowercased FQN ({@code \app\controller}).
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpNamespaceIndex
{
	public static final StubIndexKey<String, PhpNamespace> KEY = StubIndexKey.createIndexKey("php.namespaces");

	private PhpNamespaceIndex()
	{
	}
}
