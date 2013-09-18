package org.consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface PhpImplementsList extends PhpElement
{

	@NotNull
	public List<PhpClass> getInterfaces();

}
