package org.consulo.php.completion;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.consulo.php.lang.psi.PhpNamedElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: Jan 21, 2009
 * Time: 1:48:46 AM
 */
public class PhpLookupElement extends LookupElement
{

	public InsertHandler handler;
	public String lookupString = "";
	public String typeText = "";
	public String tailText = "";
	public boolean bold = false;
	public Icon icon;
	public PhpNamedElement element;

	public PhpLookupElement(PhpNamedElement element)
	{
		this.element = element;
		lookupString = element.getName();
	}

	public PhpLookupElement(String lookupString, InsertHandler handler, String typeText, String tailText, boolean bold, Icon icon)
	{
		this.handler = handler;
		this.lookupString = lookupString;
		this.typeText = typeText;
		this.tailText = tailText;
		this.bold = bold;
		this.icon = icon;
	}

	public PhpLookupElement(String lookupString, String typeText, String tailText, boolean bold, Icon icon)
	{
		this(lookupString, new BasicInsertHandler(), typeText, tailText, bold, icon);
	}

	@Override
	@NotNull
	public String getLookupString()
	{
		return lookupString;
	}

	@SuppressWarnings({"unchecked"})
	public InsertHandler getInsertHandler()
	{
		return handler;
	}

	@NotNull
	protected LookupElementRenderer<? extends LookupElement> getRenderer()
	{
		return new LookupElementRenderer<LookupElement>()
		{
			@Override
			public void renderElement(LookupElement element, LookupElementPresentation presentation)
			{
				presentation.setItemText(lookupString);
				if(bold)
				{
					presentation.setItemTextBold(true);
				}
				if(!StringUtils.isEmpty(tailText))
				{
					presentation.setTailText(tailText);
				}
				if(!StringUtils.isEmpty(typeText))
				{
					presentation.setTypeText(typeText);
				}
				presentation.setIcon(icon);
			}
		};
	}
}
