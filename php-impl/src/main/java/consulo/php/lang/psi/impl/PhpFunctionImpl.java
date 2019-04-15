package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 3, 2008 10:16:23 PM
 */
public class PhpFunctionImpl extends PhpStubbedNamedElementImpl<PhpFunctionStub> implements Function
{
	public PhpFunctionImpl(ASTNode node)
	{
		super(node);
	}

	public PhpFunctionImpl(@Nonnull PhpFunctionStub stub)
	{
		super(stub, PhpStubElements.FUNCTION);
	}

	@RequiredReadAction
	@Override
	@Nonnull
	public Parameter[] getParameters()
	{
		PhpFunctionStub stub = getStub();
		if(stub != null)
		{
			return stub.getChildrenByType(PhpStubElements.PARAMETER, Parameter.EMPTY_ARRAY);
		}

		ParameterList parameterList = getParameterList();
		if(parameterList == null)
		{
			return Parameter.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
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
		visitor.visitFunction(this);
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

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		return PhpModifier.PUBLIC_IMPLEMENTED_DYNAMIC;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}
}
