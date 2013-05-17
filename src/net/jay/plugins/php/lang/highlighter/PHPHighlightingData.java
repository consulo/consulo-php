package net.jay.plugins.php.lang.highlighter;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class PHPHighlightingData {

	@NonNls
	static final String KEYWORD_ID = "PHP_KEYWORD";
	@NonNls
	static final String COMMENT_ID = "PHP_COMMENT";
	@NonNls
	static final String HEREDOC_ID_ID = "PHP_HEREDOC_ID";
	@NonNls
	static final String HEREDOC_CONTENT_ID = "PHP_HEREDOC_CONTENT";
	@NonNls
	static final String NUMBER_ID = "PHP_NUMBER";
	@NonNls
	static final String STRING_ID = "PHP_STRING";
	@NonNls
	static final String ESCAPE_SEQUENCE_ID = "PHP_ESCAPE_SEQUENCE";
	@NonNls
	static final String OPERATION_SIGN_ID = "PHP_OPERATION_SIGN";
	@NonNls
	static final String BRACKETS_ID = "PHP_BRACKETS";
	@NonNls
	static final String EXPR_IN_STRING_ID = "PHP_EXPR_IN_STRING";
	@NonNls
	static final String BAD_CHARACTER_ID = "PHP_BAD_CHARACTER";
	@NonNls
	static final String IDENTIFIER_ID = "PHP_IDENTIFIER";
	@NonNls
	static final String CONSTANT_ID = "PHP_CONSTANT";
	@NonNls
	static final String VAR_ID = "PHP_VAR";
	@NonNls
	static final String COMMA_ID = "PHP_COMMA";
	@NonNls
	static final String SEMICOLON_ID = "PHP_SEMICOLON";
	@NonNls
	static final String DOC_TAG_ID = "PHP_DOC_TAG";
	@NonNls
	static final String DOC_MARKUP_ID = "PHP_MARKUP_ID";
	@NonNls
	static final String DOC_COMMENT_ID = "PHP_DOC_COMMENT_ID";
	@NonNls
	static final String EXEC_COMMAND_ID = "PHP_EXEC_COMMAND_ID";
    @NonNls
    static final String PREDEFINED_SYMBOL_ID = "PHP_PREDEFINED SYMBOL";

	public static final TextAttributesKey KEYWORD =
		TextAttributesKey.createTextAttributesKey(KEYWORD_ID, SyntaxHighlighterColors.KEYWORD.getDefaultAttributes().clone());
	public static final TextAttributesKey COMMENT =
		TextAttributesKey.createTextAttributesKey(COMMENT_ID, SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes().clone());
	public static final TextAttributesKey DOC_COMMENT =
		TextAttributesKey.createTextAttributesKey(DOC_COMMENT_ID, SyntaxHighlighterColors.DOC_COMMENT.getDefaultAttributes().clone());
	public static final TextAttributesKey HEREDOC_ID =
		TextAttributesKey.createTextAttributesKey(HEREDOC_ID_ID, SyntaxHighlighterColors.DOC_COMMENT_TAG.getDefaultAttributes().clone());
	public static final TextAttributesKey NUMBER =
		TextAttributesKey.createTextAttributesKey(NUMBER_ID, SyntaxHighlighterColors.NUMBER.getDefaultAttributes().clone());
	public static final TextAttributesKey STRING =
		TextAttributesKey.createTextAttributesKey(STRING_ID, SyntaxHighlighterColors.STRING.getDefaultAttributes().clone());
	public static final TextAttributesKey EXEC_COMMAND =
		TextAttributesKey.createTextAttributesKey(EXEC_COMMAND_ID, SyntaxHighlighterColors.STRING.getDefaultAttributes().clone());
	public static final TextAttributesKey ESCAPE_SEQUENCE =
		TextAttributesKey.createTextAttributesKey(ESCAPE_SEQUENCE_ID, SyntaxHighlighterColors.VALID_STRING_ESCAPE.getDefaultAttributes().clone());
	public static final TextAttributesKey OPERATION_SIGN =
		TextAttributesKey.createTextAttributesKey(OPERATION_SIGN_ID, SyntaxHighlighterColors.OPERATION_SIGN.getDefaultAttributes().clone());
	public static final TextAttributesKey BRACKETS =
		TextAttributesKey.createTextAttributesKey(BRACKETS_ID, SyntaxHighlighterColors.BRACES.getDefaultAttributes().clone());
  public static final TextAttributesKey PREDEFINED_SYMBOL =
		TextAttributesKey.createTextAttributesKey(PREDEFINED_SYMBOL_ID, HighlighterColors.TEXT.getDefaultAttributes().clone());
  public static final TextAttributesKey EXPR_SUBST_MARKS =
		TextAttributesKey.createTextAttributesKey(EXPR_IN_STRING_ID, SyntaxHighlighterColors.DOC_COMMENT_MARKUP.getDefaultAttributes().clone());
	public static final TextAttributesKey BAD_CHARACTER =
		TextAttributesKey.createTextAttributesKey(BAD_CHARACTER_ID, HighlighterColors.BAD_CHARACTER.getDefaultAttributes().clone());
	public static final TextAttributesKey HEREDOC_CONTENT =
		TextAttributesKey.createTextAttributesKey(HEREDOC_CONTENT_ID, SyntaxHighlighterColors.STRING.getDefaultAttributes().clone());

	public static final TextAttributesKey IDENTIFIER =
		TextAttributesKey.createTextAttributesKey(IDENTIFIER_ID, HighlighterColors.TEXT.getDefaultAttributes().clone());
	public static final TextAttributesKey CONSTANT =
		TextAttributesKey.createTextAttributesKey(CONSTANT_ID, HighlighterColors.TEXT.getDefaultAttributes().clone());
	public static final TextAttributesKey VAR =
		TextAttributesKey.createTextAttributesKey(VAR_ID, SyntaxHighlighterColors.KEYWORD.getDefaultAttributes().clone());

	public static final TextAttributesKey COMMA =
		TextAttributesKey.createTextAttributesKey(COMMA_ID, SyntaxHighlighterColors.COMMA.getDefaultAttributes().clone());
	public static final TextAttributesKey SEMICOLON =
		TextAttributesKey.createTextAttributesKey(SEMICOLON_ID, SyntaxHighlighterColors.JAVA_SEMICOLON.getDefaultAttributes().clone());

	public static final TextAttributesKey DOC_TAG =
		TextAttributesKey.createTextAttributesKey(DOC_TAG_ID, SyntaxHighlighterColors.DOC_COMMENT_TAG.getDefaultAttributes().clone());
	public static final TextAttributesKey DOC_MARKUP =
		TextAttributesKey.createTextAttributesKey(DOC_MARKUP_ID, SyntaxHighlighterColors.DOC_COMMENT_MARKUP.getDefaultAttributes().clone());

	static {
		VAR.getDefaultAttributes().setFontType(Font.PLAIN);
		VAR.getDefaultAttributes().setForegroundColor(new Color(0x903040));
		EXEC_COMMAND.getDefaultAttributes().setBackgroundColor(new Color(0xe3fcff));
        PREDEFINED_SYMBOL.getDefaultAttributes().setFontType(Font.ITALIC);
	}

}
