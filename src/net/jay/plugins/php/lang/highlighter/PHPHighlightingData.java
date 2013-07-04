package net.jay.plugins.php.lang.highlighter;

import java.awt.Color;
import java.awt.Font;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class PHPHighlightingData
{
	public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("PHP_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey("PHP_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey DOC_COMMENT = TextAttributesKey.createTextAttributesKey("PHP_DOC_COMMENT_ID", DefaultLanguageHighlighterColors.DOC_COMMENT);
	public static final TextAttributesKey HEREDOC_ID = TextAttributesKey.createTextAttributesKey("PHP_HEREDOC_ID", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("PHP_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("PHP_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey EXEC_COMMAND = TextAttributesKey.createTextAttributesKey("PHP_EXEC_COMMAND_ID", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
	public static final TextAttributesKey ESCAPE_SEQUENCE = TextAttributesKey.createTextAttributesKey("PHP_ESCAPE_SEQUENCE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
	public static final TextAttributesKey OPERATION_SIGN = TextAttributesKey.createTextAttributesKey("PHP_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey BRACKETS = TextAttributesKey.createTextAttributesKey("PHP_BRACKETS", DefaultLanguageHighlighterColors.BRACES);
	public static final TextAttributesKey PREDEFINED_SYMBOL = TextAttributesKey.createTextAttributesKey("PHP_PREDEFINED SYMBOL", HighlighterColors.TEXT);
	public static final TextAttributesKey EXPR_SUBST_MARKS = TextAttributesKey.createTextAttributesKey("PHP_EXPR_IN_STRING", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
	public static final TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("PHP_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
	public static final TextAttributesKey HEREDOC_CONTENT = TextAttributesKey.createTextAttributesKey("PHP_HEREDOC_CONTENT", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey IDENTIFIER = TextAttributesKey.createTextAttributesKey("PHP_IDENTIFIER", HighlighterColors.TEXT);
	public static final TextAttributesKey CONSTANT = TextAttributesKey.createTextAttributesKey("PHP_CONSTANT", HighlighterColors.TEXT);
	public static final TextAttributesKey VAR = TextAttributesKey.createTextAttributesKey("PHP_VAR");

	public static final TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("PHP_COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey SEMICOLON = TextAttributesKey.createTextAttributesKey("PHP_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);

	public static final TextAttributesKey DOC_TAG = TextAttributesKey.createTextAttributesKey("PHP_DOC_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey DOC_MARKUP = TextAttributesKey.createTextAttributesKey("PHP_MARKUP_ID", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
}
