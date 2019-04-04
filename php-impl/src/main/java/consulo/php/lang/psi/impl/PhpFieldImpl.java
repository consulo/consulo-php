package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 5, 2008 9:12:10 AM
 */
public class PhpFieldImpl extends PhpStubbedNamedElementImpl<PhpFieldStub> implements Field
{
	public PhpFieldImpl(ASTNode node)
	{
		super(node);
	}

	public PhpFieldImpl(@Nonnull PhpFieldStub stub)
	{
		super(stub, PhpStubElements.CLASS_FIELD);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitField(this);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return isConstant() ? findChildByType(PhpTokenTypes.IDENTIFIER) : findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Override
	public String getName()
	{
		PhpFieldStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
		{
			return isConstant() ? nameIdentifier.getText() : nameIdentifier.getText().substring(1);
		}
		return null;
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
	public PhpDocComment getDocComment()
	{
		final PsiElement parent = getParent();
		if(parent instanceof PhpPsiElement)
		{
			final PhpPsiElement element = ((PhpPsiElement) parent).getPrevPsiSibling();
			if(element instanceof PhpDocComment)
			{
				return (PhpDocComment) element;
			}
		}
		return null;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}

	@Override
	public boolean isConstant()
	{
		return findChildByType(PhpTokenTypes.kwCONST) != null;
	}

	@Nullable
	@Override
	public PhpClass getContainingClass()
	{
		return null;
	}

	@Override
	public PhpModifier getModifier()
	{
		return PhpModifier.PUBLIC_FINAL_STATIC;
	}
}
