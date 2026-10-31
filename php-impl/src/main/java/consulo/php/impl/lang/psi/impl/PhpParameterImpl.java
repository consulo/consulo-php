package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.PhpLangUtil;

import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.util.lang.StringUtil;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import com.jetbrains.php.lang.parser.PhpElementTypes;
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

	@RequiredReadAction
	@Override
	public boolean isOptional()
	{
		PhpParameterStub stub = getStub();
		if(stub != null)
		{
			return stub.isOptional();
		}
		return getDefaultValue() != null;
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
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getParameterType(this);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getDeclaredType()
	{
		return PhpTypeInference.getDeclaredType(this);
	}

	@Nonnull
	@Override
	public PhpType getLocalType()
	{
		// what the parameter itself declares - its type and @param - before any global resolve
		return getType();
	}

	/**
	 * @return the expression after {@code =}
	 */
	@RequiredReadAction
	@Nullable
	@Override
	public PsiElement getDefaultValue()
	{
		ASTNode defaultValue = getNode().findChildByType(PhpElementTypes.PARAMETER_DEFAULT_VALUE);
		if(defaultValue == null)
		{
			return null;
		}
		for(PsiElement child = defaultValue.getPsi().getLastChild(); child != null; child = child.getPrevSibling())
		{
			if(child.getNode().getElementType() == PhpTokenTypes.opASGN)
			{
				return null;
			}
			if(!(child instanceof PsiWhiteSpace) && !(child instanceof PsiComment))
			{
				return child;
			}
		}
		return null;
	}

	@RequiredReadAction
	@Nullable
	@Override
	public String getDefaultValuePresentation()
	{
		PsiElement defaultValue = getDefaultValue();
		return defaultValue == null ? null : defaultValue.getText();
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

	/**
	 * Name without the {@code $}, like PhpStorm: {@code $foo} is {@code foo}
	 */
	@RequiredReadAction
	@Nonnull
	@Override
	public String getName()
	{
		PhpParameterStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? "" : StringUtil.trimStart(nameIdentifier.getText(), "$");
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return PhpLangUtil.GLOBAL_NAMESPACE_NAME;
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
