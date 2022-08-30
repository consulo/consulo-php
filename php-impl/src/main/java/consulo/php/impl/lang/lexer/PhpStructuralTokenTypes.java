package consulo.php.impl.lang.lexer;

import consulo.language.ast.IElementType;
import consulo.php.lang.psi.PhpElementType;

/**
 * @author jay
 * @time 31.12.2007 3:48:05
 */
public interface PhpStructuralTokenTypes
{

	IElementType PHP_CODE = new PhpElementType("PHP_CODE");
	IElementType PHP_ECHO_CODE = new PhpElementType("PHP_ECHO_CODE");
	IElementType HTML_CODE = new PhpElementType("HTML_CODE");

}
