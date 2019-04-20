package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.indexing.ID;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.index.PhpFullFqClassIndex;
import consulo.php.index.PhpFunctionByNameIndex;
import consulo.php.index.PhpNamespaceIndex;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
@Singleton
public class PhpIndexImpl extends PhpIndex
{
	private final Project myProject;

	@Inject
	public PhpIndexImpl(Project project)
	{
		myProject = project;
	}

	@Override
	public Collection<PhpNamespace> getNamespacesByName(String name)
	{
		Collection<PhpNamespace> collection = PhpNamespaceIndex.INSTANCE.get(name, myProject, GlobalSearchScope.allScope(myProject));
		return collection;
	}

	@Nonnull
	@Override
	public Collection<String> getAllConstantNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllVariableNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllFunctionNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllClassNames(@Nullable PrefixMatcher prefixMatcher)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllClassFqns(@Nullable PrefixMatcher prefixMatcher)
	{
		if(DumbService.isDumb(myProject))
		{
			return Collections.emptyList();
		}

		Collection<String> allKeys = PhpFullFqClassIndex.INSTANCE.getAllKeys(myProject);
		List<String> result = new ArrayList<>(allKeys.size());
		for(String key : allKeys)
		{
			if(prefixMatcher == null || prefixMatcher.prefixMatches(key))
			{
				result.add(key);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public Collection<String> getAllInterfacesFqns(@Nullable PrefixMatcher prefixMatcher)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllInterfaceNames()
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllTraitNames()
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getChildNamespacesByParentName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getAllChildNamespacesFqns(@Nonnull String parentNamespaceFqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getTraitUsagesByFQN(@Nullable String name)
	{
		return null;
	}

	@Override
	public Collection<PhpUse> getUseAliasesByName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpUse> getUseAliasesByReferenceName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<Constant> getConstantsByFQN(@Nullable String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<Constant> getConstantsByName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<Variable> getVariablesByName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<Function> getFunctionsByName(@Nullable String name)
	{
		if(name == null)
		{
			List<Function> result = new ArrayList<>();
			Collection<String> allKeys = PhpFunctionByNameIndex.INSTANCE.getAllKeys(myProject);
			for(String key : allKeys)
			{
				result.addAll(PhpFunctionByNameIndex.INSTANCE.get(key, myProject, GlobalSearchScope.allScope(myProject)));
			}
			return result;
		}
		return PhpFunctionByNameIndex.INSTANCE.get(name, myProject, GlobalSearchScope.allScope(myProject));
	}

	@Nonnull
	@Override
	public Collection<Function> getFunctionsByFQN(@Nullable String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getInterfacesByName(@Nullable String name)
	{
		return null;
	}

	@Override
	public Collection<PhpClass> getTraitsByName(String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByNameInScope(@Nullable String name, GlobalSearchScope scope)
	{
		return null;
	}

	@Nullable
	@Override
	public PhpClass getClassByName(@Nullable String name)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getDirectSubclasses(@Nullable String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getAllSubclasses(@Nullable String fqn)
	{
		return null;
	}

	@Override
	public GlobalSearchScope getSearchScope()
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s, @Nullable Set<String> visited, int depth)
	{
		return null;
	}

	@Nonnull
	@Override
	protected Collection<? extends PhpNamedElement> getBySignatureInternal(@Nonnull String s, @Nullable Set<String> visited, int depth)
	{
		return null;
	}

	@Override
	public Collection<? extends PhpNamedElement> getTypeMethods(@Nonnull String classSign, @Nullable Set<String> visited, Map<String, String> providers)
	{
		return null;
	}

	@Override
	public Collection<PhpClass> getClasses(@Nullable Set<String> visited, @Nonnull String classRef)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getClassesByFQN(String fqn)
	{
		return PhpFullFqClassIndex.INSTANCE.get(fqn, myProject, GlobalSearchScope.allScope(myProject));
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getInterfacesByFQN(String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getTraitsByFQN(String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getAnyByFQN(String fqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<PhpClass> getCoveringTestClasses(@Nonnull Project project, @Nonnull String targetClassFqn)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<Method> getCoveringTestMethods(@Nonnull Project project, @Nonnull Method method)
	{
		return null;
	}

	@Override
	public Collection<PhpClass> getTraitUsages(PhpClass me)
	{
		return null;
	}

	@Override
	public Collection<PhpClass> getNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited)
	{
		return null;
	}

	@Override
	public boolean processNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited, Processor<? super PhpClass> processor)
	{
		return false;
	}

	@Override
	protected Collection<String> filterKeys(Collection<String> keys, ID id)
	{
		return null;
	}

	@Override
	public PhpType completeType(@Nonnull Project p, @Nonnull PhpType type, @Nullable Set<String> visited)
	{
		return null;
	}

	@Override
	public PhpType completeThis(@Nonnull PhpType type, @Nullable String thisClass, @Nonnull Set<String> visited)
	{
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> getClassAliasesNames()
	{
		return null;
	}

	@Nonnull
	@Override
	public <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements, @Nullable String namespaceName)
	{
		return null;
	}

	@Nonnull
	@Override
	public <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements, @Nullable String namespaceName, boolean allowGlobal)
	{
		return null;
	}
}
