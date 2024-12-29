package consulo.php.impl.completion;

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.application.progress.ProgressManager;
import consulo.component.util.Iconable;
import consulo.util.lang.StringUtil;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.php.impl.completion.insert.PhpClassConstructorInsertHandler;
import consulo.php.impl.completion.insert.PhpMethodInsertHandler;

import jakarta.annotation.Nonnull;
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

	@Nonnull
	@RequiredReadAction
	public static LookupElement getLookupItem(PhpNamedElement element, UsageContext context)
	{
		return getLookupItem(element, context, PhpNamingPolicy.NOTHING);
	}

	@Nonnull
	@RequiredReadAction
	public static LookupElement getLookupItem(PhpNamedElement element, UsageContext context, @Nonnull PhpNamingPolicy namingPolicy)
	{
		LookupElementBuilder builder = LookupElementBuilder.create(element, String.valueOf(namingPolicy.getName(element)));
		builder = builder.withIcon(IconDescriptorUpdaters.getIcon(element, Iconable.ICON_FLAG_VISIBILITY));

		if(context != null)
		{
			final PhpClass objectClass = context.getCallingObjectClass();
			if(PsiTreeUtil.getParentOfType(element, PhpClass.class) == objectClass)
			{
				builder = builder.withBoldness(true);
			}
		}

		if(element instanceof PhpDefine)
		{
			String valuePresentation = ((PhpDefine) element).getValuePresentation();
			if(!StringUtil.isEmpty(valuePresentation))
			{
				builder = builder.withTailText(" = " + valuePresentation, true);
			}
		}
		else if(element instanceof Field)
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
			Parameter[] parameters = ((Function) element).getParameters();

			String parametersText = StringUtil.join(parameters, parameter ->
			{
				PhpType type = parameter.getType();
				if(type != PhpType.EMPTY)
				{
					return type.toString() + " " + parameter.getName();
				}
				return parameter.getName();
			}, ", ");
			builder = builder.withTailText("(" + parametersText + ")");
			builder = builder.withInsertHandler(PhpMethodInsertHandler.getInstance());
		}
		else if(element instanceof Variable)
		{
			Variable variable = (Variable) element;
			PhpType type = variable.getType();
			if(type != PhpType.EMPTY)
			{
				builder = builder.withTypeText(type.toString());
			}
			else
			{
				PhpType inferredType = variable.getInferredType();
				builder = builder.withTypeText(inferredType.toString());
			}
		}
		else if(element instanceof Parameter)
		{
			Parameter parameter = (Parameter) element;
			builder = builder.withTypeText(parameter.getType().toString());
		}
		return builder;
	}

	@Nonnull
	@RequiredReadAction
	public static LookupElement[] getLookupItems(Collection<? extends PhpNamedElement> elements, UsageContext usageContext)
	{
		LookupElement[] result = new LookupElement[elements.size()];
		int i = 0;
		for(PhpNamedElement element : elements)
		{
			ProgressManager.checkCanceled();
			result[i++] = getLookupItem(element, usageContext);
		}
		return result;
	}
}
