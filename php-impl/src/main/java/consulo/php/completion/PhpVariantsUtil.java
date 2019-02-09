package consulo.php.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.psi.util.PsiTreeUtil;
import consulo.awt.TargetAWT;
import consulo.ide.IconDescriptorUpdaters;
import consulo.php.completion.insert.PhpMethodInsertHandler;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpNamedElement;
import consulo.php.lang.psi.PhpParameter;
import consulo.php.lang.psi.PhpVariableReference;

/**
 * @author jay
 * @date Jun 24, 2008 1:48:13 PM
 */
public class PhpVariantsUtil
{

	@SuppressWarnings({"unchecked"})
	public static List<LookupElement> getLookupItemsForClasses(Collection<? extends PhpClass> classes, ClassUsageContext context)
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
		item.icon = TargetAWT.to(IconDescriptorUpdaters.getIcon(element, 0));
		if(context != null)
		{
			final PhpClass objectClass = context.getCallingObjectClass();
			if(PsiTreeUtil.getParentOfType(element, PhpClass.class) == objectClass)
			{
				item.bold = true;
			}
		}
		if(element instanceof PhpField)
		{
			item.typeText = ((PhpField) element).getType().toString();
		}
		else if(element instanceof PhpFunction)
		{
			item.typeText = ((PhpFunction) element).getType().toString();
			item.tailText = "()";
		}
		/*else if(element instanceof LightPhpFunction)
		{
			item.tailText = "()";
		}         */
		item.handler = getInsertHandler(element);
		return item;
	}

	@Nonnull
	public static LookupItem[] getLookupItemsForVariables(List<? extends PhpElement> elements)
	{
		LookupItem[] result = new LookupItem[elements.size()];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = getLookupItemForVariable(elements.get(i));
		}
		return result;
	}

	public static LookupItem getLookupItemForVariable(PhpElement element)
	{
		final PhpLookupItem item = new PhpLookupItem(null);
		if(element instanceof PhpVariableReference)
		{
			PhpVariableReference variable = (PhpVariableReference) element;
			item.setName(variable.getName());

			item.setIcon(IconDescriptorUpdaters.getIcon(element, 0));
			final PhpClass variableType = variable.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		else if(element instanceof PhpParameter)
		{
			PhpParameter parameter = (PhpParameter) element;
			item.setName(parameter.getName());
			item.setIcon(IconDescriptorUpdaters.getIcon(element, 0));
			final PhpClass variableType = parameter.getType().getType();
			if(variableType != null)
			{
				item.setTypeHint(variableType.getName());
			}
		}
		return new LookupItem<>(item, item.getPresentation());
	}

	public static InsertHandler getInsertHandler(PhpNamedElement element)
	{
		if(element instanceof PhpFunction)
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
