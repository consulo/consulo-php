package consulo.php.lang.psi.impl;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.*;
import consulo.php.lang.psi.impl.stub.PhpClassStub;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.val;

/**
 * @author jay
 * @date Apr 8, 2008 1:54:50 PM
 */
public class PhpClassImpl extends PhpStubbedNamedElementImpl<PhpClassStub> implements PhpClass
{
	public PhpClassImpl(ASTNode node)
	{
		super(node);
	}

	public PhpClassImpl(@NotNull PhpClassStub stub)
	{
		super(stub, PhpStubElements.CLASS);
	}

	@NotNull
	@Override
	public PhpField[] getFields()
	{
		return findChildrenByClass(PhpField.class);
	}

	@Override
	@NotNull
	public PhpFunction[] getFunctions()
	{
		return getStubOrPsiChildren(PhpStubElements.FUNCTION, PhpFunction.ARRAY_FACTORY);
	}

	@Override
	public String getNamespace()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.getNamespace();
		}

		PhpElement prevPhpSibling = getPrevPsiSibling();
		while(prevPhpSibling != null)
		{
			if(prevPhpSibling instanceof PhpNamespaceStatement)
			{
				return ((PhpNamespaceStatement) prevPhpSibling).getNamespace();
			}
			prevPhpSibling = prevPhpSibling.getPrevPsiSibling();
		}
		return null;
	}

	@Override
	public PhpClass getSuperClass()
	{
		val list = PsiTreeUtil.getChildOfType(this, PhpExtendsList.class);
		assert list != null;
		return list.getExtendsClass();
	}

	@Override
	public PhpClass[] getImplementedInterfaces()
	{
		val list = PsiTreeUtil.getChildOfType(this, PhpImplementsList.class);
		assert list != null;
		val interfaceList = list.getInterfaces();
		return interfaceList.toArray(new PhpClass[interfaceList.size()]);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public PhpFunction getConstructor()
	{
		PhpFunction newOne = null;
		PhpFunction oldOne = null;
		for(PhpFunction phpMethod : this.getFunctions())
		{
			if(phpMethod.getName().equals(CONSTRUCTOR))
			{
				newOne = phpMethod;
			}
			if(phpMethod.getName().equals(this.getName()))
			{
				oldOne = phpMethod;
			}
		}
		if(newOne != null)
		{
			return newOne;
		}
		return oldOne;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitClass(this);
	}

	@Override
	public boolean isInterface()
	{
		return findChildByType(PhpTokenTypes.INTERFACE_KEYWORD) != null;
	}

	@Override
	public boolean isTrait()
	{
		return findChildByType(PhpTokenTypes.TRAIT_KEYWORD) != null;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return ItemPresentationProviders.getItemPresentation(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		for(PhpFunction phpMethod : getFunctions())
		{
			if(!processor.execute(phpMethod, state))
			{
				return false;
			}
		}

		for(PhpField phpField : getFields())
		{
			if(!processor.execute(phpField, state))
			{
				return false;
			}
		}

		return super.processDeclarations(processor, state, lastParent, place);
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
	public PsiElement getLeftBrace()
	{
		return findChildByType(PhpTokenTypes.chLBRACE);
	}

	@Override
	public PsiElement getRightBrace()
	{
		return findChildByType(PhpTokenTypes.chRBRACE);
	}
}
