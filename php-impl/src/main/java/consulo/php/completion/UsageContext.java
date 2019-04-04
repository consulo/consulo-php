package consulo.php.completion;

import com.jetbrains.php.lang.psi.elements.PhpClass;

/**
 * @author jay
 * @date Jun 24, 2008 1:55:36 PM
 */
public class UsageContext
{
	private PhpClass classForAccessFilter = null;
	private PhpClass callingObjectClass = null;

	public UsageContext()
	{
		classForAccessFilter = null;
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
