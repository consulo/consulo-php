package consulo.php.impl.lang.documentation.phpdoc.lexer;

import consulo.language.ast.TokenSet;
import consulo.language.lexer.MergingLexerAdapter;

/**
 * @author jay
 * @date Jun 26, 2008 10:47:50 PM
 */
public class PhpDocLexer extends MergingLexerAdapter
{

	public PhpDocLexer()
	{
		super(new PhpDocFlexLexer(), TokenSet.create(PhpDocTokenTypes.DOC_TEXT, PhpDocTokenTypes.DOC_WHITESPACE, PhpDocTokenTypes.DOC_IGNORED));
	}

}
