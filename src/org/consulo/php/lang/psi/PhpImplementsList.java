package org.consulo.php.lang.psi;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface PhpImplementsList extends PhpElement
{

	@NotNull
	public List<PhpClass> getInterfaces();

}
