package consulo.php.impl.lang.highlighter;

import consulo.codeEditor.DefaultLanguageHighlighterColors;
import consulo.codeEditor.HighlighterColors;
import consulo.colorScheme.TextAttributesKey;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PhpHighlightingData
{
	TextAttributesKey TAG = TextAttributesKey.createTextAttributesKey("PHP_TAG", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);

	TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("PHP_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey("PHP_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	TextAttributesKey DOC_COMMENT = TextAttributesKey.createTextAttributesKey("PHP_DOC_COMMENT_ID", DefaultLanguageHighlighterColors.DOC_COMMENT);
	TextAttributesKey HEREDOC_ID = TextAttributesKey.createTextAttributesKey("PHP_HEREDOC_ID", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("PHP_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("PHP_STRING", DefaultLanguageHighlighterColors.STRING);
	TextAttributesKey EXEC_COMMAND = TextAttributesKey.createTextAttributesKey("PHP_EXEC_COMMAND_ID", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
	TextAttributesKey ESCAPE_SEQUENCE = TextAttributesKey.createTextAttributesKey("PHP_ESCAPE_SEQUENCE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
	TextAttributesKey OPERATION_SIGN = TextAttributesKey.createTextAttributesKey("PHP_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	TextAttributesKey BRACKETS = TextAttributesKey.createTextAttributesKey("PHP_BRACKETS", DefaultLanguageHighlighterColors.BRACES);
	TextAttributesKey PREDEFINED_SYMBOL = TextAttributesKey.createTextAttributesKey("PHP_PREDEFINED_SYMBOL", DefaultLanguageHighlighterColors.STATIC_METHOD);
	TextAttributesKey EXPR_SUBST_MARKS = TextAttributesKey.createTextAttributesKey("PHP_EXPR_IN_STRING", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
	TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("PHP_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
	TextAttributesKey HEREDOC_CONTENT = TextAttributesKey.createTextAttributesKey("PHP_HEREDOC_CONTENT", DefaultLanguageHighlighterColors.STRING);

	TextAttributesKey IDENTIFIER = TextAttributesKey.createTextAttributesKey("PHP_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
	TextAttributesKey CONSTANT = TextAttributesKey.createTextAttributesKey("PHP_CONSTANT", DefaultLanguageHighlighterColors.STATIC_FIELD);
	TextAttributesKey VAR = TextAttributesKey.createTextAttributesKey("PHP_VAR", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

	TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("PHP_COMMA", DefaultLanguageHighlighterColors.COMMA);
	TextAttributesKey SEMICOLON = TextAttributesKey.createTextAttributesKey("PHP_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);

	TextAttributesKey DOC_TAG = TextAttributesKey.createTextAttributesKey("PHP_DOC_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	TextAttributesKey DOC_MARKUP = TextAttributesKey.createTextAttributesKey("PHP_MARKUP_ID", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
}
