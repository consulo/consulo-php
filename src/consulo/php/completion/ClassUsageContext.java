package consulo.php.completion;

/**
 * @author jay
 * @date Jun 24, 2008 6:55:00 PM
 */
public class ClassUsageContext
{

	private boolean inInstanceof = false;
	private boolean inExtends = false;
	private boolean inImplements = false;
	private boolean inNew = false;
	private boolean isStatic = false;

	public boolean isInInstanceof()
	{
		return inInstanceof;
	}

	public void setInInstanceof(boolean inInstanceof)
	{
		this.inInstanceof = inInstanceof;
	}

	public boolean isInExtends()
	{
		return inExtends;
	}

	public void setInExtends(boolean inExtends)
	{
		this.inExtends = inExtends;
	}

	public boolean isInImplements()
	{
		return inImplements;
	}

	public void setInImplements(boolean inImplements)
	{
		this.inImplements = inImplements;
	}

	public boolean isInNew()
	{
		return inNew;
	}

	public void setInNew(boolean inNew)
	{
		this.inNew = inNew;
	}

	public boolean isStatic()
	{
		return isStatic;
	}

	public void setStatic(boolean isStatic)
	{
		this.isStatic = isStatic;
	}
}
