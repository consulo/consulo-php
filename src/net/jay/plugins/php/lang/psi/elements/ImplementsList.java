package net.jay.plugins.php.lang.psi.elements;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface ImplementsList extends PHPPsiElement
{

	@NotNull
	public List<PhpInterface> getInterfaces();

}
