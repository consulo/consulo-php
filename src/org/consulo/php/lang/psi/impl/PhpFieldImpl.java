package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date May 5, 2008 9:12:10 AM
 */
public class PhpFieldImpl extends PhpNamedElementImpl implements PhpField
{
	public PhpFieldImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitField(this);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
			return nameIdentifier.getText().substring(1);
		return null;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final PhpVariableReference variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public PhpDocComment getDocComment()
	{
		final PsiElement parent = getParent();
		if(parent instanceof PhpElement)
		{
			final PhpElement element = ((PhpElement) parent).getPrevPsiSibling();
			if(element instanceof PhpDocComment)
			{
				return (PhpDocComment) element;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public PhpModifierList getModifierList() {
		return findChildByClass(PhpModifierList.class);
	}

	@Override
	public boolean hasModifier(@NotNull IElementType type) {
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(type);
	}

	@Override
	public boolean hasModifier(@NotNull TokenSet tokenSet) {
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(tokenSet);
	}
}
