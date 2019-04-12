package consulo.php.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptorUpdaters;
import consulo.php.completion.insert.PhpClassConstructorInsertHandler;
import consulo.php.completion.insert.PhpMethodInsertHandler;

/**
 * @author jay
 * @date Jun 24, 2008 1:48:13 PM
 */
public class PhpVariantsUtil
{

	@SuppressWarnings({"unchecked"})
	public static List<LookupElement> getLookupItemsForClasses(Collection<? extends PhpClass> classes, ClassUsageContext context)
	{
		List<PhpClass> filtered = new ArrayList<>();
		for(PhpClass element : classes)
		{
			if(!context.isInImplements())
			{
				PhpClass klass = element;
				if(!klass.isAbstract() || context.isInInstanceof() || context.isInExtends())
				{
					filtered.add(element);
				}
			}
			/*else if(element instanceof LightPhpInterface)
			{
				if(context.isInImplements() || context.isInInstanceof())
				{
					filtered.add(element);
				}
			}*/
		}

		final List<LookupElement> items = new ArrayList<>();
		final List<String> names = new ArrayList<>();
		for(PhpClass element : filtered)
		{
			if(!names.contains(element.getName()))
			{
				names.add(element.getName());
				LookupElementBuilder lookupItem = (LookupElementBuilder) getLookupItem(element, null);
				if(context.isInNew())
				{
					lookupItem = lookupItem.withInsertHandler(PhpClassConstructorInsertHandler.getInstance());
				}
				if(context.isStatic())
				{
					//lookupItem.handler = PhpClassInsertHandler.getInstance();
				}
				items.add(lookupItem);
			}
		}
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

	@Nonnull
	@RequiredReadAction
	public static LookupElement getLookupItem(PhpNamedElement element, UsageContext context)
	{
		LookupElementBuilder builder = LookupElementBuilder.create(element);
		builder = builder.withIcon(IconDescriptorUpdaters.getIcon(element, Iconable.ICON_FLAG_VISIBILITY));

		if(context != null)
		{
			final PhpClass objectClass = context.getCallingObjectClass();
			if(PsiTreeUtil.getParentOfType(element, PhpClass.class) == objectClass)
			{
				builder = builder.withBoldness(true);
			}
		}
		if(element instanceof Field)
		{
			builder = builder.withTypeText(element.getType().toString());
		}
		else if(element instanceof PhpClass)
		{
			String namespaceName = element.getNamespaceName();
			if(!StringUtil.isEmpty(namespaceName))
			{
				builder = builder.withTailText(" (" + namespaceName + ")", true);
			}
		}
		else if(element instanceof Function)
		{
			builder = builder.withTypeText(element.getType().toString());
			builder = builder.withTailText("()");
		}
		builder = builder.withInsertHandler(getInsertHandler(element));
		return builder;
	}

	@Nonnull
	public static LookupItem[] getLookupItemsForVariables(List<? extends PhpPsiElement> elements)
	{
		LookupItem[] result = new LookupItem[elements.size()];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = getLookupItemForVariable(elements.get(i));
		}
		return result;
	}

	public static LookupItem getLookupItemForVariable(PhpPsiElement element)
	{
		final PhpLookupItem item = new PhpLookupItem(null);
		if(element instanceof Variable)
		{
			Variable variable = (Variable) element;
			item.setName(variable.getName());

			item.setIcon(IconDescriptorUpdaters.getIcon(element, 0));
			item.setTypeHint(variable.getType().toString());
		}
		else if(element instanceof Parameter)
		{
			Parameter parameter = (Parameter) element;
			item.setName(parameter.getName());
			item.setIcon(IconDescriptorUpdaters.getIcon(element, 0));
			item.setTypeHint(parameter.getType().toString());
		}
		return new LookupItem<>(item, item.getPresentation());
	}

	public static InsertHandler getInsertHandler(PhpNamedElement element)
	{
		if(element instanceof Function)
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
