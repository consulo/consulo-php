package net.jay.plugins.php.cache.psi;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: Jan 17, 2009
 * Time: 2:03:18 AM
 */
public class LightPhpFunction extends LightPhpElement
{

	private String typeString;

	public LightPhpFunction(LightPhpElement parent, String name)
	{
		super(parent, name);
	}

	public void accept(@NotNull LightPhpElementVisitor visitor)
	{
		visitor.visitFunction(this);
	}

	public String getTypeString()
	{
		return typeString;
	}

	public void setTypeString(String typeString)
	{
		this.typeString = typeString;
	}

	public boolean equals(Object obj)
	{
		if(obj instanceof LightPhpFunction)
		{
			return ((LightPhpFunction) obj).getName().equals(getName());
		}
		return super.equals(obj);
	}

	public String toString()
	{
		return getClass().getSimpleName() + ": " + getName();
	}

}
