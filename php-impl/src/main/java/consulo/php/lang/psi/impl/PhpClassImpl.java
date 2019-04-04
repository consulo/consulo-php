package consulo.php.lang.psi.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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

	public PhpClassImpl(@Nonnull PhpClassStub stub)
	{
		super(stub, PhpStubElements.CLASS);
	}

	@Nonnull
	@Override
	public List<Field> getFields()
	{
		return Arrays.asList(getOwnFields());
	}

	@Override
	public Field[] getOwnFields()
	{
		return getStubOrPsiChildren(PhpStubElements.CLASS_FIELD, Field.ARRAY_FACTORY);
	}

	@Nullable
	@Override
	public String getSuperName()
	{
		return null;
	}

	@Nullable
	@Override
	public String getSuperFQN()
	{
		return null;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.getNamespaceName();
		}

		PhpPsiElement prevPhpSibling = getPrevPsiSibling();
		while(prevPhpSibling != null)
		{
			if(prevPhpSibling instanceof PhpNamespace)
			{
				return ((PhpNamespace) prevPhpSibling).getNamespace();
			}
			prevPhpSibling = prevPhpSibling.getPrevPsiSibling();
		}
		return "";
	}

	@Override
	public PhpClass getSuperClass()
	{
		ExtendsList list = PsiTreeUtil.getChildOfType(this, ExtendsList.class);
		assert list != null;
		return list.getExtendsClass();
	}

	@Nonnull
	@Override
	public String[] getInterfaceNames()
	{
		return new String[0];
	}

	@Override
	public PhpClass[] getImplementedInterfaces()
	{
		ImplementsList list = PsiTreeUtil.getChildOfType(this, ImplementsList.class);
		assert list != null;
		List<PhpClass> interfaceList = list.getInterfaces();
		return interfaceList.toArray(new PhpClass[interfaceList.size()]);
	}

	@Override
	public Method getConstructor()
	{
		Method newOne = null;
		Method oldOne = null;
		for(Method method : getOwnMethods())
		{
			if(method.getName().equals(CONSTRUCTOR))
			{
				newOne = method;
			}
			if(method.getName().equals(this.getName()))
			{
				oldOne = method;
			}
		}
		if(newOne != null)
		{
			return newOne;
		}
		return oldOne;
	}

	@Override
	public boolean isAnonymous()
	{
		return false;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitClass(this);
	}

	@Override
	public boolean isInterface()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.isInterface();
		}
		return findChildByType(PhpTokenTypes.INTERFACE_KEYWORD) != null;
	}

	@Override
	public boolean isAbstract()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.isAbstract();
		}
		return false;  // TODO [VISTALL] impl it!
	}

	@Override
	public boolean isFinal()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.isFinal();
		}
		return false; // TODO [VISTALL] impl it!
	}

	@Nonnull
	@Override
	public ExtendsList getExtendsList()
	{
		return findChildByClass(ExtendsList.class);
	}

	@Nonnull
	@Override
	public ImplementsList getImplementsList()
	{
		return findChildByClass(ImplementsList.class);
	}

	@Override
	public boolean isTrait()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.isTrait();
		}
		return findChildByType(PhpTokenTypes.TRAIT_KEYWORD) != null;
	}

	@Nonnull
	@Override
	public String[] getTraitNames()
	{
		return new String[0];
	}

	@Override
	public PhpClass[] getTraits()
	{
		return new PhpClass[0];
	}

	@Override
	public String[] getMixinNames()
	{
		return new String[0];
	}

	@Override
	public PhpClass[] getMixins()
	{
		return new PhpClass[0];
	}

	@Override
	public PhpClass[] getSupers()
	{
		return new PhpClass[0];
	}

	@Override
	public MultiMap<CharSequence, Field> getOwnFieldMap()
	{
		return null;
	}

	@Override
	public Collection<Method> getMethods()
	{
		return Arrays.asList(getOwnMethods());
	}

	@Override
	public Method[] getOwnMethods()
	{
		return getStubOrPsiChildren(PhpStubElements.CLASS_METHOD, Method.ARRAY_FACTORY);
	}

	@Override
	public MultiMap<CharSequence, Method> getOwnMethodsMap()
	{
		MultiMap<CharSequence, Method> map = new MultiMap<>();
		for(Method childrenByClas : getOwnMethods())
		{
			map.putValue(childrenByClas.getName(), childrenByClas);
		}
		return map;
	}

	@Override
	public boolean hasOwnStaticMembers()
	{
		return false;
	}

	@Override
	public boolean hasStaticMembers()
	{
		return false;
	}

	@Override
	public boolean hasTraitUses()
	{
		return false;
	}

	@Override
	public List<PhpTraitUseRule> getTraitUseRules()
	{
		return null;
	}

	@Nonnull
	@Override
	public String getPresentableFQN()
	{
		return null;
	}

	@Nullable
	@Override
	public Method findMethodByName(@Nullable CharSequence name)
	{
		return null;
	}

	@Nullable
	@Override
	public Method findOwnMethodByName(@Nullable CharSequence name)
	{
		return null;
	}

	@Nullable
	@Override
	public Field findFieldByName(@Nullable CharSequence name, boolean findConstant)
	{
		return null;
	}

	@Nullable
	@Override
	public Field findOwnFieldByName(@Nullable CharSequence name, boolean findConstant)
	{
		return null;
	}

	@Override
	public boolean hasMethodTags()
	{
		return false;
	}

	@Override
	public boolean hasPropertyTags()
	{
		return false;
	}

	@Override
	public boolean hasConstructorFields()
	{
		return false;
	}

	@Nullable
	@Override
	public Method getOwnConstructor()
	{
		return getConstructor();
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return ItemPresentationProviders.getItemPresentation(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place)
	{
		for(Function phpMethod : getOwnMethods())
		{
			if(!processor.execute(phpMethod, state))
			{
				return false;
			}
		}

		for(Field phpField : getFields())
		{
			if(!processor.execute(phpField, state))
			{
				return false;
			}
		}

		return super.processDeclarations(processor, state, lastParent, place);
	}

	@Nonnull
	@Override
	public PhpModifier getModifier()
	{
		if(isFinal())
		{
			return PhpModifier.PUBLIC_FINAL_STATIC;
		}
		return PhpModifier.PUBLIC_IMPLEMENTED_STATIC;
	}
}
