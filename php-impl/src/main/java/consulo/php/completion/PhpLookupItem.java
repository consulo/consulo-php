package consulo.php.completion;

import java.awt.Color;

import com.intellij.codeInsight.lookup.LookupValueWithPriority;
import com.intellij.codeInsight.lookup.LookupValueWithUIHint;
import com.intellij.codeInsight.lookup.PresentableLookupValue;
import com.intellij.openapi.util.Iconable;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotations.DeprecationInfo;
import consulo.ui.image.Image;

/**
 * @author jay
 * @date Jun 24, 2008 1:51:06 PM
 */
@Deprecated
@DeprecationInfo("Use LookupElementBuilder")
public class PhpLookupItem implements PresentableLookupValue, LookupValueWithUIHint, LookupValueWithPriority, Iconable
{
	private PhpNamedElement element;
	private String name = "";
	private Image icon = null;
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
	public Image getIcon(int flags)
	{
		return icon;
	}

	public void setIcon(Image icon)
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
