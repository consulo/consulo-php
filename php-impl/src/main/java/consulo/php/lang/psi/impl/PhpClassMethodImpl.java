package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.stubs.PhpMethodStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		return null;
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
		return false;
	}

	@Override
	public boolean isFinal()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public PhpModifier.Access getAccess()
	{
		return null;
	}

	@Nullable
	@Override
	public PhpClass getContainingClass()
	{
		return null;
	}

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		return PhpModifier.PUBLIC_FINAL_STATIC;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}
}
