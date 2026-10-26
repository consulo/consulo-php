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

import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.impl.PhpStubbedNamedElementImpl;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * {@code A = 1} of {@code const A = 1, B = 2;} outside of classes: a namespaced, case-sensitive constant
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public class ConstantImpl extends PhpStubbedNamedElementImpl<PhpConstantStub> implements Constant
{
	public ConstantImpl(@Nonnull PhpConstantStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public ConstantImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public String getName()
	{
		PhpConstantStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? "" : nameIdentifier.getText();
	}

	/**
	 * @return the value after {@code =}
	 */
	@Nullable
	@Override
	@RequiredReadAction
	public PhpPsiElement getValue()
	{
		return findChildByClass(PhpPsiElement.class);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getValuePresentation()
	{
		PhpConstantStub stub = getStub();
		if(stub != null)
		{
			return stub.getValuePresentation();
		}
		PsiElement value = getValue();
		return value == null ? null : value.getText();
	}

	@Override
	public boolean isCaseSensitive()
	{
		return true;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public String getNamespaceName()
	{
		PhpNamespace namespace = PsiTreeUtil.getStubOrPsiParentOfType(this, PhpNamespace.class);
		String name = namespace == null ? null : namespace.getName();
		if(name == null)
		{
			return "";
		}
		return name.startsWith("\\") ? name.substring(1) : name;
	}

	@Override
	public boolean isWriteAccess()
	{
		return false;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
