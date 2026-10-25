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

package com.jetbrains.php.lang.documentation.phpdoc.lexer;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenType;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocTokenType;

/**
 * Tokens of PHPDoc comments.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpDocTokenTypes
{
	IElementType DOC_COMMENT_START = new PhpDocTokenType("DOC_COMMENT_START");
	IElementType DOC_COMMENT_END = new PhpDocTokenType("DOC_COMMENT_END");
	IElementType DOC_LEADING_ASTERISK = new PhpDocTokenType("DOC_LEADING_ASTERISK");
	/**
	 * {@code @param}, {@code @ORM\\Column}
	 */
	IElementType DOC_TAG_NAME = new PhpDocTokenType("DOC_TAG_NAME");
	IElementType DOC_IDENTIFIER = new PhpDocTokenType("DOC_IDENTIFIER");
	IElementType DOC_VARIABLE = new PhpDocTokenType("DOC_VARIABLE");
	/**
	 * {@code \\} in names
	 */
	IElementType DOC_NAMESPACE = new PhpDocTokenType("DOC_NAMESPACE");
	/**
	 * {@code ::}
	 */
	IElementType DOC_STATIC = new PhpDocTokenType("DOC_STATIC");
	IElementType DOC_STRING = new PhpDocTokenType("DOC_STRING");
	IElementType DOC_INTEGER = new PhpDocTokenType("DOC_INTEGER");
	IElementType DOC_LPAREN = new PhpDocTokenType("DOC_LPAREN");
	IElementType DOC_RPAREN = new PhpDocTokenType("DOC_RPAREN");
	IElementType DOC_LBRACE = new PhpDocTokenType("DOC_LBRACE");
	IElementType DOC_RBRACE = new PhpDocTokenType("DOC_RBRACE");
	IElementType DOC_LBRACKET = new PhpDocTokenType("DOC_LBRACKET");
	IElementType DOC_RBRACKET = new PhpDocTokenType("DOC_RBRACKET");
	IElementType DOC_LAB = new PhpDocTokenType("DOC_LAB");
	IElementType DOC_RAB = new PhpDocTokenType("DOC_RAB");
	IElementType DOC_COMMA = new PhpDocTokenType("DOC_COMMA");
	IElementType DOC_PIPE = new PhpDocTokenType("DOC_PIPE");
	IElementType DOC_AMPERSAND = new PhpDocTokenType("DOC_AMPERSAND");
	IElementType DOC_DOT = new PhpDocTokenType("DOC_DOT");
	/**
	 * Any other text, {@code =} included
	 */
	IElementType DOC_TEXT = new PhpDocTokenType("DOC_TEXT");
	IElementType DOC_HTML_TAG = new PhpDocTokenType("DOC_HTML_TAG");
	IElementType DOC_IGNORED = new PhpDocTokenType("DOC_IGNORED");
	IElementType DOC_WHITESPACE = TokenType.WHITE_SPACE;
}
