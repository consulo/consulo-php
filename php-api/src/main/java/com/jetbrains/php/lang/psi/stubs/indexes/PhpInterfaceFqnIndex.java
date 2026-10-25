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

import consulo.index.io.ID;

/**
 * File index of interface FQNs: case-preserving, with the leading backslash ({@code \App\Foo}), without values.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpInterfaceFqnIndex
{
	public static final ID<String, Void> KEY = ID.create("php.interface.fqn");

	private PhpInterfaceFqnIndex()
	{
	}
}
