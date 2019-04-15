package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamedStub;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;

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

	@Override
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

	@Override
	@Nonnull
	public PhpType getType()
	{
		return PhpType.EMPTY;
	}

	@RequiredWriteAction
	@Override
	public PsiElement setName(@NonNls @Nonnull String s) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null && !getName().equals(s))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), s);
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

