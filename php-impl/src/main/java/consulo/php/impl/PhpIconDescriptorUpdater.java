package consulo.php.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.component.util.Iconable;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.php.impl.ide.projectView.PhpTreeStructureProvider;
import consulo.util.lang.BitUtil;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
@ExtensionImpl(order = "before default")
public class PhpIconDescriptorUpdater implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags)
	{
		if(element instanceof PhpFile)
		{
			PhpClass singleClass = PhpTreeStructureProvider.findSingleClass(element);
			if(singleClass != null)
			{
				IconDescriptorUpdaters.processExistingDescriptor(iconDescriptor, singleClass, flags);
			}
		}
		else if(element instanceof PhpClass)
		{
			PhpClass phpClass = (PhpClass) element;

			if(phpClass.isInterface())
			{
				iconDescriptor.setMainIcon(AllIcons.Nodes.Interface);
			}
			else if(phpClass.isTrait())
			{
				iconDescriptor.setMainIcon(AllIcons.Nodes.Trait);
			}
			else if(phpClass.getModifier().isAbstract())
			{
				iconDescriptor.setMainIcon(AllIcons.Nodes.AbstractClass);
			}
			else
			{
				iconDescriptor.setMainIcon(AllIcons.Nodes.Class);
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
		else if(element instanceof PhpDefine)
		{
			IconDescriptor descriptor = new IconDescriptor(AllIcons.Nodes.Field);
			descriptor.addLayerIcon(AllIcons.Nodes.StaticMark);
			descriptor.addLayerIcon(AllIcons.Nodes.FinalMark);
			if(BitUtil.isSet(flags, Iconable.ICON_FLAG_VISIBILITY))
			{
				descriptor.setRightIcon(AllIcons.Nodes.C_public);
			}

			iconDescriptor.setMainIcon(descriptor.toIcon());
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
