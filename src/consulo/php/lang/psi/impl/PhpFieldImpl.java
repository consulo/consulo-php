package consulo.php.lang.psi.impl;

import consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpModifierList;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.PhpVariableReference;
import consulo.php.lang.psi.impl.stub.PhpFieldStub;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date May 5, 2008 9:12:10 AM
 */
public class PhpFieldImpl extends PhpStubbedNamedElementImpl<PhpFieldStub> implements PhpField
{
	public PhpFieldImpl(ASTNode node)
	{
		super(node);
	}

	public PhpFieldImpl(@NotNull PhpFieldStub stub)
	{
		super(stub, PhpStubElements.FIELD);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
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
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final PhpVariableReference variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public PhpDocComment getDocComment()
	{
		final PsiElement parent = getParent();
		if(parent instanceof PhpElement)
		{
			final PhpElement element = ((PhpElement) parent).getPrevPsiSibling();
			if(element instanceof PhpDocComment)
			{
				return (PhpDocComment) element;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public PhpModifierList getModifierList()
	{
		return findChildByClass(PhpModifierList.class);
	}

	@Override
	public boolean hasModifier(@NotNull IElementType type)
	{
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(type);
	}

	@Override
	public boolean hasModifier(@NotNull TokenSet tokenSet)
	{
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(tokenSet);
	}

	@Override
	public boolean isConstant()
	{
		return findChildByType(PhpTokenTypes.kwCONST) != null;
	}
}
