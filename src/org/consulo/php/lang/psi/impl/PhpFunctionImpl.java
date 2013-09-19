package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpModifierList;
import org.consulo.php.lang.psi.PhpParameter;
import org.consulo.php.lang.psi.PhpParameterList;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Apr 3, 2008 10:16:23 PM
 */
public class PhpFunctionImpl extends PhpNamedElementImpl implements PhpFunction
{
	public PhpFunctionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@NotNull
	public PhpParameter[] getParameters()
	{
		PhpParameterList parameterList = getParameterList();
		if(parameterList == null) {
			return PhpParameter.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
	}

	@Override
	public PhpParameterList getParameterList()
	{
		return PsiTreeUtil.getChildOfType(this, PhpParameterList.class);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitFunction(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement psiElement, @NotNull PsiElement psiElement1)
	{
		for(PhpParameter parameter : getParameters())
		{
			if(!processor.execute(parameter, resolveState))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, resolveState, psiElement, psiElement1);
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
