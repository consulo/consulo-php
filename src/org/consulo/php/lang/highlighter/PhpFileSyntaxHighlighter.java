package org.consulo.php.lang.highlighter;

import com.intellij.lang.LanguageVersion;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import org.consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.fileTypes.LanguageVersionableSyntaxHighlighter;
import org.consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxim.Mossienko
 *         Date: 29.01.2009
 *         Time: 22:30:17
 */
public class PhpFileSyntaxHighlighter extends LanguageVersionableSyntaxHighlighter {
	private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();
	private static final Map<IElementType, TextAttributesKey> DOC_ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

	public PhpFileSyntaxHighlighter(LanguageVersion languageVersion) {
		super(languageVersion);
	}

	@Override
	public Lexer getHighlightingLexer(LanguageVersion languageVersion) {
		return new PhpHighlightingLexer((PhpLanguageLevel) languageVersion);
	}

	@Override
	@NotNull
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		return pack(ATTRIBUTES.get(tokenType), DOC_ATTRIBUTES.get(tokenType));
	}

	static {
		safeMap(ATTRIBUTES, PhpTokenTypes.tsCOMMENTS, PhpHighlightingData.COMMENT);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsNUMBERS, PhpHighlightingData.NUMBER);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsCONSTANTS, PhpHighlightingData.CONSTANT);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsSTRING_EDGE, PhpHighlightingData.STRING);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsEXPR_SUBST_MARKS, PhpHighlightingData.EXPR_SUBST_MARKS);
		//safeMap(ATTRIBUTES, PhpTokenTypes.tsBINARY_OPS, PhpHighlightingData.OPERATION_SIGN);
		safeMap(ATTRIBUTES, PhpTokenTypes.KEYWORDS, PhpHighlightingData.KEYWORD);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsBRACKETS, PhpHighlightingData.BRACKETS);
		safeMap(ATTRIBUTES, PhpTokenTypes.tsHEREDOC_IDS, PhpHighlightingData.HEREDOC_ID);
		ATTRIBUTES.put(PhpTokenTypes.HEREDOC_CONTENTS, PhpHighlightingData.HEREDOC_CONTENT);
		ATTRIBUTES.put(PhpTokenTypes.opCOMMA, PhpHighlightingData.COMMA);
		ATTRIBUTES.put(PhpTokenTypes.opSEMICOLON, PhpHighlightingData.SEMICOLON);
		ATTRIBUTES.put(PhpTokenTypes.STRING_LITERAL, PhpHighlightingData.STRING);
		ATTRIBUTES.put(PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PhpHighlightingData.STRING);
		ATTRIBUTES.put(PhpTokenTypes.EXEC_COMMAND, PhpHighlightingData.EXEC_COMMAND);
		ATTRIBUTES.put(PhpTokenTypes.chBACKTRICK, PhpHighlightingData.EXEC_COMMAND);
		ATTRIBUTES.put(PhpTokenTypes.IDENTIFIER, PhpHighlightingData.IDENTIFIER);
		ATTRIBUTES.put(PhpTokenTypes.VARIABLE, PhpHighlightingData.VAR);
		ATTRIBUTES.put(StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN, PhpHighlightingData.ESCAPE_SEQUENCE);
		ATTRIBUTES.put(StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN, PhpHighlightingData.BAD_CHARACTER);
		ATTRIBUTES.put(PhpTokenTypes.UNKNOWN_SYMBOL, PhpHighlightingData.BAD_CHARACTER);

		ATTRIBUTES.put(PhpTokenTypes.PHP_OPENING_TAG, PhpHighlightingData.TAG);
		ATTRIBUTES.put(PhpTokenTypes.PHP_ECHO_OPENING_TAG, PhpHighlightingData.TAG);
		ATTRIBUTES.put(PhpTokenTypes.PHP_CLOSING_TAG, PhpHighlightingData.TAG);
		registerPHPDoc();
	}

	private static void registerHtmlMarkup(IElementType[] htmlTokens, IElementType[] htmlTokens2) {
		for (IElementType idx : htmlTokens) {
			ATTRIBUTES.put(idx, PhpHighlightingData.DOC_COMMENT);
			DOC_ATTRIBUTES.put(idx, PhpHighlightingData.DOC_MARKUP);
		}

		for (IElementType idx : htmlTokens2) {
			ATTRIBUTES.put(idx, PhpHighlightingData.DOC_COMMENT);
		}
	}

	private static void registerPHPDoc() {
		ATTRIBUTES.put(PhpDocTokenTypes.DOC_TAG_NAME, PhpHighlightingData.DOC_COMMENT);
		DOC_ATTRIBUTES.put(PhpDocTokenTypes.DOC_TAG_NAME, PhpHighlightingData.DOC_TAG);

		IElementType[] javadoc = IElementType.enumerate(new IElementType.Predicate() {
			@Override
			public boolean matches(IElementType type) {
				return type instanceof PhpDocElementType;
			}
		});

		for (IElementType type : javadoc) {
			ATTRIBUTES.put(type, PhpHighlightingData.DOC_COMMENT);
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
