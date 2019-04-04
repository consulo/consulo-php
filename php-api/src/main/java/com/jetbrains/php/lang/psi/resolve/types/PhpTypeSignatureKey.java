// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.jetbrains.php.lang.psi.resolve.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.util.text.StringUtil;

public enum PhpTypeSignatureKey
{
	VARIABLE('V'),
	ARRAY_ELEMENT('E'), //TODO move to ArrayAccessTP
	ARRAY_KEY('Y'), //TODO move to ArrayAccessTP
	PARAMETER('A'),
	FUNCTION('F'),
	CONSTANT('D'),
	CLASS('C'),
	CLASS_CONSTANT('K'),
	FIELD('P'),
	METHOD('M');

	private final char myKey;

	PhpTypeSignatureKey(char key)
	{
		myKey = key;
	}

	public boolean is(char key)
	{
		return myKey == key;
	}

	@Nonnull
	public String sign(@Nullable CharSequence name)
	{
		return "#" + myKey + name;
	}

	@Nonnull
	public String signIfUnsigned(@Nonnull String name)
	{
		return StringUtil.startsWith(name, "#") ? name : sign(name);
	}
}
