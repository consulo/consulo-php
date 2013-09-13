package org.consulo.php.lang.psi.elements;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface ImplementsList extends PhpElement
{

	@NotNull
	public List<PhpClass> getInterfaces();

}
