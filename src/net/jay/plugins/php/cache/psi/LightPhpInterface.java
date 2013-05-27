package net.jay.plugins.php.cache.psi;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 6, 2008 1:34:13 PM
 */
public class LightPhpInterface extends LightPhpElement
{
	private String name;

	public LightPhpInterface(LightPhpElement parent, String name)
	{
		super(parent);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void accept(@NotNull LightPhpElementVisitor visitor)
	{
		visitor.visitInterface(this);
	}

	public List<LightPhpMethod> getMethods()
	{
		return getChildrenOfType(LightPhpMethod.class);
	}
}
