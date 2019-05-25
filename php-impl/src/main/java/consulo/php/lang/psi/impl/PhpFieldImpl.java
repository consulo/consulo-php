package consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	@RequiredReadAction
	@Override
	public PsiElement getNameIdentifier()
	{
		return isConstant() ? findChildByType(PhpTokenTypes.IDENTIFIER) : findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Nonnull
	@RequiredReadAction
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
		return "";
	}

	@RequiredReadAction
	@Override
	public boolean isConstant()
	{
		PhpFieldStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isConstant();
		}
		return findChildByType(PhpTokenTypes.kwCONST) != null;
	}

	@Nullable
	@Override
	public PhpClass getContainingClass()
	{
		return getStubOrPsiParentOfType(PhpClass.class);
	}

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		PhpFieldStub stub = getStub();
		if(stub != null)
		{
			boolean constant = stub.isConstant();
			return PhpModifier.instance(stub.getAccess(), stub.isFinal() ? PhpModifier.Abstractness.FINAL : PhpModifier.Abstractness.IMPLEMENTED, constant ? PhpModifier.State.STATIC : PhpModifier
					.State.DYNAMIC);
		}

		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		boolean constant = isConstant();
		PhpModifier.Access access = modifierList == null ? PhpModifier.Access.PUBLIC : modifierList.getAccess();
		return PhpModifier.instance(access, constant ? PhpModifier.Abstractness.FINAL : PhpModifier.Abstractness.IMPLEMENTED, constant ? PhpModifier.State.STATIC : PhpModifier.State.DYNAMIC);
	}
}
