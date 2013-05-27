package net.jay.plugins.php.lang.psi.elements;

import javax.swing.Icon;

import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 10:32:20 PM
 */
public interface Parameter extends PHPPsiElement, PhpNamedElement, PhpTypedElement
{

	public Icon getIcon();

}
