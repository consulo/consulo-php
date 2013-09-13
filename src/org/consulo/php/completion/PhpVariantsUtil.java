package org.consulo.php.completion;

import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.RowIcon;
import org.consulo.php.completion.insert.PhpMethodInsertHandler;
import org.consulo.php.lang.psi.elements.*;
import org.consulo.php.util.PhpPresentationUtil;
import org.consulo.php.lang.psi.elements.PhpNamedElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 1:48:13 PM
 */
public class PhpVariantsUtil
{

	@SuppressWarnings({"unchecked"})
	public static List<LookupElement> getLookupItemsForClasses(Collection<? extends org.consulo.php.lang.psi.elements.PhpClass> classes, ClassUsageContext context)
	{
		/*List<PhpClass> filtered = new ArrayList<PhpClass>();
		for(PhpClass element : classes)
		{
			if(element instanceof PhpClass && !context.isInImplements())
			{
				PhpClass klass = (PhpClass) element;
				if(!klass.isAbstract() || context.isInInstanceof() || context.isInExtends())
				{
					if(context.isStatic() && klass.hasStaticMembers())
					{
						filtered.add(element);
					}
					else if(!context.isStatic())
					{
						filtered.add(element);
					}
				}
			}
			else if(element instanceof LightPhpInterface)
			{
				if(context.isInImplements() || context.isInInstanceof())
				{
					filtered.add(element);
				}
			}
		}       */

		final List<LookupElement> items = new ArrayList<LookupElement>();
		/*final List<String> names = new ArrayList<String>();
		for(PhpClass element : filtered)
		{
			if(!names.contains(element.getName()))
			{
				names.add(element.getName());
				final PhpLookupElement lookupItem = (PhpLookupElement) getLookupItem(element, null);
				if(context.isInNew())
				{
					lookupItem.handler = PhpClassConstructorInsertHandler.getInstance();
				}
				if(context.isStatic())
				{
					lookupItem.handler = PhpClassInsertHandler.getInstance();
				}
				items.add(lookupItem);
			}
		}      */
		return items;
	}

	public static List<LookupElement> getLookupItems(List<? extends PhpNamedElement> elements, UsageContext context)
	{
	/*	List<PhpNamedElement> filtered = new ArrayList<PhpNamedElement>();
		for(LightPhpElement element : elements)
		{
			if(element instanceof LightPhpElementWithModifier)
			{
				final PhpModifier modifier = ((LightPhpElementWithModifier) element).getModifier();
				if(element instanceof LightPhpMethod)
				{
					if(element.getParentOfType(LightPhpClass.class).getConstructor() == element)
					{
						continue;
					}
				}
				if(modifier.isStatic() == context.getModifier().isStatic())
				{
					if(modifier.isPrivate())
					{
						if(context.getClassForAccessFilter() == element.getParentOfType(LightPhpClass.class))
						{
							filtered.add(element);
						}
					}
					if(modifier.isProtected())
					{
						LightPhpClass myContextClass = context.getClassForAccessFilter();
						LightPhpClass elementClass = element.getParentOfType(LightPhpClass.class);
						while(myContextClass != null)
						{
							if(myContextClass == elementClass)
							{
								filtered.add(element);
								break;
							}
							myContextClass = myContextClass.getSuperClass();
						}
					}
					if(modifier.isPublic())
					{
						filtered.add(element);
					}
				}
			}
			else
			{
				filtered.add(element);
			}
		}
           */
		final List<LookupElement> items = new ArrayList<LookupElement>();
		/*for(LightPhpElement element : filtered)
		{
			items.add(getLookupItem(element, context));
		} */
		return items;
	}

	@SuppressWarnings({"unchecked"})
	public static LookupElement getLookupItem(PhpNamedElement element, UsageContext context)
	{
		PhpLookupElement item = new PhpLookupElement(element);
		item.icon = PhpPresentationUtil.getIcon(element);
		if(context != null)
		{
			final org.consulo.php.lang.psi.elements.PhpClass objectClass = context.getCallingObjectClass();
			if(PsiTreeUtil.getParentOfType(element, org.consulo.php.lang.psi.elements.PhpClass.class) == objectClass)
			{
				item.bold = true;
			}
		}
		if(element instanceof org.consulo.php.lang.psi.elements.PhpField)
		{
			item.typeText = ((org.consulo.php.lang.psi.elements.PhpField) element).getType().toString();
		}
		else if(element instanceof org.consulo.php.lang.psi.elements.PhpMethod)
		{
			item.typeText = ((org.consulo.php.lang.psi.elements.PhpMethod) element).getType().toString();
			item.tailText = "()";
		}
		/*else if(element instanceof LightPhpFunction)
		{
			item.tailText = "()";
		}         */
		item.handler = getInsertHandler(element);
		return item;
	}

	public static List<LookupItem> getLookupItemsForVariables(List<? extends org.consulo.php.lang.psi.elements.PHPPsiElement> elements)
	{
		List<LookupItem> result = new ArrayList<LookupItem>();
		for(org.consulo.php.lang.psi.elements.PHPPsiElement element : elements)
		{
			result.add(getLookupItemForVariable(element));
		}
		return result;
	}

	public static LookupItem getLookupItemForVariable(org.consulo.php.lang.psi.elements.PHPPsiElement element)
	{
		final PhpLookupItem item = new PhpLookupItem(null);
		if(element instanceof org.consulo.php.lang.psi.elements.PhpVariableReference)
		{
			org.consulo.php.lang.psi.elements.PhpVariableReference variable = (org.consulo.php.lang.psi.elements.PhpVariableReference) element;
			item.setName(variable.getName());
			final RowIcon icon = new RowIcon(2);
			icon.setIcon(variable.getIcon(), 0);
			item.setIcon(icon);
			final org.consulo.php.lang.psi.elements.PhpClass variableType = variable.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		else if(element instanceof org.consulo.php.lang.psi.elements.PhpParameter)
		{
			org.consulo.php.lang.psi.elements.PhpParameter parameter = (org.consulo.php.lang.psi.elements.PhpParameter) element;
			item.setName(parameter.getName());
			final RowIcon icon = new RowIcon(2);
			icon.setIcon(parameter.getIcon(), 0);
			item.setIcon(icon);
			final org.consulo.php.lang.psi.elements.PhpClass variableType = parameter.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		return new LookupItem<PhpLookupItem>(item, item.getPresentation());
	}

	public static InsertHandler getInsertHandler(PhpNamedElement element)
	{
		if(element instanceof org.consulo.php.lang.psi.elements.PhpMethod)
		{
			return PhpMethodInsertHandler.getInstance();
		}
	/*	if(element instanceof LightPhpFunction)
		{
			return PhpMethodInsertHandler.getInstance();
		}  */
		return new BasicInsertHandler();
	}

}
