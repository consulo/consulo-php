package org.consulo.php.lang.parser.util;

import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 */
public interface ParserPart
{

	/**
	 * Parses some part of BNF tree
	 *
	 * @param builder standard builder to mark AST nodes
	 * @return type of expression that was parsed
	 */
	public IElementType parse(PHPPsiBuilder builder);

}
