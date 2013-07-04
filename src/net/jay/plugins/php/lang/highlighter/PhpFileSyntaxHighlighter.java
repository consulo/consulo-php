package net.jay.plugins.php.lang.highlighter;

import java.util.HashMap;
import java.util.Map;

import net.jay.plugins.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;

import org.jetbrains.annotations.NotNull;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;

/**
 * @author Maxim.Mossienko
 *         Date: 29.01.2009
 *         Time: 22:30:17
 */
public class PhpFileSyntaxHighlighter extends SyntaxHighlighterBase
{
	private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();
	private static final Map<IElementType, TextAttributesKey> DOC_ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

	@NotNull
	public Lexer getHighlightingLexer()
	{
		return new PHPHighlightingLexer();
	}

	@NotNull
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		return pack(ATTRIBUTES.get(tokenType), DOC_ATTRIBUTES.get(tokenType));
	}

	static
	{
		safeMap(ATTRIBUTES, PHPTokenTypes.tsCOMMENTS, PHPHighlightingData.COMMENT);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsNUMBERS, PHPHighlightingData.NUMBER);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsCONSTANTS, PHPHighlightingData.CONSTANT);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsSTRING_EDGE, PHPHighlightingData.STRING);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsEXPR_SUBST_MARKS, PHPHighlightingData.EXPR_SUBST_MARKS);
		//safeMap(ATTRIBUTES, PHPTokenTypes.tsBINARY_OPS, PHPHighlightingData.OPERATION_SIGN);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsKEYWORDS, PHPHighlightingData.KEYWORD);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsBRACKETS, PHPHighlightingData.BRACKETS);
		safeMap(ATTRIBUTES, PHPTokenTypes.tsHEREDOC_IDS, PHPHighlightingData.HEREDOC_ID);
		ATTRIBUTES.put(PHPTokenTypes.HEREDOC_CONTENTS, PHPHighlightingData.HEREDOC_CONTENT);
		ATTRIBUTES.put(PHPTokenTypes.opCOMMA, PHPHighlightingData.COMMA);
		ATTRIBUTES.put(PHPTokenTypes.opSEMICOLON, PHPHighlightingData.SEMICOLON);
		ATTRIBUTES.put(PHPTokenTypes.STRING_LITERAL, PHPHighlightingData.STRING);
		ATTRIBUTES.put(PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PHPHighlightingData.STRING);
		ATTRIBUTES.put(PHPTokenTypes.EXEC_COMMAND, PHPHighlightingData.EXEC_COMMAND);
		ATTRIBUTES.put(PHPTokenTypes.chBACKTRICK, PHPHighlightingData.EXEC_COMMAND);
		ATTRIBUTES.put(PHPTokenTypes.IDENTIFIER, PHPHighlightingData.IDENTIFIER);
		ATTRIBUTES.put(PHPTokenTypes.VARIABLE, PHPHighlightingData.VAR);
		ATTRIBUTES.put(StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN, PHPHighlightingData.ESCAPE_SEQUENCE);
		ATTRIBUTES.put(StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN, PHPHighlightingData.BAD_CHARACTER);
		ATTRIBUTES.put(PHPTokenTypes.UNKNOWN_SYMBOL, PHPHighlightingData.BAD_CHARACTER);
		ATTRIBUTES.put(PHPTokenTypes.PREDEFINED_IDENTIFIER, PHPHighlightingData.PREDEFINED_SYMBOL);
		registerPHPDoc();
	}

	private static void registerHtmlMarkup(IElementType[] htmlTokens, IElementType[] htmlTokens2)
	{
		for(IElementType idx : htmlTokens)
		{
			ATTRIBUTES.put(idx, PHPHighlightingData.DOC_COMMENT);
			DOC_ATTRIBUTES.put(idx, PHPHighlightingData.DOC_MARKUP);
		}

		for(IElementType idx : htmlTokens2)
		{
			ATTRIBUTES.put(idx, PHPHighlightingData.DOC_COMMENT);
		}
	}

	private static void registerPHPDoc()
	{
		ATTRIBUTES.put(PhpDocTokenTypes.DOC_TAG_NAME, PHPHighlightingData.DOC_COMMENT);
		DOC_ATTRIBUTES.put(PhpDocTokenTypes.DOC_TAG_NAME, PHPHighlightingData.DOC_TAG);

		IElementType[] javadoc = IElementType.enumerate(new IElementType.Predicate()
		{
			public boolean matches(IElementType type)
			{
				return type instanceof PhpDocElementType;
			}
		});

		for(IElementType type : javadoc)
		{
			ATTRIBUTES.put(type, PHPHighlightingData.DOC_COMMENT);
		}

		IElementType javaDocMarkup[] = {
				XmlTokenType.XML_START_TAG_START,
				XmlTokenType.XML_END_TAG_START,
				XmlTokenType.XML_TAG_END,
				XmlTokenType.XML_EMPTY_ELEMENT_END,
				XmlTokenType.TAG_WHITE_SPACE,
				XmlTokenType.XML_TAG_NAME,
				XmlTokenType.XML_NAME,
				XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN,
				XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER,
				XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER,
				XmlTokenType.XML_EQ
		};

		IElementType javaDocMarkup2[] = {
				XmlTokenType.XML_DATA_CHARACTERS,
				XmlTokenType.XML_REAL_WHITE_SPACE,
				XmlTokenType.TAG_WHITE_SPACE
		};
		registerHtmlMarkup(javaDocMarkup, javaDocMarkup2);
	}
}
