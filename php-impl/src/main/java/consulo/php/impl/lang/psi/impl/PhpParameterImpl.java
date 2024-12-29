package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.util.IncorrectOperationException;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpPsiElementFactory;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

	@RequiredReadAction
	@Override
	public boolean isVariadic()
	{
		PhpParameterStub stub = getStub();
		if(stub != null)
		{
			return stub.isVariadic();
		}
		return findChildByType(PhpTokenTypes.ELLIPSIS) != null;
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		PhpParameterStub stub = getStub();
		if(stub != null)
		{
			// TODO [VISTALL] implement me. don't call AST inside completion
			return PhpType.EMPTY;
		}

		ClassReference reference = findChildByClass(ClassReference.class);
		if(reference != null)
		{
			return reference.resolveLocalType();
		}
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	public PhpType getDeclaredType()
	{
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	public PhpType getLocalType()
	{
		return PhpType.EMPTY;
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

	@RequiredReadAction
	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
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
