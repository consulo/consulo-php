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
package com.jetbrains.php.lang.psi.stubs;

import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpModifier;

import javax.annotation.Nonnull;

public interface PhpMemberStub<T extends PhpClassMember> extends PhpNamedStub<T>
{
	boolean isStatic();

	boolean isFinal();

	boolean isAbstract();

	boolean isPrivate();

	boolean isProtected();

	@Nonnull
	default PhpModifier.Access getAccess()
	{
		if(isPrivate())
		{
			return PhpModifier.Access.PRIVATE;
		}

		if(isProtected())
		{
			return PhpModifier.Access.PROTECTED;
		}

		return PhpModifier.Access.PUBLIC;
	}
}
