package net.jay.plugins.php.lang.lexer;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 31.12.2007 3:44:46
 */
public class PHPHTMLLexer extends LayeredLexer
{

	public PHPHTMLLexer()
	{
		super(new PHPHTMLMergingLexer());
		registerLayer(new PHPFlexAdapter(), new IElementType[]{PHPStructuralTokenTypes.PHP_CODE});
		registerLayer(new PHPFlexAdapter(), new IElementType[]{PHPStructuralTokenTypes.PHP_ECHO_CODE});
	}
}

