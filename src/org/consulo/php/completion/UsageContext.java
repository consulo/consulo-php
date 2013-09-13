package org.consulo.php.completion;

import org.consulo.php.lang.psi.elements.PhpClass;
import org.consulo.php.lang.psi.elements.PhpModifier;

/**
 * @author jay
 * @date Jun 24, 2008 1:55:36 PM
 */
public class UsageContext
{

	private PhpModifier modifier = null;
	private PhpClass classForAccessFilter = null;
	private PhpClass callingObjectClass = null;

	public UsageContext()
	{
		modifier = null;
		classForAccessFilter = null;
	}

	public UsageContext(PhpModifier modifier, PhpClass classForAccessFilter)
	{
		this.modifier = modifier;
		this.classForAccessFilter = classForAccessFilter;
	}

	public PhpModifier getModifier()
	{
		return modifier;
	}

	public void setModifier(PhpModifier modifier)
	{
		this.modifier = modifier;
	}

	public PhpClass getClassForAccessFilter()
	{
		return classForAccessFilter;
	}

	public void setClassForAccessFilter(PhpClass klass)
	{
		this.classForAccessFilter = klass;
	}

	public PhpClass getCallingObjectClass()
	{
		return callingObjectClass;
	}

	public void setCallingObjectClass(PhpClass callingObjectClass)
	{
		this.callingObjectClass = callingObjectClass;
	}
}
