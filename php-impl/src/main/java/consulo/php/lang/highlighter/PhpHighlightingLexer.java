package consulo.php.lang.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpStringLiteralLexer;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpHighlightingLexer extends LayeredLexer
{
	public PhpHighlightingLexer(PhpLanguageLevel languageLevel)
	{
		super(new PhpFlexLexer(true, languageLevel));

		registerSelfStoppingLayer(new PhpDocLexer(), new IElementType[]{PhpDocElementTypes.DOC_COMMENT}, new IElementType[]{PhpDocTokenTypes.DOC_COMMENT_END});

		//Lexer lexer = getHtmlHighlightingLexer();
		//docLexer.registerLayer(lexer, PhpTokenTypes.DOC_COMMENT);
		// @todo do it!
		registerLayer(new PhpStringLiteralLexer(PhpStringLiteralLexer.NO_QUOTE_CHAR, PhpTokenTypes.STRING_LITERAL, PhpStringLiteralLexer.TYPE_DOUBLE_QUOTE), PhpTokenTypes.STRING_LITERAL);
		registerLayer(new PhpStringLiteralLexer(PhpStringLiteralLexer.NO_QUOTE_CHAR, PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PhpStringLiteralLexer.TYPE_SINGLE_QUOTE), PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE);
	}
}