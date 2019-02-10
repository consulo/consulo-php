package consulo.php.completion;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import consulo.awt.TargetAWT;
import consulo.php.lang.psi.PhpNamedElement;
import consulo.ui.image.Image;

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
	public Image icon;
	public PhpNamedElement element;

	public PhpLookupElement(PhpNamedElement element)
	{
		this.element = element;
		lookupString = element.getName();
	}

	public PhpLookupElement(String lookupString, InsertHandler handler, String typeText, String tailText, boolean bold, Image icon)
	{
		this.handler = handler;
		this.lookupString = lookupString;
		this.typeText = typeText;
		this.tailText = tailText;
		this.bold = bold;
		this.icon = icon;
	}

	public PhpLookupElement(String lookupString, String typeText, String tailText, boolean bold, Image icon)
	{
		this(lookupString, new BasicInsertHandler(), typeText, tailText, bold, icon);
	}

	@Override
	@Nonnull
	public String getLookupString()
	{
		return lookupString;
	}

	@SuppressWarnings({"unchecked"})
	public InsertHandler getInsertHandler()
	{
		return handler;
	}

	@Nonnull
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
				if(!StringUtil.isEmpty(tailText))
				{
					presentation.setTailText(tailText);
				}
				if(!StringUtil.isEmpty(typeText))
				{
					presentation.setTypeText(typeText);
				}
				presentation.setIcon(TargetAWT.to(icon));
			}
		};
	}
}
