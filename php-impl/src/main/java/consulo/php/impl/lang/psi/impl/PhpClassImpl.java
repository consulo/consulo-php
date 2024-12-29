package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.PhpClassHierarchyUtils;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.application.util.CachedValueProvider;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiModificationTracker;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.LanguageCachedValueUtil;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.ItemPresentation;
import consulo.navigation.ItemPresentationProvider;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.util.collection.MultiMap;
import consulo.util.lang.StringUtil;
import consulo.util.lang.ref.Ref;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
	public PhpType getType()
	{
		return PhpType.builder().add(getFQN()).build();
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

		PsiElement parent = getParent();
		if(parent instanceof GroupStatement)
		{
			PsiElement groupParent = parent.getParent();
			if(groupParent instanceof PhpNamespace)
			{
				return ((PhpNamespace) groupParent).getName();
			}
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
		return getOwnConstructor();
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
		PhpModifierList element = findChildByClass(PhpModifierList.class);
		return element != null && element.hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD);
	}

	@Override
	public boolean isFinal()
	{
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return stub.isFinal();
		}
		PhpModifierList element = findChildByClass(PhpModifierList.class);
		return element != null && element.hasModifier(PhpTokenTypes.FINAL_KEYWORD);
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
		PhpClass superClass = getSuperClass();
		if(superClass != null)
		{
			return new PhpClass[]{superClass};
		}
		return PhpClass.EMPTY_ARRAY;
	}

	@Nonnull
	@Override
	public MultiMap<CharSequence, Field> getOwnFieldMap()
	{
		return LanguageCachedValueUtil.getCachedValue(this, () -> {
			MultiMap<CharSequence, Field> map = new MultiMap<>();
			for(Field field : getOwnFields())
			{
				map.putValue(field.getNameCS(), field);
			}
			return CachedValueProvider.Result.create(map, PsiModificationTracker.MODIFICATION_COUNT);
		});
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

	@Nonnull
	@Override
	public MultiMap<CharSequence, Method> getOwnMethodsMap()
	{
		return LanguageCachedValueUtil.getCachedValue(this, () -> {
			MultiMap<CharSequence, Method> map = new MultiMap<>();
			for(Method childrenByClas : getOwnMethods())
			{
				map.putValue(childrenByClas.getNameCS(), childrenByClas);
			}
			return CachedValueProvider.Result.create(map, PsiModificationTracker.MODIFICATION_COUNT);
		});
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
		return getFQN();
	}

	@Nullable
	@Override
	public Method findMethodByName(@Nullable CharSequence name)
	{
		Ref<Method> methodRef = Ref.create();
		PhpClassHierarchyUtils.processMethods(this, this, (method, subClass, baseClass) -> {
			if(StringUtil.equals(method.getNameCS(), name))
			{
				methodRef.set(method);
				return false;
			}

			return true;
		}, false);
		return methodRef.get();
	}

	@Nullable
	@Override
	public Method findOwnMethodByName(@Nullable CharSequence name)
	{
		Method[] ownMethods = getOwnMethods();
		for(Method ownMethod : ownMethods)
		{
			if(StringUtil.equals(ownMethod.getNameCS(), name))
			{
				return ownMethod;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public Field findFieldByName(@Nullable CharSequence name, boolean findConstant)
	{
		Ref<Field> fieldRef = Ref.create();
		PhpClassHierarchyUtils.processFields(this, this, (field, subClass, baseClass) -> {
			if(StringUtil.equals(field.getNameCS(), name))
			{
				fieldRef.set(field);
				return false;
			}

			return true;
		}, false);
		return fieldRef.get();
	}

	@Nullable
	@Override
	public Field findOwnFieldByName(@Nullable CharSequence name, boolean findConstant)
	{
		Field[] fields = getOwnFields();
		for(Field field : fields)
		{
			if(StringUtil.equals(field.getNameCS(), name))
			{
				return field;
			}
		}
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
	public ItemPresentation getPresentation()
	{
		return ItemPresentationProvider.getItemPresentation(this);
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
		else if(isAbstract())
		{
			return PhpModifier.PUBLIC_ABSTRACT_STATIC;
		}
		return PhpModifier.PUBLIC_IMPLEMENTED_STATIC;
	}
}
