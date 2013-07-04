package net.jay.plugins.php.lang.highlighter;

import net.jay.plugins.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import net.jay.plugins.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import net.jay.plugins.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import net.jay.plugins.php.lang.lexer.PHPFlexLexer;
import net.jay.plugins.php.lang.lexer.PHPStringLiteralLexer;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;

import com.intellij.lang.StdLanguages;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPHighlightingLexer extends LayeredLexer
{

	public PHPHighlightingLexer()
	{
		super(new FlexAdapter(new PHPFlexLexer(true)));
		LayeredLexer docLexer = new LayeredLexer(new PhpDocLexer());
		registerSelfStoppingLayer(docLexer, new IElementType[]{PHPTokenTypes.DOC_COMMENT}, new IElementType[]{PhpDocTokenTypes.DOC_COMMENT_END});

		//Lexer lexer = getHtmlHighlightingLexer();
		//docLexer.registerLayer(lexer, PHPTokenTypes.DOC_COMMENT);
		// @todo do it!
		registerLayer(new PHPStringLiteralLexer(PHPStringLiteralLexer.NO_QUOTE_CHAR, PHPTokenTypes.STRING_LITERAL, PHPStringLiteralLexer.TYPE_DOUBLE_QUOTE), new IElementType[]{PHPTokenTypes.STRING_LITERAL});
		registerLayer(new PHPStringLiteralLexer(PHPStringLiteralLexer.NO_QUOTE_CHAR, PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PHPStringLiteralLexer.TYPE_SINGLE_QUOTE), new IElementType[]{PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE});
	}

	private static Lexer getHtmlHighlightingLexer()
	{
		return SyntaxHighlighterFactory.getSyntaxHighlighter(StdLanguages.HTML, null, null).getHighlightingLexer();
	}

}