package consulo.php.lang.psi.impl;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.util.IncorrectOperationException;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpConstantReference;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.resolve.types.PhpType;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubbedNamedElementImpl<T extends NamedStub<?>> extends PhpStubbedElementImpl<T> implements PsiNameIdentifierOwner, PsiNamedElement
{
	public PhpStubbedNamedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpStubbedNamedElementImpl(@NotNull ASTNode node)
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

	@NotNull
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
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null && !getName().equals(s))
		{
			final PhpConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), s);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}
}

