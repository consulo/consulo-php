package consulo.php.impl.lang.psi.impl;

import consulo.php.impl.codeInsight.controlFlow.PhpControlFlowBuilder;
import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

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

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getReturnType(this);
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
		PhpNamespace namespace = PsiTreeUtil.getStubOrPsiParentOfType(this, PhpNamespace.class);
		String name = namespace == null ? null : namespace.getName();
		if(name == null)
		{
			return "";
		}
		return name.startsWith("\\") ? name.substring(1) : name;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpControlFlow getControlFlow()
	{
		return PhpControlFlowBuilder.getControlFlow(this);
	}
}
