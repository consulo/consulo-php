package net.jay.plugins.php.completion;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpElementWithModifier;
import net.jay.plugins.php.cache.psi.LightPhpField;
import net.jay.plugins.php.cache.psi.LightPhpFunction;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import net.jay.plugins.php.completion.insert.PhpClassConstructorInsertHandler;
import net.jay.plugins.php.completion.insert.PhpClassInsertHandler;
import net.jay.plugins.php.completion.insert.PhpMethodInsertHandler;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.Parameter;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.util.PhpPresentationUtil;

import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.ui.RowIcon;

/**
 * @author jay
 * @date Jun 24, 2008 1:48:13 PM
 */
public class PhpVariantsUtil
{

	@SuppressWarnings({"unchecked"})
	public static List<LookupElement> getLookupItemsForClasses(List<? extends LightPhpElement> classes, ClassUsageContext context)
	{
		List<LightPhpElement> filtered = new ArrayList<LightPhpElement>();
		for(LightPhpElement element : classes)
		{
			if(element instanceof LightPhpClass && !context.isInImplements())
			{
				LightPhpClass klass = (LightPhpClass) element;
				if(!klass.getModifier().isAbstract() || context.isInInstanceof() || context.isInExtends())
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
		}

		final List<LookupElement> items = new ArrayList<LookupElement>();
		final List<String> names = new ArrayList<String>();
		for(LightPhpElement element : filtered)
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
		}
		return items;
	}

	public static List<LookupElement> getLookupItems(List<? extends LightPhpElement> elements, UsageContext context)
	{
		List<LightPhpElement> filtered = new ArrayList<LightPhpElement>();
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

		final List<LookupElement> items = new ArrayList<LookupElement>();
		for(LightPhpElement element : filtered)
		{
			items.add(getLookupItem(element, context));
		}
		return items;
	}

	@SuppressWarnings({"unchecked"})
	public static LookupElement getLookupItem(LightPhpElement element, UsageContext context)
	{
		PhpLookupElement item = new PhpLookupElement(element);
		item.icon = PhpPresentationUtil.getIcon(element);
		if(context != null)
		{
			final LightPhpClass objectClass = context.getCallingObjectClass();
			if(element.getParentOfType(LightPhpClass.class) == objectClass)
			{
				item.bold = true;
			}
		}
		if(element instanceof LightPhpField)
		{
			item.typeText = ((LightPhpField) element).getType();
		}
		else if(element instanceof LightPhpMethod)
		{
			item.typeText = ((LightPhpMethod) element).getTypeString();
			item.tailText = "()";
		}
		else if(element instanceof LightPhpFunction)
		{
			item.tailText = "()";
		}
		item.handler = getInsertHandler(element);
		return item;
	}

	public static List<LookupItem> getLookupItemsForVariables(List<? extends PHPPsiElement> elements)
	{
		List<LookupItem> result = new ArrayList<LookupItem>();
		for(PHPPsiElement element : elements)
		{
			result.add(getLookupItemForVariable(element));
		}
		return result;
	}

	public static LookupItem getLookupItemForVariable(PHPPsiElement element)
	{
		final PhpLookupItem item = new PhpLookupItem(null);
		if(element instanceof Variable)
		{
			Variable variable = (Variable) element;
			item.setName(variable.getName());
			final RowIcon icon = new RowIcon(2);
			icon.setIcon(variable.getIcon(), 0);
			item.setIcon(icon);
			final LightPhpClass variableType = variable.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		else if(element instanceof Parameter)
		{
			Parameter parameter = (Parameter) element;
			item.setName(parameter.getName());
			final RowIcon icon = new RowIcon(2);
			icon.setIcon(parameter.getIcon(), 0);
			item.setIcon(icon);
			final LightPhpClass variableType = parameter.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		return new LookupItem<PhpLookupItem>(item, item.getPresentation());
	}

	public static InsertHandler getInsertHandler(LightPhpElement element)
	{
		if(element instanceof LightPhpMethod)
		{
			return PhpMethodInsertHandler.getInstance();
		}
		if(element instanceof LightPhpFunction)
		{
			return PhpMethodInsertHandler.getInstance();
		}
		return new BasicInsertHandler();
	}

}
