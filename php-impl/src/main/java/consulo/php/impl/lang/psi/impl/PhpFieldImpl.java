package consulo.php.impl.lang.psi.impl;

import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpPsiElementFactory;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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
			return nameIdentifier.getText();
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

	@Override
	public boolean isStatic()
	{
		PhpFieldStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isConstant();
		}

		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.STATIC_KEYWORD);
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
		PhpModifier.State state = getState();
		PhpFieldStub stub = getStub();
		if(stub != null)
		{
			return PhpModifier.instance(stub.getAccess(), stub.isFinal() ? PhpModifier.Abstractness.FINAL : PhpModifier.Abstractness.IMPLEMENTED, state);
		}

		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		boolean constant = isConstant();
		PhpModifier.Access access = modifierList == null ? PhpModifier.Access.PUBLIC : modifierList.getAccess();
		return PhpModifier.instance(access, constant ? PhpModifier.Abstractness.FINAL : PhpModifier.Abstractness.IMPLEMENTED, state);
	}

	@Nonnull
	private PhpModifier.State getState()
	{
		PhpFieldStub stub = getGreenStub();
		if(stub != null)
		{
			if(stub.isStatic() || stub.isConstant())
			{
				return PhpModifier.State.STATIC;
			}
			return PhpModifier.State.DYNAMIC;
		}

		if(isStatic() || isConstant())
		{
			return PhpModifier.State.STATIC;
		}
		return PhpModifier.State.DYNAMIC;
	}

	@Override
	public boolean isWriteAccess()
	{
		return false;
	}
}
