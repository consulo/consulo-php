package net.jay.plugins.php.cache.psi;

import net.jay.plugins.php.lang.psi.elements.PhpModifier;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 25, 2008 9:03:52 PM
 */
public class LightPhpField extends LightPhpElement implements LightPhpElementWithModifier
{

	private PhpModifier modifier;
	private String type = "";

	public LightPhpField(LightPhpElement parent, String name)
	{
		super(parent, name);
	}

	public void accept(@NotNull LightPhpElementVisitor visitor)
	{
		visitor.visitField(this);
	}

	public void setModifier(PhpModifier modifier)
	{
		this.modifier = modifier;
	}

	public PhpModifier getModifier()
	{
		return modifier;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
