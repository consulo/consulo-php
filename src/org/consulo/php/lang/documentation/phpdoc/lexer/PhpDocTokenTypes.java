package org.consulo.php.lang.documentation.phpdoc.lexer;

import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @date Jun 26, 2008 10:10:24 PM
 */
public interface PhpDocTokenTypes
{

	IElementType DOC_COMMENT_START = new PhpDocElementType("DOC_COMMENT_START");
	IElementType DOC_COMMENT_END = new PhpDocElementType("DOC_COMMENT_END");

	IElementType DOC_TAG_NAME = new PhpDocElementType("DOC_TAG_NAME");
	IElementType DOC_IDENTIFIER = new PhpDocElementType("DOC_IDENTIFIER");
	IElementType DOC_DOT = new PhpDocElementType("DOC_DOT");
	IElementType DOC_PIPE = new PhpDocElementType("DOC_PIPE");
	IElementType DOC_LBRACE = new PhpDocElementType("DOC_LBRACE");
	IElementType DOC_RBRACE = new PhpDocElementType("DOC_RBRACE");
	IElementType DOC_VARIABLE = new PhpDocElementType("DOC_VARIABLE");

	IElementType DOC_LEADING_ASTERISK = new PhpDocElementType("DOC_LEADING_ASTERISK");
	IElementType DOC_WHITESPACE = new PhpDocElementType("DOC_WHITESPACE");
	IElementType DOC_TEXT = new PhpDocElementType("DOC_TEXT");
	IElementType DOC_IGNORED = new PhpDocElementType("DOC_IGNORED");
	IElementType DOC_HTML_TAG = new PhpDocElementType("DOC_HTML_TAG");

	TokenSet DOC_TAG_VALUE_END = TokenSet.create(DOC_COMMENT_END, DOC_IGNORED, DOC_LEADING_ASTERISK);

}
