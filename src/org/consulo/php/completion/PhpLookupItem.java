package org.consulo.php.completion;

import com.intellij.codeInsight.lookup.LookupValueWithPriority;
import com.intellij.codeInsight.lookup.LookupValueWithUIHint;
import com.intellij.codeInsight.lookup.PresentableLookupValue;
import com.intellij.openapi.util.Iconable;
import org.consulo.php.lang.psi.elements.PhpNamedElement;

import javax.swing.*;
import java.awt.*;

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

	public String getPresentation()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTypeHint()
	{
		return type;
	}

	public void setTypeHint(String typeHint)
	{
		type = typeHint;
	}

	public Color getColorHint()
	{
		return null;
	}

	public boolean isBold()
	{
		return bold;
	}

	public void setBold(boolean bold)
	{
		this.bold = bold;
	}

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

	public int getPriority()
	{
		return HIGHER;
	}
}
