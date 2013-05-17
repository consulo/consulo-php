package net.jay.plugins.php.lang.parser.util;

import com.intellij.psi.tree.IElementType;
import com.intellij.lang.PsiBuilder;

/**
 * @author jay
 */
public interface ParserPart {

	/**
	 * Parses some part of BNF tree
	 *
	 * @param builder standard builder to mark AST nodes
	 * @return type of expression that was parsed
	 */
	public IElementType parse(PHPPsiBuilder builder);

}
