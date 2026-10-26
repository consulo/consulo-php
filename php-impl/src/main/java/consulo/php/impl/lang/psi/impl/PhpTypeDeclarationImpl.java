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
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpTypeDeclaration;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-26
 */
public class PhpTypeDeclarationImpl extends PhpElementImpl implements PhpTypeDeclaration
{
	public PhpTypeDeclarationImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@RequiredReadAction
	public List<ClassReference> getClassReferences()
	{
		return List.of(findChildrenByClass(ClassReference.class));
	}

	@Override
	@RequiredReadAction
	public boolean isNullable()
	{
		if(findChildByType(PhpTokenTypes.opQUEST) != null)
		{
			return true;
		}
		for(ClassReference reference : getClassReferences())
		{
			if("null".equalsIgnoreCase(reference.getText()))
			{
				return true;
			}
		}
		return false;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getDeclaredType(this);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
