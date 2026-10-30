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

package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.PhpCase;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpSwitch;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-30
 */
public class PhpSwitchImpl extends PhpElementImpl implements PhpSwitch
{
	public PhpSwitchImpl(ASTNode node)
	{
		super(node);
	}

	@RequiredReadAction
	@Nullable
	@Override
	public PhpPsiElement getArgument()
	{
		PhpPsiElement first = getFirstPsiChild();
		return first instanceof PhpCase ? null : first;
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public PhpCase[] getAllCases()
	{
		// the branches may be in a group statement
		List<PhpCase> cases = PsiTreeUtil.findChildrenOfType(this, PhpCase.class).stream()
			.filter(phpCase -> PsiTreeUtil.getParentOfType(phpCase, PhpSwitch.class) == this)
			.toList();
		return cases.toArray(PhpCase.EMPTY_ARRAY);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
