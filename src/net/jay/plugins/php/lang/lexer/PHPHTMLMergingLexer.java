package net.jay.plugins.php.lang.lexer;

import com.intellij.psi.tree.TokenSet;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.lexer.FlexAdapter;

/**
 * @author jay
 * @time 31.12.2007 3:46:53
 */
public class PHPHTMLMergingLexer extends MergingLexerAdapter {

	public PHPHTMLMergingLexer() {
		super(new FlexAdapter(new PHPStructuringFlexLexer()), TokenSet.create(PHPStructuralTokenTypes.HTML_CODE, PHPStructuralTokenTypes.PHP_CODE, PHPStructuralTokenTypes.PHP_ECHO_CODE));
	}
}
