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

package consulo.php.impl.lang.documentation.phpdoc.parser;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.ast.ASTNode;
import consulo.language.ast.ILazyParseableElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionUtil;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocCommentImpl;

/**
 * @author jay
 * @since Jun 26, 2008
 */
public interface PhpDocElementTypes extends com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes, PhpDocTokenTypes
{
	ILazyParseableElementType DOC_COMMENT = new ILazyParseableElementType("PhpDocComment", PhpLanguage.INSTANCE)
	{
		@Override
		public ASTNode parseContents(ASTNode chameleon)
		{
			PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
			PsiElement parentElement = chameleon.getTreeParent().getPsi();
			LanguageVersion defaultVersion = LanguageVersionUtil.findDefaultVersion(getLanguage());
			PsiBuilder builder = factory.createBuilder(parentElement.getProject(), chameleon, new PhpDocLexer(), getLanguage(), defaultVersion, chameleon.getText());
			PsiParser parser = new PhpDocParser();
			return parser.parse(this, builder, defaultVersion).getFirstChildNode();
		}

		@Override
		public ASTNode createNode(CharSequence text)
		{
			return new PhpDocCommentImpl(text);
		}
	};
}
