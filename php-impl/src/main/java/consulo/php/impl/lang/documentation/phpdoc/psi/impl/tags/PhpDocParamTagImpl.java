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

package consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * {@code @param Type $name description}
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpDocParamTagImpl extends PhpDocTagImpl implements PhpDocParamTag
{
	public PhpDocParamTagImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getVarName()
	{
		PsiElement variable = findVariable();
		if(variable == null)
		{
			return null;
		}
		String text = variable.getText();
		int dollar = text.indexOf('$');
		return dollar < 0 ? text : text.substring(dollar + 1);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getTypeString()
	{
		StringBuilder builder = new StringBuilder();
		for(PhpDocType type : findChildrenByClass(PhpDocType.class))
		{
			if(builder.length() > 0)
			{
				builder.append('|');
			}
			builder.append(type.getName());
		}
		return builder.length() == 0 ? null : builder.toString();
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		PhpType.PhpTypeBuilder builder = PhpType.builder();
		for(PhpDocType type : findChildrenByClass(PhpDocType.class))
		{
			builder.add(PhpTypeInference.getDocType(type));
		}
		return builder.build();
	}
}
