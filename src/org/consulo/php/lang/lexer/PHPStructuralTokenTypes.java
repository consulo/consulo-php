package org.consulo.php.lang.lexer;

import org.consulo.php.lang.psi.PHPElementType;

import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 31.12.2007 3:48:05
 */
public interface PHPStructuralTokenTypes
{

	IElementType PHP_CODE = new PHPElementType("PHP_CODE");
	IElementType PHP_ECHO_CODE = new PHPElementType("PHP_ECHO_CODE");
	IElementType HTML_CODE = new PHPElementType("HTML_CODE");

}
