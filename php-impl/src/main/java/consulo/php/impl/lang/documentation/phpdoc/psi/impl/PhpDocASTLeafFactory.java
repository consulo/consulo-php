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

package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocToken;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.ast.IElementType;
import consulo.language.impl.ast.ASTLeafFactory;
import consulo.language.impl.ast.LeafElement;
import consulo.language.version.LanguageVersion;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocTokenType;

/**
 * PHPDoc tokens are {@link PhpDocToken}s.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtensionImpl
public class PhpDocASTLeafFactory implements ASTLeafFactory
{
	@Override
	public LeafElement createLeaf(IElementType type, LanguageVersion languageVersion, CharSequence text)
	{
		return new PhpDocTokenImpl(type, text);
	}

	@Override
	public boolean test(IElementType elementType)
	{
		return elementType instanceof PhpDocTokenType;
	}
}
