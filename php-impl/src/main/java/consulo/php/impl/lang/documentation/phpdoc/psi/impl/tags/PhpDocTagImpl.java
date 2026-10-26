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

import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenType;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;

/**
 * Any PHPDoc tag; subclasses add accessors for well-known tags.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpDocTagImpl extends PhpDocPsiElementImpl implements PhpDocTag
{
	public PhpDocTagImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@RequiredReadAction
	public String getName()
	{
		ASTNode name = getNode().findChildByType(PhpDocTokenTypes.DOC_TAG_NAME);
		return name == null ? "" : name.getText();
	}

	@Override
	@RequiredReadAction
	public String getTagValue()
	{
		StringBuilder builder = new StringBuilder();
		ASTNode name = getNode().findChildByType(PhpDocTokenTypes.DOC_TAG_NAME);
		ASTNode child = name == null ? getNode().getFirstChildNode() : name.getTreeNext();
		for(; child != null; child = child.getTreeNext())
		{
			appendText(child, builder);
		}
		return builder.toString().trim();
	}

	private static void appendText(ASTNode node, StringBuilder builder)
	{
		IElementType type = node.getElementType();
		if(type == PhpDocTokenTypes.DOC_LEADING_ASTERISK)
		{
			return;
		}
		if(type == TokenType.WHITE_SPACE)
		{
			if(builder.length() > 0 && builder.charAt(builder.length() - 1) != ' ')
			{
				builder.append(' ');
			}
			return;
		}
		ASTNode child = node.getFirstChildNode();
		if(child == null)
		{
			builder.append(node.getText());
			return;
		}
		for(; child != null; child = child.getTreeNext())
		{
			appendText(child, builder);
		}
	}

	@Override
	public String toString()
	{
		return "PhpDocTag";
	}

	protected PsiElement findVariable()
	{
		return findChildByType(PhpDocElementTypes.phpDocVariable);
	}
}
