package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.component.ServiceImpl;
import consulo.application.progress.ProgressManager;
import consulo.application.util.function.Processor;
import consulo.application.util.matcher.PrefixMatcher;
import consulo.index.io.ID;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.php.impl.index.PhpClassIndex;
import consulo.php.impl.index.PhpDefineIndex;
import consulo.php.impl.index.PhpFullFqClassIndex;
import consulo.php.impl.index.PhpFunctionByNameIndex;
import consulo.php.impl.index.PhpNamespaceIndex;
import consulo.php.impl.index.PhpSuperTypeIndex;
import consulo.php.impl.lang.psi.impl.light.PhpEmptyNamespaceImpl;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeResolver;
import consulo.project.DumbService;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.function.Predicate;

/**
 * Class and function lookups are case-insensitive (keys are lower-cased), constants are case-sensitive.
 *
 * @author VISTALL
 * @since 2019-04-12
 */
@Singleton
@ServiceImpl
public class PhpIndexImpl extends PhpIndex
{
	private final Project myProject;

	@Inject
	public PhpIndexImpl(Project project)
	{
		myProject = project;
	}

	private <T extends PsiElement> Collection<T> get(StringStubIndexExtension<T> index, @Nullable String key)
	{
		if(key == null || DumbService.isDumb(myProject))
		{
			return Collections.emptyList();
		}
		return index.get(key, myProject, getSearchScope());
	}

	private Collection<String> keys(StringStubIndexExtension<?> index, @Nullable PrefixMatcher prefixMatcher)
	{
		if(DumbService.isDumb(myProject))
		{
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<>();
		for(String key : index.getAllKeys(myProject))
		{
			if(prefixMatcher == null || prefixMatcher.prefixMatches(key))
			{
				result.add(key);
			}
		}
		return result;
	}

	private static String normalizeFqn(String fqn)
	{
		return (fqn.startsWith("\\") ? fqn : "\\" + fqn).toLowerCase(Locale.ROOT);
	}

	private static String shortName(String fqn)
	{
		return fqn.substring(fqn.lastIndexOf('\\') + 1);
	}

	private static Collection<PhpClass> filter(Collection<PhpClass> classes, Predicate<PhpClass> predicate)
	{
		List<PhpClass> result = new ArrayList<>(classes.size());
		for(PhpClass phpClass : classes)
		{
			if(predicate.test(phpClass))
			{
				result.add(phpClass);
			}
		}
		return result;
	}

	private static boolean isClass(PhpClass phpClass)
	{
		return !phpClass.isInterface() && !phpClass.isTrait();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// namespaces

	@Override
	public Collection<PhpNamespace> getNamespacesByName(String name)
	{
		String key = namespaceKey(name);
		Collection<PhpNamespace> collection = get(PhpNamespaceIndex.INSTANCE, key);
		if(!collection.isEmpty())
		{
			return collection;
		}
		// a parent of declared namespaces exists without a declaration
		String prefix = key + "\\";
		for(String indexKey : keys(PhpNamespaceIndex.INSTANCE, null))
		{
			if(indexKey.startsWith(prefix))
			{
				return Collections.singletonList(new PhpEmptyNamespaceImpl(myProject, name));
			}
		}
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<String> getChildNamespacesByParentName(@Nullable String name)
	{
		String prefix = namespacePrefix(name);
		// lowercased child name -> child name as declared
		Map<String, String> result = new LinkedHashMap<>();
		for(String key : keys(PhpNamespaceIndex.INSTANCE, null))
		{
			if(key.startsWith(prefix) && key.length() > prefix.length())
			{
				String rest = key.substring(prefix.length());
				int separator = rest.indexOf('\\');
				String child = separator < 0 ? rest : rest.substring(0, separator);
				result.computeIfAbsent(child, it -> declaredSegment(key, prefix.length(), it));
			}
		}
		return result.values();
	}

	@Nonnull
	@Override
	public Collection<String> getAllChildNamespacesFqns(@Nonnull String parentNamespaceFqn)
	{
		String prefix = namespacePrefix(parentNamespaceFqn);
		List<String> result = new ArrayList<>();
		for(String key : keys(PhpNamespaceIndex.INSTANCE, null))
		{
			if(key.startsWith(prefix))
			{
				result.add(declaredName(key));
			}
		}
		return result;
	}

	/**
	 * Namespace keys are lowercased FQNs without a trailing separator: {@code \app\controller}
	 */
	private static String namespaceKey(String name)
	{
		String key = normalizeFqn(name);
		return key.length() > 1 && key.endsWith("\\") ? key.substring(0, key.length() - 1) : key;
	}

	private static String namespacePrefix(@Nullable String name)
	{
		if(name == null || name.isEmpty())
		{
			return "\\";
		}
		String key = normalizeFqn(name);
		return key.endsWith("\\") ? key : key + "\\";
	}

	/**
	 * Namespace FQN as declared, for a lowercased index key
	 */
	private String declaredName(String key)
	{
		for(PhpNamespace namespace : get(PhpNamespaceIndex.INSTANCE, key))
		{
			String name = namespace.getName();
			if(name != null && name.length() + 1 == key.length())
			{
				return "\\" + name;
			}
		}
		return key;
	}

	private String declaredSegment(String key, int start, String lowercased)
	{
		String declared = declaredName(key);
		return declared.length() >= start + lowercased.length() ? declared.substring(start, start + lowercased.length()) : lowercased;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// names

	@Nonnull
	@Override
	public Collection<String> getAllConstantNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return keys(PhpDefineIndex.INSTANCE, prefixMatcher);
	}

	@Nonnull
	@Override
	public Collection<String> getAllVariableNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<String> getAllFunctionNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return keys(PhpFunctionByNameIndex.INSTANCE, prefixMatcher);
	}

	@Nonnull
	@Override
	public Collection<String> getAllClassNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return keys(PhpClassIndex.INSTANCE, prefixMatcher);
	}

	@Nonnull
	@Override
	public Collection<String> getAllClassFqns(@Nullable PrefixMatcher prefixMatcher)
	{
		return keys(PhpFullFqClassIndex.INSTANCE, prefixMatcher);
	}

	@Nonnull
	@Override
	public Collection<String> getAllInterfacesFqns(@Nullable PrefixMatcher prefixMatcher)
	{
		List<String> result = new ArrayList<>();
		for(String key : keys(PhpFullFqClassIndex.INSTANCE, prefixMatcher))
		{
			ProgressManager.checkCanceled();
			if(!getInterfacesByFQN(key).isEmpty())
			{
				result.add(key);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public Collection<String> getAllInterfaceNames()
	{
		return namesOf(PhpClass::isInterface);
	}

	@Nonnull
	@Override
	public Collection<String> getAllTraitNames()
	{
		return namesOf(PhpClass::isTrait);
	}

	private Collection<String> namesOf(Predicate<PhpClass> predicate)
	{
		List<String> result = new ArrayList<>();
		for(String key : keys(PhpClassIndex.INSTANCE, null))
		{
			ProgressManager.checkCanceled();
			for(PhpClass phpClass : get(PhpClassIndex.INSTANCE, key))
			{
				if(predicate.test(phpClass))
				{
					result.add(phpClass.getName());
					break;
				}
			}
		}
		return result;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// classes

	@Nonnull
	@Override
	public Collection<PhpClass> getAnyByFQN(String fqn)
	{
		return fqn == null ? Collections.emptyList() : get(PhpFullFqClassIndex.INSTANCE, normalizeFqn(fqn));
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByFQN(String fqn)
	{
		return filter(getAnyByFQN(fqn), PhpIndexImpl::isClass);
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getInterfacesByFQN(String fqn)
	{
		return filter(getAnyByFQN(fqn), PhpClass::isInterface);
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getTraitsByFQN(String fqn)
	{
		return filter(getAnyByFQN(fqn), PhpClass::isTrait);
	}

	private Collection<PhpClass> getAnyByName(@Nullable String name)
	{
		return name == null ? Collections.emptyList() : get(PhpClassIndex.INSTANCE, name.toLowerCase(Locale.ROOT));
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByName(@Nullable String name)
	{
		return filter(getAnyByName(name), PhpIndexImpl::isClass);
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getInterfacesByName(@Nullable String name)
	{
		return filter(getAnyByName(name), PhpClass::isInterface);
	}

	@Override
	public Collection<PhpClass> getTraitsByName(String name)
	{
		return filter(getAnyByName(name), PhpClass::isTrait);
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByNameInScope(@Nullable String name, GlobalSearchScope scope)
	{
		if(name == null || DumbService.isDumb(myProject))
		{
			return Collections.emptyList();
		}
		return filter(PhpClassIndex.INSTANCE.get(name.toLowerCase(Locale.ROOT), myProject, scope), PhpIndexImpl::isClass);
	}

	@Nullable
	@Override
	public PhpClass getClassByName(@Nullable String name)
	{
		Collection<PhpClass> classes = getClassesByName(name);
		return classes.isEmpty() ? null : classes.iterator().next();
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getDirectSubclasses(@Nullable String fqn)
	{
		return fqn == null ? Collections.emptyList() : get(PhpSuperTypeIndex.INSTANCE, normalizeFqn(fqn));
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getAllSubclasses(@Nullable String fqn)
	{
		if(fqn == null)
		{
			return Collections.emptyList();
		}
		Set<String> visited = new HashSet<>();
		Deque<String> queue = new ArrayDeque<>();
		queue.add(normalizeFqn(fqn));
		List<PhpClass> result = new ArrayList<>();
		while(!queue.isEmpty())
		{
			ProgressManager.checkCanceled();
			String current = queue.poll();
			if(!visited.add(current))
			{
				continue;
			}
			for(PhpClass subclass : getDirectSubclasses(current))
			{
				result.add(subclass);
				String subclassFqn = subclass.getFQN();
				if(subclassFqn != null)
				{
					queue.add(normalizeFqn(subclassFqn));
				}
			}
		}
		return result;
	}

	@Override
	public GlobalSearchScope getSearchScope()
	{
		// the runtime stubs are roots of the PHP SDK
		return GlobalSearchScope.allScope(myProject);
	}

	@Nonnull
	@Override
	public Collection<String> getClassAliasesNames()
	{
		return Collections.emptyList();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// functions and constants

	@Nonnull
	@Override
	public Collection<Function> getFunctionsByName(@Nullable String name)
	{
		if(name == null)
		{
			List<Function> result = new ArrayList<>();
			for(String key : keys(PhpFunctionByNameIndex.INSTANCE, null))
			{
				ProgressManager.checkCanceled();
				result.addAll(get(PhpFunctionByNameIndex.INSTANCE, key));
			}
			return result;
		}
		return get(PhpFunctionByNameIndex.INSTANCE, name.toLowerCase(Locale.ROOT));
	}

	@Nonnull
	@Override
	public Collection<Function> getFunctionsByFQN(@Nullable String fqn)
	{
		if(fqn == null)
		{
			return Collections.emptyList();
		}
		String normalized = normalizeFqn(fqn);
		List<Function> result = new ArrayList<>();
		for(Function function : getFunctionsByName(shortName(normalized)))
		{
			String functionFqn = function.getFQN();
			if(functionFqn != null && normalized.equals(normalizeFqn(functionFqn)))
			{
				result.add(function);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public Collection<Constant> getConstantsByName(@Nullable String name)
	{
		return new ArrayList<>(get(PhpDefineIndex.INSTANCE, name));
	}

	@Nonnull
	@Override
	public Collection<Constant> getConstantsByFQN(@Nullable String fqn)
	{
		if(fqn == null)
		{
			return Collections.emptyList();
		}
		String name = shortName(fqn);
		List<Constant> result = new ArrayList<>();
		for(Constant constant : getConstantsByName(name))
		{
			String constantFqn = constant.getFQN();
			if(constantFqn == null || constantFqn.equals(fqn.startsWith("\\") ? fqn : "\\" + fqn) || ("\\" + name).equals(fqn))
			{
				result.add(constant);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public Collection<Variable> getVariablesByName(@Nullable String name)
	{
		return Collections.emptyList();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// not indexed yet

	@Nonnull
	@Override
	public Collection<String> getTraitUsagesByFQN(@Nullable String name)
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<PhpUse> getUseAliasesByName(@Nullable String name)
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<PhpUse> getUseAliasesByReferenceName(@Nullable String name)
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getCoveringTestClasses(@Nonnull Project project, @Nonnull String targetClassFqn)
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<Method> getCoveringTestMethods(@Nonnull Project project, @Nonnull Method method)
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<PhpClass> getTraitUsages(PhpClass me)
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<PhpClass> getNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean processNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited, Processor<? super PhpClass> processor)
	{
		return true;
	}

	@Override
	protected Collection<String> filterKeys(Collection<String> keys, ID id)
	{
		return keys;
	}

	@Nonnull
	@Override
	public <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements, @Nullable String namespaceName)
	{
		return filterByNamespace(elements, namespaceName, false);
	}

	@Nonnull
	@Override
	public <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements, @Nullable String namespaceName, boolean allowGlobal)
	{
		String expected = namespaceName == null ? "" : StringUtilTrim.trimSlashes(namespaceName);
		List<T> result = new ArrayList<>();
		for(T element : elements)
		{
			String namespace = StringUtilTrim.trimSlashes(element.getNamespaceName());
			if(namespace.equalsIgnoreCase(expected) || allowGlobal && namespace.isEmpty())
			{
				result.add(element);
			}
		}
		return result;
	}

	private static final class StringUtilTrim
	{
		static String trimSlashes(@Nullable String name)
		{
			if(name == null)
			{
				return "";
			}
			String result = name;
			while(result.startsWith("\\"))
			{
				result = result.substring(1);
			}
			while(result.endsWith("\\"))
			{
				result = result.substring(0, result.length() - 1);
			}
			return result;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// types

	private PhpTypeResolver getTypeResolver()
	{
		return new PhpTypeResolver(this, myProject);
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s)
	{
		return getBySignature(s, null, 0);
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s, @Nullable Set<String> visited, int depth)
	{
		return getBySignatureInternal(s, visited, depth);
	}

	@Nonnull
	@Override
	protected Collection<? extends PhpNamedElement> getBySignatureInternal(@Nonnull String s, @Nullable Set<String> visited, int depth)
	{
		return getTypeResolver().getBySignature(s, visited, depth);
	}

	@Override
	public Collection<? extends PhpNamedElement> getTypeMethods(@Nonnull String classSign, @Nullable Set<String> visited, Map<String, String> providers)
	{
		List<PhpNamedElement> result = new ArrayList<>();
		for(PhpClass phpClass : getClasses(visited, classSign))
		{
			result.addAll(phpClass.getMethods());
		}
		return result;
	}

	@Override
	public Collection<PhpClass> getClasses(@Nullable Set<String> visited, @Nonnull String classRef)
	{
		List<PhpClass> result = new ArrayList<>();
		for(String type : completeType(myProject, PhpType.builder().add(classRef).build(), visited).getTypes())
		{
			if(!PhpType.isPrimitiveType(type))
			{
				result.addAll(getAnyByFQN(type));
			}
		}
		return result;
	}

	@Override
	public PhpType completeType(@Nonnull Project p, @Nonnull PhpType type, @Nullable Set<String> visited)
	{
		return getTypeResolver().complete(type, visited);
	}

	@Override
	public PhpType completeThis(@Nonnull PhpType type, @Nullable String thisClass, @Nonnull Set<String> visited)
	{
		return getTypeResolver().completeThis(type, thisClass, visited);
	}
}
