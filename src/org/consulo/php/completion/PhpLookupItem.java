package org.consulo.php.completion;

import java.awt.Color;

import javax.swing.Icon;

import org.consulo.php.lang.psi.PhpNamedElement;
import com.intellij.codeInsight.lookup.LookupValueWithPriority;
import com.intellij.codeInsight.lookup.LookupValueWithUIHint;
import com.intellij.codeInsight.lookup.PresentableLookupValue;
import com.intellij.openapi.util.Iconable;

/**
 * @author jay
 * @date Jun 24, 2008 1:51:06 PM
 */
public class PhpLookupItem implements PresentableLookupValue, LookupValueWithUIHint, LookupValueWithPriority, Iconable
{

	private PhpNamedElement element;
	private String name = "";
	private Icon icon = null;
	private boolean bold = false;
	private String type = "";

	public PhpLookupItem(PhpNamedElement element)
	{
		this.element = element;
	}

	@Override
	public String getPresentation()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getTypeHint()
	{
		return type;
	}

	public void setTypeHint(String typeHint)
	{
		type = typeHint;
	}

	@Override
	public Color getColorHint()
	{
		return null;
	}

	@Override
	public boolean isBold()
	{
		return bold;
	}

	public void setBold(boolean bold)
	{
		this.bold = bold;
	}

	@Override
	public Icon getIcon(int flags)
	{
		return icon;
	}

	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}

	public PhpNamedElement getLightElement()
	{
		return element;
	}

	@Override
	public int getPriority()
	{
		return HIGHER;
	}
}
