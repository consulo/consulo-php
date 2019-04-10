package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 3, 2008 10:32:38 PM
 */
public class PhpParameterImpl extends PhpStubbedNamedElementImpl<PhpParameterStub> implements Parameter
{
	public PhpParameterImpl(ASTNode node)
	{
		super(node);
	}

	public PhpParameterImpl(@Nonnull PhpParameterStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isOptional()
	{
		return false;
	}

	@Override
	public boolean isVariadic()
	{
		return false;
	}

	@Nonnull
	@Override
	public PhpType getDeclaredType()
	{
		return null;
	}

	@Nonnull
	@Override
	public PhpType getLocalType()
	{
		return PhpType.VOID;
	}

	@Nullable
	@Override
	public PsiElement getDefaultValue()
	{
		return null;
	}

	@Nullable
	@Override
	public String getDefaultValuePresentation()
	{
		return null;
	}

	@Override
	public boolean isPassByRef()
	{
		return false;
	}

	@Override
	public String getName()
	{
		PhpParameterStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}

		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
		{
			return nameNode.getText().substring(1);
		}
		return null;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
	}

	@Override
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final Variable variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitParameter(this);
	}

	@Override
	public boolean isWriteAccess()
	{
		return false;
	}
}
