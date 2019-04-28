package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpDefineImpl extends PhpStubbedNamedElementImpl<PhpConstantStub> implements PhpDefine
{
	public PhpDefineImpl(@Nonnull PhpConstantStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpDefineImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public String getName()
	{
		PhpConstantStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}

		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier == null)
		{
			return "";
		}
		return StringUtil.unquoteString(nameIdentifier.getText());
	}

	@RequiredReadAction
	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		PhpFunctionReferenceImpl reference = findNotNullChildByClass(PhpFunctionReferenceImpl.class);

		ParameterList parameterList = reference.getParameterList();
		if(parameterList == null)
		{
			return null;
		}

		PsiElement[] parameters = parameterList.getParameters();
		if(parameters.length == 0)
		{
			return null;
		}

		PsiElement parameter = parameters[0];
		if(!(parameter instanceof PhpStringLiteralExpressionImpl))
		{
			return null;
		}
		return parameter.getFirstChild();
	}

	@Nullable
	@Override
	public PsiElement getValue()
	{
		return null;
	}

	@Nullable
	@Override
	public String getValuePresentation()
	{
		return null;
	}

	@Override
	public boolean isCaseSensitive()
	{
		return false;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}

	@Override
	public boolean isWriteAccess()
	{
		return false;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{

	}
}
