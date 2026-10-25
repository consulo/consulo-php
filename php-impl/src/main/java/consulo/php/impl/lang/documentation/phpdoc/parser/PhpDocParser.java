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

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @since Jun 26, 2008
 */
public class PhpDocParser implements PsiParser
{
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion)
	{
		PhpPsiBuilder phpBuilder = new PhpPsiBuilder(builder);
		PsiBuilder.Marker rootMarker = phpBuilder.mark();
		PhpDocParsing.parse(phpBuilder);
		rootMarker.done(root);
		return phpBuilder.getTreeBuilt();
	}
}
