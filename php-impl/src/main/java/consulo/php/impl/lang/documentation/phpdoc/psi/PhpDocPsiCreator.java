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

package consulo.php.impl.lang.documentation.phpdoc.psi;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocStringImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocParamTagImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocReturnTagImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocVarTagImpl;

import java.util.Locale;

/**
 * @author jay
 * @since Jun 29, 2008
 */
public class PhpDocPsiCreator implements PhpDocElementTypes
{
	public static PsiElement createElement(ASTNode node)
	{
		IElementType type = node.getElementType();
		if(type == phpDocTag)
		{
			ASTNode name = node.findChildByType(DOC_TAG_NAME);
			String tagName = name == null ? "" : name.getText().toLowerCase(Locale.ROOT);
			return switch(tagName)
			{
				case "@var", "@psalm-var", "@phpstan-var" -> new PhpDocVarTagImpl(node);
				case "@return", "@psalm-return", "@phpstan-return" -> new PhpDocReturnTagImpl(node);
				case "@param", "@psalm-param", "@phpstan-param" -> new PhpDocParamTagImpl(node);
				default -> new PhpDocTagImpl(node);
			};
		}
		if(type == phpDocType)
		{
			return new PhpDocTypeImpl(node);
		}
		if(type == phpDocString)
		{
			return new PhpDocStringImpl(node);
		}
		return new PhpDocPsiElementImpl(node);
	}
}
