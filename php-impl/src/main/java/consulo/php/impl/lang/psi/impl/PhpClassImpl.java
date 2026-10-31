package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.PhpLangUtil;

import consulo.language.ast.IElementType;
import consulo.php.impl.lang.parser.PhpElementTypes;
import com.jetbrains.php.PhpClassHierarchyUtils;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import consulo.util.collection.ArrayUtil;
import consulo.php.impl.lang.psi.PhpClassSupertypes;
import consulo.php.impl.lang.psi.PhpClassLookup;
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

	/**
	 * @return own and inherited fields and constants
	 */
	@Nonnull
	@Override
	public Collection<Field> getFields()
	{
		Map<String, Field> fields = new LinkedHashMap<>();
		PhpClassHierarchyUtils.processFields(this, this, (field, subClass, baseClass) -> {
			fields.putIfAbsent((field.isConstant() ? "const:" : "") + field.getName(), field);
			return true;
		}, false);
		return fields.values();
	}

	@Override
	public Field[] getOwnFields()
	{
		List<Field> fields = new ArrayList<>();
		if(getGreenStub() != null)
		{
			fields.addAll(List.of(getStubOrPsiChildren(PhpStubElements.CLASS_FIELD, Field.ARRAY_FACTORY)));
		}
		else
		{
			for(PsiElement child : getChildren())
			{
				IElementType type = child.getNode().getElementType();
				if(type == PhpElementTypes.CLASS_FIELDS || type == PhpElementTypes.CLASS_CONSTANTS)
				{
					fields.addAll(PsiTreeUtil.getChildrenOfTypeAsList(child, Field.class));
				}
			}
		}

		// promoted constructor parameters are fields, like in PhpStorm
		Method constructor = findOwnMethodByName("__construct");
		if(constructor != null)
		{
			for(Parameter parameter : constructor.getParameters())
			{
				if(parameter instanceof Field field)
				{
					fields.add(field);
				}
			}
		}
		return fields.toArray(Field.EMPTY);
	}

	@Nullable
	@Override
	public String getSuperName()
	{
		String fqn = getSuperFQN();
		return fqn == null ? null : fqn.substring(fqn.lastIndexOf('\\') + 1);
	}

	@Nullable
	@Override
	public String getSuperFQN()
	{
		PhpClassStub stub = getStub();
		return stub != null ? stub.getSuperclass() : PhpClassSupertypes.superClassFqn(this);
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		// \Foo\Bar\, the global namespace is \
		PhpClassStub stub = getStub();
		if(stub != null)
		{
			return PhpLangUtil.toNamespaceName(stub.getNamespaceName());
		}

		PsiElement parent = getParent();
		if(parent instanceof GroupStatement)
		{
			PsiElement groupParent = parent.getParent();
			if(groupParent instanceof PhpNamespace)
			{
				return PhpLangUtil.toNamespaceName(((PhpNamespace) groupParent).getName());
			}
		}
		return PhpLangUtil.GLOBAL_NAMESPACE_NAME;
	}

	@Nullable
	@Override
	public PhpClass getSuperClass()
	{
		String fqn = getSuperFQN();
		if(fqn == null)
		{
			return null;
		}
		for(PhpClass phpClass : PhpClassLookup.findClasses(this, fqn))
		{
			if(phpClass != this)
			{
				return phpClass;
			}
		}
		return null;
	}

	@Nonnull
	@Override
	public String[] getInterfaceNames()
	{
		PhpClassStub stub = getStub();
		return stub != null ? stub.getInterfaces() : ArrayUtil.toStringArray(PhpClassSupertypes.interfaceFqns(this));
	}

	@Override
	public PhpClass[] getImplementedInterfaces()
	{
		return findAll(getInterfaceNames());
	}

	private PhpClass[] findAll(String[] fqns)
	{
		List<PhpClass> result = new ArrayList<>();
		for(String fqn : fqns)
		{
			for(PhpClass phpClass : PhpClassLookup.findClasses(this, fqn))
			{
				if(phpClass != this)
				{
					result.add(phpClass);
					break;
				}
			}
		}
		return result.toArray(PhpClass.EMPTY_ARRAY);
	}

	@Override
	public Method getConstructor()
	{
		// the own constructor, else the inherited one; a cyclic hierarchy of broken code ends the walk
		Set<PhpClass> visited = new HashSet<>();
		for(PhpClass phpClass = this; phpClass != null && visited.add(phpClass); phpClass = phpClass.getSuperClass())
		{
			Method constructor = phpClass.getOwnConstructor();
			if(constructor != null)
			{
				return constructor;
			}
		}
		return null;
	}

	@Override
	public boolean isAnonymous()
	{
		// anonymous classes are never stubbed
		return getStub() == null && getNode().getElementType() == PhpElementTypes.ANONYMOUS_CLASS;
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
	public boolean isEnum()
	{
		return findChildByType(PhpTokenTypes.ENUM_KEYWORD) != null;
	}

	@Override
	public boolean isReadonly()
	{
		PhpModifierList modifierList = findChildByClass(PhpModifierList.class);
		return modifierList != null && modifierList.hasModifier(PhpTokenTypes.READONLY_KEYWORD);
	}

	@Nonnull
	@Override
	public Collection<PhpEnumCase> getEnumCases()
	{
		return Arrays.asList(findChildrenByClass(PhpEnumCase.class));
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
		PhpClassStub stub = getStub();
		return stub != null ? stub.getTraits() : ArrayUtil.toStringArray(PhpClassSupertypes.traitFqns(this));
	}

	@Override
	public PhpClass[] getTraits()
	{
		return findAll(getTraitNames());
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
		List<PhpClass> result = new ArrayList<>();
		PhpClass superClass = getSuperClass();
		if(superClass != null)
		{
			result.add(superClass);
		}
		result.addAll(Arrays.asList(getImplementedInterfaces()));
		return result.toArray(PhpClass.EMPTY_ARRAY);
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

	/**
	 * @return own and inherited methods, overridden ones excluded
	 */
	@Nonnull
	@Override
	public Collection<Method> getMethods()
	{
		Map<String, Method> methods = new LinkedHashMap<>();
		PhpClassHierarchyUtils.processMethods(this, this, (method, subClass, baseClass) -> {
			methods.putIfAbsent(method.getName().toLowerCase(Locale.ROOT), method);
			return true;
		}, false);
		return methods.values();
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
		return getTraitNames().length > 0;
	}

	@Override
	public List<PhpTraitUseRule> getTraitUseRules()
	{
		// the insteadof / as rules of a trait use are not parsed yet: the members of the used traits are taken as they are
		return List.of();
	}

	@Nonnull
	@Override
	public String getPresentableFQN()
	{
		// the FQN without its leading separator
		return PhpLangUtil.toPresentableFQN(getFQN());
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
