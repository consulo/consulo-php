package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamedStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpConstantReference;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubbedNamedElementImpl<T extends PhpNamedStub<?>> extends PhpStubbedElementImpl<T> implements PsiNameIdentifierOwner, PhpNamedElement
{
	public PhpStubbedNamedElementImpl(@Nonnull T stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpStubbedNamedElementImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	public PhpDocComment getDocComment()
	{
		PsiElement element = getPrevSibling();
		if(element instanceof PhpDocComment)
		{
			return (PhpDocComment) element;
		}
		return null;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getFQN()
	{
		String namespaceName = getNamespaceName();
		if(StringUtil.isEmpty(namespaceName))
		{
			return getName();
		}
		return namespaceName + "/" + getName();
	}


	@RequiredReadAction
	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier != null ? nameIdentifier.getTextOffset() : super.getTextOffset();
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean isInternal()
	{
		T stub = getStub();
		if(stub != null)
		{
			return stub.isInternal();
		}

		// TODO [VISTALL] is internal impl
		return false;
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public String getName()
	{
		T stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}

		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getText();
	}

	@Nonnull
	@Override
	public CharSequence getNameCS()
	{
		return getName();
	}

	@Nonnull
	public PhpType getType()
	{
		PhpType type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		if(type == null)
		{
			PhpTypeAnnotatorVisitor.process(this);
		}
		type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		assert type != null;
		return type;
	}

	@Override
	public PsiElement setName(@NonNls @Nonnull String s) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null && !getName().equals(s))
		{
			final PhpConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), s);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return null;
	}

	@RequiredReadAction
	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}
}

