package org.consulo.php.lang.lexer;

import org.consulo.php.lang.psi.PhpElementType;

import com.intellij.psi.tree.IElementType;

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
