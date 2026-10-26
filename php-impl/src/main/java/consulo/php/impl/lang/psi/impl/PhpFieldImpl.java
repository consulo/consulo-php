package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.PhpTypeDeclaration;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import com.jetbrains.php.lang.psi.elements.PhpAttributesList;
import com.jetbrains.php.lang.psi.elements.PhpAttribute;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
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
		return getParent().getNode().getElementType() == PhpElementTypes.CLASS_CONSTANTS;
	}

	/**
	 * {@code CLASS_FIELDS} or {@code CLASS_CONSTANTS} holding modifiers, type, attributes and doc comment of this field
	 */
	@RequiredReadAction
	private PsiElement getListElement()
	{
		return getParent();
	}

	@Nullable
	@RequiredReadAction
	private PhpModifierList getModifierList()
	{
		return PsiTreeUtil.getChildOfType(getListElement(), PhpModifierList.class);
	}

	@Override
	public boolean isStatic()
	{
		PhpFieldStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.isStatic();
		}
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.STATIC_KEYWORD);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PsiElement getDefaultValue()
	{
		PsiElement assign = findChildByType(PhpTokenTypes.opASGN);
		if(assign == null)
		{
			return null;
		}
		PsiElement value = assign.getNextSibling();
		while(value instanceof PsiWhiteSpace || value instanceof PsiComment)
		{
			value = value.getNextSibling();
		}
		return value;
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getDefaultValuePresentation()
	{
		PsiElement value = getDefaultValue();
		return value == null ? null : value.getText();
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

		PhpModifierList modifierList = getModifierList();
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

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getFieldType(this);
	}

	@Override
	@RequiredReadAction
	public PhpDocComment getDocComment()
	{
		PsiElement element = getListElement().getPrevSibling();
		while(element instanceof PsiWhiteSpace)
		{
			element = element.getPrevSibling();
		}
		return element instanceof PhpDocComment ? (PhpDocComment) element : null;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public Collection<PhpAttribute> getAttributes()
	{
		List<PhpAttribute> result = new ArrayList<>();
		for(PhpAttributesList attributesList : PsiTreeUtil.getChildrenOfTypeAsList(getListElement(), PhpAttributesList.class))
		{
			result.addAll(attributesList.getAttributes());
		}
		return result;
	}

	@Nullable
	@Override
	@RequiredReadAction
	public PhpTypeDeclaration getTypeDeclaration()
	{
		// constants and properties: the type is shared by the list
		PsiElement parent = getParent();
		return parent == null ? null : PsiTreeUtil.getChildOfType(parent, PhpTypeDeclaration.class);
	}
}
