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

import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList;
import com.jetbrains.php.lang.psi.elements.PhpFieldType;
import com.jetbrains.php.lang.psi.elements.PhpModifierList;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-26
 */
public class PhpClassFieldsListImpl extends PhpElementImpl implements PhpClassFieldsList
{
	public PhpClassFieldsListImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@RequiredReadAction
	public List<Field> getFields()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, Field.class);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PhpFieldType getFieldType()
	{
		return findChildByClass(PhpFieldType.class);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PhpModifierList getModifierList()
	{
		return findChildByClass(PhpModifierList.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
