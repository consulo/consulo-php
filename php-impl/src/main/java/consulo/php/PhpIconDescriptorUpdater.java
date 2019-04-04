package consulo.php;

import javax.annotation.Nonnull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.util.BitUtil;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpElementWithModifier;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public class PhpIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags)
	{
		if(element instanceof PhpClass)
		{
			PhpClass phpClass = (PhpClass) element;

			if(phpClass.isInterface())
			{
				iconDescriptor.setMainIcon(PhpIcons.Interface);
			}
			else if(phpClass.isTrait())
			{
				iconDescriptor.setMainIcon(PhpIcons.Trait);
			}
			else if(phpClass.getModifier().isAbstract())
			{
				iconDescriptor.setMainIcon(PhpIcons.AbstractClass);
			}
			else
			{
				iconDescriptor.setMainIcon(PhpIcons.Class);
			}

			processModifierList(iconDescriptor, flags, (PhpElementWithModifier) element);
		}
		else if(element instanceof Variable)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Variable);
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
		}
		else if(element instanceof Parameter)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Parameter);
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
		}
		else if(element instanceof Function)
		{
			iconDescriptor.setMainIcon(((Function) element).getModifier().isAbstract() ? AllIcons.Nodes.AbstractMethod : AllIcons.Nodes.Function);

			processModifierList(iconDescriptor, flags, (PhpElementWithModifier) element);
		}
		else if(element instanceof Field)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Field);

			processModifierList(iconDescriptor, flags, (PhpElementWithModifier) element);
		}
	}

	private void processModifierList(IconDescriptor iconDescriptor, int flags, PhpElementWithModifier phpModifierListOwner)
	{
		PhpModifier modifier = phpModifierListOwner.getModifier();
		if(modifier.isFinal())
		{
			iconDescriptor.addLayerIcon(AllIcons.Nodes.FinalMark);
		}

		if(modifier.isStatic() && !(phpModifierListOwner instanceof PhpClass))
		{
			iconDescriptor.addLayerIcon(AllIcons.Nodes.StaticMark);
		}

		if(!BitUtil.isSet(flags, Iconable.ICON_FLAG_VISIBILITY))
		{
			return;
		}

		if(modifier.isPublic())
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_public);
		}
		else if(modifier.isProtected())
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_protected);
		}
		else if(modifier.isProtected())
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_private);
		}
		else
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
		}
	}
}
