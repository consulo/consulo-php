package org.consulo.php.lang.highlighter;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import org.consulo.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import org.consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import org.consulo.php.lang.lexer.PHPFlexLexer;
import org.consulo.php.lang.lexer.PHPStringLiteralLexer;
import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.PhpLanguageLevel;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPHighlightingLexer extends LayeredLexer
{
	public PHPHighlightingLexer(PhpLanguageLevel languageLevel)
	{
		super(new FlexAdapter(new PHPFlexLexer(true, languageLevel)));
		LayeredLexer docLexer = new LayeredLexer(new PhpDocLexer());
		registerSelfStoppingLayer(docLexer, new IElementType[]{PHPTokenTypes.DOC_COMMENT}, new IElementType[]{PhpDocTokenTypes.DOC_COMMENT_END});

		//Lexer lexer = getHtmlHighlightingLexer();
		//docLexer.registerLayer(lexer, PHPTokenTypes.DOC_COMMENT);
		// @todo do it!
		registerLayer(new PHPStringLiteralLexer(PHPStringLiteralLexer.NO_QUOTE_CHAR, PHPTokenTypes.STRING_LITERAL, PHPStringLiteralLexer.TYPE_DOUBLE_QUOTE), new IElementType[]{PHPTokenTypes.STRING_LITERAL});
		registerLayer(new PHPStringLiteralLexer(PHPStringLiteralLexer.NO_QUOTE_CHAR, PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PHPStringLiteralLexer.TYPE_SINGLE_QUOTE), new IElementType[]{PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE});
	}
}