package consulo.php.lang.highlighter;

import consulo.php.PhpLanguageLevel;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import consulo.php.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpStringLiteralLexer;
import consulo.php.lang.lexer.PhpTokenTypes;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;

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
		super(new FlexAdapter(new PhpFlexLexer(true, languageLevel)));
		LayeredLexer docLexer = new LayeredLexer(new PhpDocLexer());
		registerSelfStoppingLayer(docLexer, new IElementType[]{PhpTokenTypes.DOC_COMMENT}, new IElementType[]{PhpDocTokenTypes.DOC_COMMENT_END});

		//Lexer lexer = getHtmlHighlightingLexer();
		//docLexer.registerLayer(lexer, PhpTokenTypes.DOC_COMMENT);
		// @todo do it!
		registerLayer(new PhpStringLiteralLexer(PhpStringLiteralLexer.NO_QUOTE_CHAR, PhpTokenTypes.STRING_LITERAL, PhpStringLiteralLexer.TYPE_DOUBLE_QUOTE), new IElementType[]{PhpTokenTypes.STRING_LITERAL});
		registerLayer(new PhpStringLiteralLexer(PhpStringLiteralLexer.NO_QUOTE_CHAR, PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE, PhpStringLiteralLexer.TYPE_SINGLE_QUOTE), new IElementType[]{PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE});
	}
}