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

package com.jetbrains.php.lang.psi.elements.impl;


import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpModifierList;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.impl.PhpParameterImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpParameterStubImpl;
import consulo.util.lang.BitUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Constructor parameter with a visibility or {@code readonly}, which declares a field too:
 * {@code public function __construct(private Foo $foo)}. Like PhpStorm's, it is the {@link Field} of its class.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpPromotedFieldParameterImpl extends PhpParameterImpl implements Field
{
	public PhpPromotedFieldParameterImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	public PhpPromotedFieldParameterImpl(@Nonnull PhpParameterStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isConstant()
	{
		return false;
	}

	@Override
	public boolean isStatic()
	{
		return false;
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PhpClass getContainingClass()
	{
		return getStubOrPsiParentOfType(PhpClass.class);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpModifier getModifier()
	{
		return PhpModifier.instance(getAccess(), PhpModifier.Abstractness.IMPLEMENTED, PhpModifier.State.DYNAMIC);
	}

	@Nonnull
	@RequiredReadAction
	public PhpModifier.Access getAccess()
	{
		PhpParameterStub stub = getGreenStub();
		if(stub != null)
		{
			return PhpParameterStubImpl.getAccess(stub.getFlags());
		}
		// 'readonly' alone is public
		PhpModifierList modifierList = PsiTreeUtil.getChildOfType(this, PhpModifierList.class);
		return modifierList == null ? PhpModifier.Access.PUBLIC : modifierList.getAccess();
	}
}
