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
package com.jetbrains.php.lang.psi.resolve.types;

import com.jetbrains.php.PhpClassHierarchyUtils;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.application.ApplicationManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.HashingStrategy;
import consulo.util.collection.Sets;
import consulo.util.lang.StringUtil;
import consulo.util.lang.ref.Ref;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class PhpType
{
	private static final Logger LOG = Logger.getInstance(PhpType.class);

	public static final String PHPSTORM_HELPERS = "___PHPSTORM_HELPERS";
	public static final String _PHPSTORM_HELPERS_FQN = "\\___PHPSTORM_HELPERS";

	public static final String _OBJECT_FQN = "\\" + PHPSTORM_HELPERS + "\\object"; //TODO - USAGES?
	public static final String _PHPSTORM_HELPERS_STATIC = "\\" + PHPSTORM_HELPERS + "\\static";
	public static final String _PHPSTORM_HELPERS_$THIS = "\\" + PHPSTORM_HELPERS + "\\this";

	public static final String _OBJECT = "\\object";
	public static final String _MIXED = "\\mixed";
	public static final String _VOID = "\\void";
	public static final String _NULL = "\\null";
	public static final String _ARRAY = "\\array";
	public static final String _ITERABLE = "\\iterable";
	public static final String _INT = "\\int";
	public static final String _INTEGER = "\\integer";
	public static final String _BOOL = "\\bool";
	public static final String _BOOLEAN = "\\boolean";
	public static final String _TRUE = "\\true";
	public static final String _FALSE = "\\false";
	public static final String _STRING = "\\string";
	public static final String _FLOAT = "\\float";
	public static final String _DOUBLE = "\\double";
	public static final String _CLOSURE = "\\Closure";
	/**
	 * @deprecated use CALLABLE in our code, but doc literal should be OK
	 */
	@Deprecated
	@SuppressWarnings("DeprecatedIsStillUsed")
	public static final String _CALLBACK = "\\callback";
	public static final String _CALLABLE = "\\callable";
	public static final String _NUMBER = "\\number";
	public static final String _RESOURCE = "\\resource";
	public static final String _EXCEPTION = "\\Exception";
	public static final String _THROWABLE = "\\Throwable";

	//primitive types
	public static final PhpType EMPTY = builder().build();
	public static final PhpType MIXED = builder().add(_MIXED).build();
	public static final PhpType NULL = builder().add(_NULL).build();
	public static final PhpType STRING = builder().add(_STRING).build();
	public static final PhpType BOOLEAN = builder().add(_BOOL).build();
	public static final PhpType FALSE = builder().add(_FALSE).build();
	public static final PhpType TRUE = builder().add(_TRUE).build();
	public static final PhpType INT = builder().add(_INT).build();
	public static final PhpType FLOAT = builder().add(_FLOAT).build();
	public static final PhpType OBJECT = builder().add(_OBJECT).build();
	public static final PhpType CLOSURE = builder().add(_CLOSURE).build();
	public static final PhpType CALLABLE = builder().add(_CALLABLE).build();
	public static final PhpType RESOURCE = builder().add(_RESOURCE).build();
	public static final PhpType ARRAY = builder().add(_ARRAY).build();
	public static final PhpType ITERABLE = builder().add(_ITERABLE).build();

	//aliases
	public static final PhpType NUMBER = builder().add(_NUMBER).build();

	//fake types
	public static final PhpType VOID = builder().add(_VOID).build();

	//complex types
	public static final PhpType NUMERIC = builder().add(STRING).add(INT).build();
	public static final PhpType SCALAR = builder().add(INT).add(FLOAT).add(STRING).add(BOOLEAN).build();
	public static final PhpType FLOAT_INT = builder().add(FLOAT).add(INT).build();

	public static final PhpType UNSET = builder().add("unset").build();
	public static final PhpType STATIC = builder().add(PhpClass.STATIC).build();
	public static final PhpType EXCEPTION = builder().add(_EXCEPTION).build();
	public static final PhpType THROWABLE = builder().add(_THROWABLE).build();
	public static final PhpType $THIS = builder().add(Variable.$THIS).build();

	@Nullable
	Set<String> types;
	private boolean isComplete = true;
	private boolean dirty = false;
	String myStringResolved;
	String myString;


	public PhpType()
	{
	}

	@Nonnull
	public static PhpTypeBuilder builder()
	{
		return new PhpTypeBuilder();
	}

	@Nonnull
	public PhpType add(@Nullable String aClass)
	{
		if(aClass != null && aClass.length() > 0)
		{
			if(aClass.charAt(0) == '#')
			{
				isComplete = false;
			}
			if(isPrimitiveType(aClass) && !aClass.startsWith("\\"))
			{
				aClass = "\\" + aClass; //intern will help here
			}
			if(aClass.equalsIgnoreCase(_INTEGER))
			{
				aClass = _INT;
			}
			else if(aClass.equals(_STRING))
			{
				aClass = _STRING;
			}
			else if(aClass.equalsIgnoreCase(_ARRAY))
			{
				aClass = _ARRAY;
			}
			else if(aClass.equalsIgnoreCase(_BOOL))
			{
				aClass = _BOOL;
			}
			else if(aClass.equalsIgnoreCase(_MIXED))
			{
				aClass = _MIXED;
			}
			else if(aClass.equalsIgnoreCase(_BOOLEAN))
			{
				aClass = _BOOL;
			}
			else //noinspection deprecation
				if(aClass.equalsIgnoreCase(_CALLBACK))
				{
					aClass = _CALLABLE;
				}
				else if(aClass.equalsIgnoreCase(_ITERABLE))
				{
					aClass = _ITERABLE;
				}
				else if(aClass.equalsIgnoreCase(_DOUBLE))
				{
					aClass = _FLOAT;
				}
			if(types == null)
			{
				types = Sets.newHashSet(HashingStrategy.caseInsensitive());
			}
			if(types.size() > 50 && ApplicationManager.getApplication().isInternal())
			{
				LOG.warn("too much type variants: " + types);
			}
			else
			{
				types.add(aClass);
			}
			dirty = true;
		}
		return this;
	}

	@Nonnull
	public PhpType add(@Nullable PsiElement other)
	{
		if(other instanceof PhpTypedElement)
		{
			final PhpType type = ((PhpTypedElement) other).getType();
			add(type);
		}
		return this;
	}

	@Nonnull
	public PhpType add(PhpType type)
	{
		if(type != null && type.types != null && type.types.size() > 0)
		{
			try
			{
				isComplete &= type.isComplete;
				if(types == null)
				{
					types = type.types.size() < 2 ? Sets.newHashSet(type.types, HashingStrategy.caseInsensitive())
							: Sets.newHashSet(type.types, HashingStrategy.caseInsensitive());
				}
				else
				{
					types.addAll(type.types);
					if(types.size() > 50 && ApplicationManager.getApplication().isInternal())
					{
						LOG.warn("too much type variants: " + types);
					}
				}
				dirty = true;
			}
			catch(NoSuchElementException e)
			{
				throw new RuntimeException("NSEE @" + type.types, e);
			}
		}
		return this;
	}

	public int size()
	{
		return types != null ? types.size() : 0;
	}

	public boolean isUndefined()
	{
		if(types == null)
		{
			return true;
		}
		if(types.size() == 0)
		{
			return true;
		}
		if(types.size() == 1 && (types.contains(_MIXED) || types.contains(_OBJECT_FQN)))
		{
			return true;
		}
		return false;
	}

	/**
	 * @return UNORDERED set of contained type literals
	 */
	@Nonnull
	public Set<String> getTypes()
	{
		if(types != null)
		{
			return types;
		}
		else
		{
			return Collections.emptySet();
		}
	}

	/**
	 * @return more costly, ORDERED set of contained type literals (cached), should be avoided
	 * TODO - factor usages out?
	 */
	@Nonnull
	public Set<String> getTypesSorted()
	{
		if(types != null)
		{
			sortIfNeeded();
			assert types != null;
			return types;
		}
		else
		{
			return Collections.emptySet();
		}
	}

	public String toString()
	{
		if(types == null)
		{
			return "";
		}
		if(!dirty && myString != null)
		{
			return myString;
		}
		final StringBuilder builder = new StringBuilder();
		for(String type : getTypesSorted())
		{
			if(!type.startsWith("?"))
			{
				type = toString(type);
				builder.append(type).append('|');
			}
		}
		if(builder.length() > 1)
		{
			builder.setLength(builder.length() - 1);
		}
		if(!isComplete)
		{
			builder.append("|?");
		}
		myString = builder.toString();
		return myString;
	}

	/**
	 * @param type string to trim-root-ns-if-required
	 * @return trimmed string
	 */
	public static String toString(String type)
	{
		return (isPrimitiveType(StringUtil.trimEnd(type, "[]")) && type.startsWith("\\")) ? type.substring(1) : type;
	}

	private void sortIfNeeded()
	{
		if(types != null && !(types instanceof SortedSet))
		{
			final SortedSet<String> sorted = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			sorted.addAll(types);
			types = sorted;
		}
	}

	/**
	 * NOTE: apply to .global() type only!
	 *
	 * @return presentable name
	 */
	public String toStringResolved()
	{
		//PhpContractUtil.assertCompleteType(this);
		if(!dirty && myStringResolved != null)
		{
			return myStringResolved;
		}
		myStringResolved = toStringRelativized(null);
		return myStringResolved;
	}

	/**
	 * NOTE: apply to .global() type only!
	 *
	 * @return relativized presentable name
	 */
	public String toStringRelativized(@Nullable final String currentNamespaceName)
	{
		//PhpContractUtil.assertCompleteType(this);
		if(types == null)
		{
			return "";
		}
		final StringBuilder builder = new StringBuilder();
		for(String type : getTypesSorted())
		{
			if(!type.startsWith("?"))
			{
				type = toString(type);
				if(currentNamespaceName != null && type.startsWith(currentNamespaceName))
				{
					type = type.substring(currentNamespaceName.length());
				}
				builder.append(type).append('|');
			}
		}
		if(builder.length() > 1)
		{
			builder.setLength(builder.length() - 1);
		}
		if(!isComplete)
		{
			builder.append("|?");
		}
		return builder.toString();
	}


	public boolean isConvertibleFrom(@Nonnull PhpType otherType, @Nonnull PhpIndex index)
	{
		//System.out.println(this + " isConvertibleFrom " + otherType);
		if(isUndefined() || otherType.isUndefined())
		{
			return true;
		}
		if(this.equals(NULL))
		{
			return true;
		}
		Set<String> otherTypes = otherType.types;
		if(otherTypes == null)
		{
			return types == null;
		}
		for(String other : otherTypes)
		{
			if(types != null)
			{
				for(final String my : types)
				{
					//System.out.println("\tmy = " + my + " other = " + other);
					if(my.equalsIgnoreCase(other)
							|| other.equalsIgnoreCase(_MIXED) || my.equalsIgnoreCase(_MIXED)
							//|| my.equalsIgnoreCase(_OBJECT) //TODO
							|| my.startsWith("?") || other.startsWith("?")
							|| isPluralType(my) && other.equalsIgnoreCase(_ARRAY)
							|| isPluralType(other) && (my.equalsIgnoreCase(_ARRAY) || my.equalsIgnoreCase(_ITERABLE))
							|| (my.equalsIgnoreCase(_STRING) && !other.equalsIgnoreCase(_ARRAY))
							|| (other.equalsIgnoreCase(_STRING) && my.equalsIgnoreCase(_CALLABLE))
							|| (other.equalsIgnoreCase(_ARRAY) && my.equalsIgnoreCase(_CALLABLE))
							|| (other.equalsIgnoreCase(_STRING) && my.equalsIgnoreCase(_INT))
							|| (other.equalsIgnoreCase(_STRING) && my.equalsIgnoreCase(_FLOAT))
							|| (other.equalsIgnoreCase(_STRING) && my.equalsIgnoreCase(_BOOL))
							|| my.equalsIgnoreCase(_ITERABLE) && other.equalsIgnoreCase(_ARRAY)
							|| isBidi(my, other, _TRUE, _BOOL)
							|| isBidi(my, other, _FALSE, _BOOL)
							|| isBidi(my, other, _INT, _FLOAT)
							|| isBidi(my, other, _BOOL, _INT)
							|| isBidi(my, other, _BOOL, _FLOAT)
							|| isBidi(my, other, _NUMBER, _FLOAT)
							|| isBidi(my, other, _NUMBER, _INT)
							|| isBidi(my, other, _OBJECT, "\\stdClass")
					)
					{
						return true;
					}
					if(!my.equalsIgnoreCase(_CALLABLE) && !other.equalsIgnoreCase(_CALLABLE) && isPrimitiveType(my) && isPrimitiveType(other))
					{
						continue;
					}
					if(findSuper(my, other, index))
					{
						return true;
					}
					if(!isPrimitiveType(other) && _OBJECT.equalsIgnoreCase(my))
					{
						return true;
					}
					if(!isPluralPrimitiveType(other) && my.endsWith("[]") && _OBJECT.equalsIgnoreCase(my.substring(0, my.length() - 2)))
					{
						return true;
					}
					if(my.equalsIgnoreCase(_CALLABLE) && checkInvoke(other, index)
							|| other.equalsIgnoreCase(_CALLABLE) && checkInvoke(my, index))
					{
						return true;
					}
				}
			}
		}
		//System.out.println("\tisConvertibleFrom == false");
		return false;
	}

	private static boolean isBidi(String my, String other, String type1, String type2)
	{
		return (my.equalsIgnoreCase(type1) && other.equalsIgnoreCase(type2)) ||
				(other.equalsIgnoreCase(type1) && my.equalsIgnoreCase(type2));
	}

	@Nonnull
	public PhpType filterUnknown()
	{
		return filterOut(s -> s.startsWith("#") || s.startsWith("?"));
	}

	@Nonnull
	public PhpType filterPrimitives()
	{
		return filterOut(PhpType::isPrimitiveType);
	}

	public boolean hasUnknown()
	{
		if(types == null)
		{
			return false;
		}
		for(String type : types)
		{
			if(StringUtil.startsWith(type, "?"))
			{
				return true;
			}
		}
		return false;
	}

	private static boolean checkInvoke(@Nonnull String some, @Nonnull PhpIndex index)
	{
		final Collection<PhpClass> candidates = index.getAnyByFQN(some);
		for(PhpClass candidate : candidates)
		{
			if(candidate.findMethodByName(PhpClass.INVOKE) != null)
			{
				return true;
			}
		}
		return false;
	}

	//TODO move to hierarchy utils
	public static boolean findSuper(@Nonnull String my, @Nullable String other, @Nonnull PhpIndex index)
	{
		if(other == null)
		{
			return false;
		}
		if(my.endsWith("[]") != other.endsWith("[]"))
		{
			return false;
		}
		my = StringUtil.trimEnd(my, "[]");
		other = StringUtil.trimEnd(other, "[]");
		if(!my.startsWith("\\"))
		{
			my = "\\" + my;
		}
		if(!other.startsWith("\\"))
		{
			other = "\\" + other;
		}
		if(my.equalsIgnoreCase(other))
		{
			return true;
		}
		Collection<PhpClass> mes = index.getAnyByFQN(my);
		Ref<Boolean> result = new Ref<>(false);
		return index.getAnyByFQN(other).stream().anyMatch(phpClass -> {
			PhpClassHierarchyUtils.processSupers(phpClass, true, true, aSuper -> {
				for(final PhpClass me : mes)
				{
					if(PhpClassHierarchyUtils.classesEqual(me, aSuper))
					{
						result.set(true);
						break;
					}
				}
				return !result.get();
			});
			return result.get();
		});
	}

	/**
	 * @return type info is complete from all possible sources/is incomplete - non local stuff omitted. Defaults to true.
	 */
	public boolean isComplete()
	{
		return isComplete;
	}

	/**
	 * @param context - important for resolution of SELF, THIS, STATIC
	 * @return resolved type
	 */
	public PhpType globalLocationAware(@Nonnull PsiElement context)
	{
		//System.out.println("globalLocationAware context=" + context + " [" + (context instanceof PhpNamedElement ? ((PhpNamedElement)context).getFQN() : context.getText()) + "]," +
		//                   " this=[" + this + "] ");
		try
		{
			return PhpIndex.getInstance(context.getProject()).completeThis(this, get$ThisClassFQN(context), new HashSet<>());
		}
		catch(StackOverflowError e)
		{
			throw new RuntimeException("SOE in PhpType.globalLocationAware at " + context.getParent().getText() + " in " + context.getContainingFile().getName() + " type=" + this.toString(), e);
		}
	}

	@Nullable
	private static String get$ThisClassFQN(@Nullable PsiElement context)
	{
		String result = null;
		if(context instanceof PhpClassMember)
		{
			final PhpClass aClass = ((PhpClassMember) context).getContainingClass();
			if(aClass != null)
			{
				result = aClass.getFQN();
			}
		}
		else
		{
			//note - its important no NOT materialise tree here when possible!
			final PhpClass aClass = PsiTreeUtil.getStubOrPsiParentOfType(context, PhpClass.class);
			if(aClass != null)
			{
				result = aClass.getFQN();
			}
		}
		return result;
	}

	public PhpType global(@Nonnull Project p)
	{
		return PhpIndex.getInstance(p).completeType(p, this, null);
	}

	public boolean isEmpty()
	{
		return types == null || types.isEmpty();
	}

	@Nonnull
	public PhpType elementType()
	{
		final PhpType elementType = new PhpType();
		if(types != null)
		{
			for(String type : types)
			{
				if(type.equalsIgnoreCase(_ARRAY))
				{
					elementType.add(MIXED);
				}
				else if(isPluralType(type))
				{
					elementType.add(type.substring(0, type.length() - 2));
				}
				else if(!isPrimitiveType(type))
				{
					elementType.add(PhpTypeSignatureKey.ARRAY_ELEMENT.sign(PhpTypeSignatureKey.CLASS.signIfUnsigned(type)));
				}
			}
		}
		if(elementType.isEmpty())
		{
			elementType.add(_MIXED);
		}
		return elementType;
	}

	@Nonnull
	public PhpType pluralise()
	{
		final PhpType elementType = new PhpType();
		if(types != null)
		{
			for(String type : types)
			{
				elementType.add(type + "[]");
			}
		}
		return elementType;
	}

	@Nonnull
	public PhpType unpluralize()
	{
		if(ContainerUtil.isEmpty(types))
		{
			return EMPTY;
		}
		final PhpType unpluralized = new PhpType();
		for(final String type : types)
		{
			unpluralized.add(_ARRAY.equalsIgnoreCase(type) ? _MIXED : StringUtil.trimEnd(type, "[]"));
		}
		return unpluralized;
	}

	public static boolean isPrimitiveType(@Nullable String type)
	{
		if(type == null)
		{
			return true;
		}
		if(type.length() < 3 || type.length() > 11)
		{
			return false;
		}
		if(type.charAt(0) == '#')
		{
			return false;
		}
		if(!type.startsWith("\\"))
		{
			type = "\\" + type;
		}
		//noinspection deprecation
		return isNotExtendablePrimitiveType(type) ||
				isArray(type) ||
				_OBJECT.equalsIgnoreCase(type) ||
				_CALLABLE.equalsIgnoreCase(type) ||
				_CALLBACK.equalsIgnoreCase(type) ||
				_ITERABLE.equalsIgnoreCase(type);
	}

	/**
	 * Some primitive types are not exclusive, e.g. variable can be an {@code array} and {@code ArrayAccess} class at the same time
	 * or {@code callable} and any other class type with implemented {@code __invoke} method. <br/><br/>
	 * <p>
	 * This method is indented to check if type consists of exclusive primitive types,
	 * i.e. types that can't be extended by class types
	 *
	 * @return {@code true} if type consists of exclusive primitive types, {@code false} otherwise
	 */
	public boolean isNotExtendablePrimitiveType()
	{
		if(isEmpty())
		{
			return false;
		}
		return getTypes().stream().allMatch(PhpType::isNotExtendablePrimitiveType);
	}

	public static boolean isNotExtendablePrimitiveType(@Nullable String type)
	{
		if(type == null)
		{
			return true;
		}
		if(type.length() < 3 || type.length() > 11)
		{
			return false;
		}
		if(type.charAt(0) == '#')
		{
			return false;
		}
		if(!type.startsWith("\\"))
		{
			type = "\\" + type;
		}
		return
				_MIXED.equalsIgnoreCase(type) ||
						_STRING.equalsIgnoreCase(type) ||
						_INT.equalsIgnoreCase(type) ||
						_INTEGER.equalsIgnoreCase(type) ||
						_NUMBER.equalsIgnoreCase(type) ||
						_BOOL.equalsIgnoreCase(type) ||
						_BOOLEAN.equalsIgnoreCase(type) ||
						_TRUE.equalsIgnoreCase(type) ||
						_FALSE.equalsIgnoreCase(type) ||
						_FLOAT.equalsIgnoreCase(type) ||
						_NULL.equalsIgnoreCase(type) ||
						_RESOURCE.equalsIgnoreCase(type) ||
						_VOID.equalsIgnoreCase(type) ||
						_DOUBLE.equalsIgnoreCase(type)
				;
	}

	public static boolean isArray(@Nonnull final String type)
	{
		return _ARRAY.equals(type);
	}

	public static boolean isString(@Nonnull final String type)
	{
		return _STRING.equals(type);
	}

	public static boolean isObject(@Nonnull final String type)
	{
		return _OBJECT.equals(type);
	}

	public static boolean isMixedType(@Nonnull final String type)
	{
		return _MIXED.equals(type);
	}

	public static boolean isCallableType(@Nonnull final String type)
	{
		return _CALLABLE.equals(type);
	}

	public static boolean isPluralType(@Nonnull final String type)
	{
		return type.endsWith("[]");
	}

	public static boolean isPluralPrimitiveType(@Nonnull final String type)
	{
		return type.endsWith("[]") && isPrimitiveType(type.substring(0, type.length() - 2));
	}

	public static boolean isAnonymousClass(@Nonnull String type)
	{
		return StringUtil.startsWith(type, PhpClass.ANONYMOUS);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
		{
			return true;
		}
		if(!(o instanceof PhpType))
		{
			return false;
		}

		PhpType phpType = (PhpType) o;

		if(isComplete != phpType.isComplete)
		{
			return false;
		}
		if(types != null ? !types.equals(phpType.types) : phpType.types != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = types != null ? computeHashCode(types) : 0;
		result = 31 * result + (isComplete ? 1 : 0);
		return result;
	}

	private static int computeHashCode(@Nonnull final Set<String> types)
	{
		int result = 0;
		for(final String type : types)
		{
			result += HashingStrategy.caseInsensitive().hashCode(type); // the same as THashSet does
		}
		return result;
	}

	public static boolean isUnresolved(@Nonnull final String type)
	{
		return type.indexOf('#') != -1;
	}

	public static boolean isNull(@Nonnull final String type)
	{
		return _NULL.equals(type);
	}

	private static boolean isScalar(@Nonnull final String type)
	{
		return _BOOL.equals(type) ||
				_BOOLEAN.equals(type) ||
				_FLOAT.equals(type) ||
				_STRING.equals(type) ||
				_INT.equals(type) ||
				_INTEGER.equals(type);
	}

	public static boolean isScalar(@Nonnull final PhpType type, @Nonnull final Project project)
	{
		final PhpType completedType = type.global(project);
		Set<String> types = completedType.types;
		if(types == null)
		{
			return true;
		}
		for(String curType : types)
		{
			if(!isScalar(curType))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean intersects(@Nonnull PhpType phpType1, @Nonnull PhpType phpType2)
	{
		//PhpContractUtil.assertCompleteType(phpType1, phpType2);
		final Set<String> phpTypeSet1 = phpType1.types;
		final Set<String> phpTypeSet2 = phpType2.types;
		if(phpTypeSet1 == null || phpTypeSet2 == null)
		{
			return phpTypeSet1 == phpTypeSet2;
		}
		for(String type1 : phpTypeSet1)
		{
			if(phpTypeSet2.contains(type1))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isSubType(@Nonnull PhpType phpType1, @Nonnull PhpType phpType2)
	{
		//PhpContractUtil.assertCompleteType(phpType1, phpType2);
		final Set<String> typeSet1 = phpType1.types;
		if(typeSet1 == null || typeSet1.size() == 0)
		{
			return false;
		}
		final Set<String> typeSet2 = phpType2.types;
		if(typeSet2 == null || typeSet2.size() == 0)
		{
			return false;
		}
		for(String type1 : typeSet1)
		{
			if(!typeSet2.contains(type1))
			{
				return false;
			}
		}
		return true;
	}

	@Nonnull
	public static PhpType and(@Nonnull PhpType phpType1, @Nonnull PhpType phpType2)
	{
		final Set<String> phpTypeSet1 = phpType1.types;
		final Set<String> phpTypeSet2 = phpType2.types;
		if(phpTypeSet1 == null || phpTypeSet2 == null)
		{
			return EMPTY;
		}
		final PhpType phpType = new PhpType();
		for(String type1 : phpTypeSet1)
		{
			if(phpTypeSet2.contains(type1))
			{
				phpType.add(type1);
			}
		}
		return phpType;
	}

	@Nonnull
	public static PhpType or(@Nonnull PhpType phpType1, @Nonnull PhpType phpType2)
	{
		return new PhpType().add(phpType1).add(phpType2);
	}

	@Nonnull
	public PhpType filterNull()
	{
		return filterOut(PhpType::isNull);
	}

	@Nonnull
	public PhpType filterMixed()
	{
		return filterOut(PhpType::isMixedType);
	}

	@Nonnull
	public PhpType filterPlurals()
	{
		return filterOut(PhpType::isPluralType);
	}

	@Nonnull
	public PhpType filterOut(@Nonnull Predicate<String> typeExcludePredicate)
	{
		if(types == null)
		{
			return EMPTY;
		}
		final PhpType phpType = new PhpType();
		for(String type : types)
		{
			if(!typeExcludePredicate.test(type))
			{
				phpType.add(type);
			}
		}
		return phpType;
	}

	@Nonnull
	public PhpType filter(@Nonnull final PhpType sieve)
	{
		if(ContainerUtil.isEmpty(types))
		{
			return EMPTY;
		}
		final Set<String> sieveTypes = sieve.types;
		if(ContainerUtil.isEmpty(sieveTypes))
		{
			return new PhpType().add(this);
		}
		final PhpType phpType = new PhpType();
		if(sieveTypes.size() == 1)
		{
			final String sieveType = ContainerUtil.getFirstItem(sieveTypes);
			assert sieveType != null;
			types.stream().filter(type -> !sieveType.equals(type)).forEach(phpType::add);
		}
		else
		{
			types.stream().filter(type -> !sieveTypes.contains(type)).forEach(phpType::add);
		}
		return phpType;
	}

	private static class ImmutablePhpType extends PhpType
	{

		@Nonnull
		@Override
		public PhpType add(@Nullable String aClass)
		{
			throw getException();
		}

		@Nonnull
		@Override
		public PhpType add(@Nullable PsiElement other)
		{
			throw getException();
		}

		@Nonnull
		@Override
		public PhpType add(@Nullable PhpType type)
		{
			throw getException();
		}

		@Nonnull
		private static RuntimeException getException()
		{
			return new UnsupportedOperationException("This PHP type is immutable");
		}
	}

	public static class PhpTypeBuilder
	{

		private final PhpType temp = new PhpType();

		@Nonnull
		public PhpTypeBuilder add(@Nullable final String aClass)
		{
			temp.add(aClass);
			return this;
		}

		@Nonnull
		public PhpTypeBuilder add(@Nullable final PsiElement other)
		{
			temp.add(other);
			return this;
		}

		@Nonnull
		public PhpTypeBuilder add(@Nullable final PhpType type)
		{
			temp.add(type);
			return this;
		}

		@Nonnull
		public PhpTypeBuilder merge(@Nonnull final PhpTypeBuilder builder)
		{
			temp.add(builder.temp);
			return this;
		}

		@Nonnull
		public PhpType build()
		{
			final PhpType type = new ImmutablePhpType();
			if(temp.types != null)
			{
				switch(temp.types.size())
				{
					case 0:
						type.types = Collections.emptySet();
						break;
					case 1:
						type.types = Collections.singleton(ContainerUtil.getFirstItem(temp.types));
						break;
					default:
						type.types = temp.types;
						type.dirty = temp.dirty;
				}
			}
			type.isComplete = temp.isComplete;
			return type;
		}
	}
}
