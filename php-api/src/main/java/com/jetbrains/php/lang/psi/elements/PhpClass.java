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
package com.jetbrains.php.lang.psi.elements;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.MultiMap;

public interface PhpClass extends PhpNamedElement, PhpElementWithModifier, PhpCallbackElement
{
	String CLONE = "__clone";
	String TO_STRING = "__toString";
	String CONSTRUCTOR = "__construct";
	String DESTRUCTOR = "__destruct";
	String INVOKE = "__invoke";
	String PARENT = "parent";
	String SELF = "self";
	String STATIC = "static";
	String CLASS = "class";
	PhpClass[] EMPTY_ARRAY = new PhpClass[0];
	Condition<PsiElement> INSTANCEOF = use -> use instanceof PhpClass;
	String ANONYMOUS = "__anonymous@";

	boolean isAnonymous();

	boolean isInterface();

	boolean isAbstract();

	boolean isFinal();

	@Nonnull
	ExtendsList getExtendsList();

	@Nonnull
	ImplementsList getImplementsList();

	@Nullable
	String getSuperName();

	@Nullable
	String getSuperFQN();

	@Nullable
	PhpClass getSuperClass();

	@Nonnull
	String[] getInterfaceNames();

	PhpClass[] getImplementedInterfaces();

	boolean hasTraitUses();

	@Nonnull
	String[] getTraitNames();

	PhpClass[] getTraits();

	String[] getMixinNames();

	PhpClass[] getMixins();

	PhpClass[] getSupers();

	/**
	 * Heavy, looks across hierarchy. Try getOwnFields() & PhpClassHierarchyUtils.processFields(), findFieldByName
	 */
	Collection<Field> getFields();

	/**
	 * Try PhpClassHierarchyUtils.processFields(), findFieldByName
	 */
	Field[] getOwnFields();

	MultiMap<CharSequence, Field> getOwnFieldMap();

	/**
	 * Heavy, looks across hierarchy. Result order unpredicted.
	 * Try getOwnMethods(), PhpClassHierarchyUtils.processMethods(), findMethodByName
	 */
	Collection<Method> getMethods();

	/**
	 * Try PhpClassHierarchyUtils.processMethods(), findMethodByName
	 *
	 * @return only methods from the current class without traversing supers
	 * <p>
	 * See {@link PhpClass#getOwnMethodsMap()}
	 */
	Method[] getOwnMethods();

	/**
	 * @return only methods from the current class without traversing supers
	 * <br/><br/>
	 * The real methods will be returned first then @methods.
	 * If both exist, @method is rudimentary and must be shown as a duplicate declaration. Resolve, completion, and refactorings must choose the
	 * real method because it is used in the code and @method is a descriptive annotation for magic methods (for example, __call).
	 */
	MultiMap<CharSequence, Method> getOwnMethodsMap();

	boolean hasOwnStaticMembers();

	boolean hasStaticMembers();

	@Nullable
	Method getConstructor();

	@Nullable
	Method findMethodByName(@Nullable CharSequence name);

	@Nullable
	Method findOwnMethodByName(@Nullable CharSequence name);

	@Nullable
	Field findFieldByName(@Nullable CharSequence name, boolean findConstant);

	@Nullable
	Field findOwnFieldByName(@Nullable CharSequence name, boolean findConstant);

	boolean hasMethodTags();

	boolean hasPropertyTags();

	boolean hasConstructorFields();

	@Nullable
	Method getOwnConstructor();

	boolean isTrait();

	List<PhpTraitUseRule> getTraitUseRules();

	@Nonnull
	String getPresentableFQN();
}
