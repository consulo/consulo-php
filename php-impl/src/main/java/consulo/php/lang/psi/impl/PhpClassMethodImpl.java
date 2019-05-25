package consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.stubs.PhpMethodStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-03-30
 */
public class PhpClassMethodImpl extends PhpStubbedNamedElementImpl<PhpMethodStub> implements Method
{
	public PhpClassMethodImpl(ASTNode node)
	{
		super(node);
	}

	public PhpClassMethodImpl(@Nonnull PhpMethodStub stub)
	{
		super(stub, PhpStubElements.CLASS_METHOD);
	}

	@RequiredReadAction
	@Override
	@Nonnull
	public Parameter[] getParameters()
	{
		ParameterList parameterList = getParameterList();
		if(parameterList == null)
		{
			return Parameter.EMPTY_ARRAY;
		}
		return (Parameter[]) parameterList.getParameters();
	}

	@Override
	public ParameterList getParameterList()
	{
		return PsiTreeUtil.getChildOfType(this, ParameterList.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitMethod(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState resolveState, PsiElement psiElement, @Nonnull PsiElement psiElement1)
	{
		for(Parameter parameter : getParameters())
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
	public MethodType getMethodType(boolean allowAmbiguity)
	{
		return null;
	}

	@Override
	public boolean isStatic()
	{
		PhpMethodStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isStatic();
		}
		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.STATIC_KEYWORD);
	}

	@Override
	public boolean isFinal()
	{
		PhpMethodStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isFinal();
		}
		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.FINAL_KEYWORD);
	}

	@Override
	public boolean isAbstract()
	{
		PhpMethodStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isAbstract();
		}
		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD);
	}

	@Override
	public PhpModifier.Access getAccess()
	{
		PhpMethodStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.getAccess();
		}
		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		if(modifierList == null)
		{
			return PhpModifier.Access.PUBLIC;
		}

		return modifierList.getAccess();
	}

	@Nullable
	@Override
	public PhpClass getContainingClass()
	{
		return getStubOrPsiParentOfType(PhpClass.class);
	}

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		PhpModifier.Abstractness abstractness = PhpModifier.Abstractness.IMPLEMENTED;
		if(isAbstract())
		{
			abstractness = PhpModifier.Abstractness.ABSTRACT;
		}
		else if(isFinal())
		{
			abstractness = PhpModifier.Abstractness.FINAL;
		}

		PhpModifier.State state = PhpModifier.State.DYNAMIC;
		if(isStatic())
		{
			state = PhpModifier.State.STATIC;
		}
		return PhpModifier.instance(getAccess(), abstractness, state);
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
	}
}
