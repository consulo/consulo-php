package consulo.php.impl.lang.parser.util;

import consulo.language.ast.IElementType;

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
	public IElementType parse(PhpPsiBuilder builder);

}
