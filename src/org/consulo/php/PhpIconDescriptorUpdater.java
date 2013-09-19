package org.consulo.php;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.util.BitUtil;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpModifierListOwner;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public class PhpIconDescriptorUpdater implements IconDescriptorUpdater {
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags) {
		if (element instanceof PhpClass) {
			PhpClass phpClass = (PhpClass) element;

			if (phpClass.isInterface()) {
				iconDescriptor.setMainIcon(PhpIcons2.Interface);
			}
			else if (phpClass.isTrait()) {
				iconDescriptor.setMainIcon(PhpIcons2.Trait);
			}
			else if (phpClass.hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD)) {
				iconDescriptor.setMainIcon(PhpIcons2.AbstractClass);
			}
			else {
				iconDescriptor.setMainIcon(PhpIcons2.Class);
			}

			processModifierList(iconDescriptor, flags, (PhpModifierListOwner) element);
		}
		else if (element instanceof PhpFunction) {
			iconDescriptor.setMainIcon(((PhpFunction) element).hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD) ? AllIcons.Nodes.AbstractMethod : AllIcons.Nodes.Function);

			processModifierList(iconDescriptor, flags, (PhpModifierListOwner) element);
		}
		else if (element instanceof PhpField) {
			iconDescriptor.setMainIcon(AllIcons.Nodes.Field);

			processModifierList(iconDescriptor, flags, (PhpModifierListOwner) element);
		}
	}

	private void processModifierList(IconDescriptor iconDescriptor, int flags, PhpModifierListOwner phpModifierListOwner) {
		if(phpModifierListOwner.hasModifier(PhpTokenTypes.FINAL_KEYWORD)) {
			iconDescriptor.addLayerIcon(AllIcons.Nodes.FinalMark);
		}
		if(phpModifierListOwner.hasModifier(PhpTokenTypes.STATIC_KEYWORD)) {
			iconDescriptor.addLayerIcon(AllIcons.Nodes.StaticMark);
		}

		if (!BitUtil.isSet(flags, Iconable.ICON_FLAG_VISIBILITY)) {
			return;
		}

		if (phpModifierListOwner.hasModifier(PhpTokenTypes.PUBLIC_KEYWORD)) {
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_public);
		}
		else if (phpModifierListOwner.hasModifier(PhpTokenTypes.PROTECTED_KEYWORD)) {
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_protected);
		}
		else if (phpModifierListOwner.hasModifier(PhpTokenTypes.PROTECTED_KEYWORD)) {
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_private);
		}
		else {
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
		}
	}
}
