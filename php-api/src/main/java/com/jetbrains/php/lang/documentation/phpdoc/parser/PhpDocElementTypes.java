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

package com.jetbrains.php.lang.documentation.phpdoc.parser;

import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;

/**
 * Composite elements of PHPDoc comments.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpDocElementTypes extends PhpDocTokenTypes
{
	PhpDocElementType phpDocTag = PhpDocStubElementTypes.phpDocTag;
	PhpDocElementType phpDocText = new PhpDocElementType("PhpDocText");
	PhpDocElementType phpDocInlineTag = new PhpDocElementType("PhpDocInlineTag");
	PhpDocElementType phpDocTagValue = new PhpDocElementType("PhpDocTagValue");
	PhpDocElementType phpDocType = new PhpDocElementType("PhpDocType");
	PhpDocElementType phpDocVariable = new PhpDocElementType("PhpDocVariable");
	/**
	 * {@code (...)} of an annotation: {@code @ORM\\Column(name="x")}
	 */
	PhpDocElementType phpDocAttributeList = new PhpDocElementType("PhpDocAttributeList");
	/**
	 * {@code "x"} inside an attribute list - a {@link StringLiteralExpression}
	 */
	PhpDocElementType phpDocString = new PhpDocElementType("PhpDocString");
}
