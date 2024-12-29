// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.jetbrains.php;

import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.application.util.function.Processor;
import consulo.application.util.matcher.PrefixMatcher;
import consulo.index.io.ID;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@ServiceAPI(ComponentScope.PROJECT)
public abstract class PhpIndex
{
	@Nonnull
	public static PhpIndex getInstance(@Nonnull Project project)
	{
		return project.getComponent(PhpIndex.class);
	}

	/**
	 * @param name "\the\namespace"
	 */
	public abstract Collection<PhpNamespace> getNamespacesByName(String name);

	@Nonnull
	public abstract Collection<String> getAllConstantNames(@Nullable PrefixMatcher prefixMatcher);

	@Nonnull
	public abstract Collection<String> getAllVariableNames(@Nullable PrefixMatcher prefixMatcher);

	@Nonnull
	public abstract Collection<String> getAllFunctionNames(@Nullable PrefixMatcher prefixMatcher);

	/**
	 * @see #getAllClassFqns(PrefixMatcher)
	 */
	@Nonnull
	public abstract Collection<String> getAllClassNames(@Nullable PrefixMatcher prefixMatcher);

	/**
	 * @param prefixMatcher if null, all fqns will be returned
	 */
	@Nonnull
	public abstract Collection<String> getAllClassFqns(@Nullable final PrefixMatcher prefixMatcher);

	/**
	 * @param prefixMatcher if null, all fqns will be returned
	 */
	@Nonnull
	public abstract Collection<String> getAllInterfacesFqns(@Nullable PrefixMatcher prefixMatcher);

	/**
	 * @return all interface names in lowercase
	 */
	@Nonnull
	public abstract Collection<String> getAllInterfaceNames();

	/**
	 * @return all trait names in lowercase
	 */
	@Nonnull
	public abstract Collection<String> getAllTraitNames();

	@Nonnull
	public abstract Collection<String> getChildNamespacesByParentName(@Nullable String name);

	@Nonnull
	public abstract Collection<String> getAllChildNamespacesFqns(@Nonnull String parentNamespaceFqn);

	@Nonnull
	public abstract Collection<String> getTraitUsagesByFQN(@Nullable String name);

	@SuppressWarnings({"UnusedDeclaration"})
	public abstract Collection<PhpUse> getUseAliasesByName(@Nullable String name);

	@Nonnull
	public abstract Collection<PhpUse> getUseAliasesByReferenceName(@Nullable String name);

	@Nonnull
	public abstract Collection<Constant> getConstantsByFQN(@Nullable String fqn);

	@Nonnull
	public abstract Collection<Constant> getConstantsByName(@Nullable String name);

	@Nonnull
	public abstract Collection<Variable> getVariablesByName(@Nullable String name);

	@Nonnull
	public abstract Collection<Function> getFunctionsByName(@Nullable String name);

	@Nonnull
	public abstract Collection<Function> getFunctionsByFQN(@Nullable String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getInterfacesByName(@Nullable String name);

	public abstract Collection<PhpClass> getTraitsByName(String name);

	@Nonnull
	public abstract Collection<PhpClass> getClassesByName(@Nullable String name);

	@Nonnull
	public abstract Collection<PhpClass> getClassesByNameInScope(@Nullable String name, GlobalSearchScope scope);

	@Nullable
	public abstract PhpClass getClassByName(@Nullable String name);

	@Nonnull
	public abstract Collection<PhpClass> getDirectSubclasses(@Nullable String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getAllSubclasses(@Nullable String fqn);

	/**
	 * This scope should be used if results from php lib stubs are needed.
	 */
	public abstract GlobalSearchScope getSearchScope();

	@Nonnull
	public abstract Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s);

	@Nonnull
	public abstract Collection<? extends PhpNamedElement> getBySignature(@Nonnull String s, @Nullable Set<String> visited, int depth);

	@Nonnull
	protected abstract Collection<? extends PhpNamedElement> getBySignatureInternal(@Nonnull String s,
																					@Nullable Set<String> visited,
																					int depth);

	public abstract Collection<? extends PhpNamedElement> getTypeMethods(@Nonnull String classSign,
																		 @Nullable Set<String> visited, Map<String, String> providers);

	public abstract Collection<PhpClass> getClasses(@Nullable Set<String> visited, @Nonnull String classRef);

	@Nonnull
	public abstract Collection<PhpClass> getClassesByFQN(String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getInterfacesByFQN(String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getTraitsByFQN(String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getAnyByFQN(String fqn);

	@Nonnull
	public abstract Collection<PhpClass> getCoveringTestClasses(@Nonnull Project project, @Nonnull String targetClassFqn);

	@Nonnull
	public abstract Collection<Method> getCoveringTestMethods(@Nonnull Project project, @Nonnull Method method);

	public abstract Collection<PhpClass> getTraitUsages(PhpClass me);

	/**
	 * @param me      trait to look for
	 * @param visited cyclic reference prevention
	 * @return all traits using this, classes directly using it and their superclasses AND THEIR TRAITS ETC.
	 * @see processNestedTraitUsages
	 * @deprecated SLOW!
	 */
	public abstract Collection<PhpClass> getNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited);

	/**
	 * processes all traits using this, classes directly using it and their superclasses AND THEIR TRAITS ETC..
	 *
	 * @param me      trait to look for
	 * @param visited cyclic reference prevention
	 */
	public abstract boolean processNestedTraitUsages(PhpClass me, @Nullable Collection<String> visited, Processor<? super PhpClass> processor);

	protected abstract Collection<String> filterKeys(Collection<String> keys, ID id);

	@Nonnull
	public abstract <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements,
																				@Nullable String namespaceName,
																				boolean allowGlobal);

	/**
	 * @param elements      collection to filter
	 * @param namespaceName trailing '\' included!
	 * @return filtered collection
	 */
	@Nonnull
	public abstract <T extends PhpNamedElement> Collection<T> filterByNamespace(@Nonnull Collection<T> elements, @Nullable String namespaceName);

	public abstract PhpType completeType(@Nonnull Project p, @Nonnull PhpType type, @Nullable Set<String> visited);

	public abstract PhpType completeThis(@Nonnull PhpType type,
										 @Nullable String thisClass, final @Nonnull Set<String> visited);

	@Nonnull
	public abstract Collection<String> getClassAliasesNames();
}
