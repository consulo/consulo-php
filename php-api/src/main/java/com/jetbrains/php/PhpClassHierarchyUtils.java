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
import consulo.application.util.function.Processor;
import consulo.language.psi.PsiElement;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;
import consulo.util.lang.function.Condition;
import consulo.util.lang.ref.Ref;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class PhpClassHierarchyUtils
{
	private static final NextElementsAppender<PhpClass> SUPER_CLASS_APPENDER_NOT_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final PhpClass superClass = element.getSuperClass();
			if(superClass != null)
			{
				phpClasses.add(superClass);
			}
		}
	};
	private static final NextElementsAppender<PhpClass> SUPER_CLASS_APPENDER_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final String superFQN = element.getSuperFQN();
			final Project project = element.getProject();
			final PhpIndex phpIndex = PhpIndex.getInstance(project);
			phpClasses.addAll(phpIndex.getClassesByFQN(superFQN));
		}
	};
	private static final NextElementsAppender<PhpClass> SUPER_INTERFACE_APPENDER_NOT_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
			final String[] interfaceNames = element.getInterfaceNames();
			for(String interfaceName : interfaceNames)
			{
				final Collection<PhpClass> interfacesByFQN = phpIndex.getInterfacesByFQN(interfaceName);
				if(interfacesByFQN.size() == 1)
				{
					phpClasses.add(interfacesByFQN.iterator().next());
				}
			}
		}
	};
	private static final NextElementsAppender<PhpClass> SUPER_INTERFACE_APPENDER_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
			final String[] interfaceNames = element.getInterfaceNames();
			for(String interfaceName : interfaceNames)
			{
				phpClasses.addAll(phpIndex.getInterfacesByFQN(interfaceName));
			}
		}
	};

	private static final NextElementsAppender<PhpClass> SUPER_TRAIT_APPENDER_NOT_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
			final String[] names = element.getTraitNames();
			for(String name : names)
			{
				final Collection<PhpClass> classes = phpIndex.getTraitsByFQN(name);
				if(classes.size() == 1)
				{
					phpClasses.add(classes.iterator().next());
				}
			}
			ContainerUtil.addAll(phpClasses, element.getMixins());
		}
	};
	private static final NextElementsAppender<PhpClass> SUPER_TRAIT_APPENDER_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
			final String[] names = element.getTraitNames();
			for(String name : names)
			{
				phpClasses.addAll(phpIndex.getTraitsByFQN(name));
			}
			ContainerUtil.addAll(phpClasses, element.getMixins());
		}
	};

	private static final NextElementsAppender<PhpClass> SUPER_APPENDER_NOT_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			SUPER_TRAIT_APPENDER_NOT_AMBIGUITY.appendNextElements(element, phpClasses);
			SUPER_CLASS_APPENDER_NOT_AMBIGUITY.appendNextElements(element, phpClasses);
			SUPER_INTERFACE_APPENDER_NOT_AMBIGUITY.appendNextElements(element, phpClasses);
		}
	};
	private static final NextElementsAppender<PhpClass> SUPER_APPENDER_AMBIGUITY = new NextElementsAppender<PhpClass>()
	{
		@Override
		public void appendNextElements(@Nonnull PhpClass element, @Nonnull Collection<PhpClass> phpClasses)
		{
			SUPER_TRAIT_APPENDER_AMBIGUITY.appendNextElements(element, phpClasses);
			SUPER_CLASS_APPENDER_AMBIGUITY.appendNextElements(element, phpClasses);
			SUPER_INTERFACE_APPENDER_AMBIGUITY.appendNextElements(element, phpClasses);
		}
	};

	private PhpClassHierarchyUtils()
	{
	}

	public interface NextElementsAppender<T>
	{

		void appendNextElements(@Nonnull T element, @Nonnull Collection<T> collection);

	}

	public static <T> void process(@Nonnull final T initialElement,
								   boolean processSelf,
								   @Nonnull final Processor<? super T> processor,
								   @Nonnull final NextElementsAppender<T> appender)
	{
		final Set<T> processed = new HashSet<>();
		final Deque<T> processorPool = new ArrayDeque<>();
		if(processSelf)
		{
			processorPool.add(initialElement);
		}
		else
		{
			appender.appendNextElements(initialElement, processorPool);
		}
		while(processorPool.size() > 0)
		{
			final T first = processorPool.getFirst();
			if(processed.add(first))
			{
				if(processor.process(first))
				{
					appender.appendNextElements(first, processorPool);
				}
				else
				{
					return;
				}
			}
			processorPool.removeFirst();
		}
	}

	public static void processSuperClasses(@Nonnull final PhpClass clazz,
										   boolean processSelf,
										   final boolean allowAmbiguity,
										   @Nonnull final Processor<? super PhpClass> processor)
	{
		process(clazz, processSelf, processor, allowAmbiguity ? SUPER_CLASS_APPENDER_AMBIGUITY : SUPER_CLASS_APPENDER_NOT_AMBIGUITY);
	}

	public static void processSuperInterfaces(@Nonnull final PhpClass clazz,
											  boolean processSelf,
											  final boolean allowAmbiguity,
											  @Nonnull final Processor<? super PhpClass> processor)
	{
		process(clazz, processSelf, processor, allowAmbiguity ? SUPER_INTERFACE_APPENDER_AMBIGUITY : SUPER_INTERFACE_APPENDER_NOT_AMBIGUITY);
	}

	public static void processSupers(@Nonnull final PhpClass clazz,
									 boolean processSelf,
									 final boolean allowAmbiguity,
									 @Nonnull final Processor<? super PhpClass> processor)
	{
		process(clazz, processSelf, processor, allowAmbiguity ? SUPER_APPENDER_AMBIGUITY : SUPER_APPENDER_NOT_AMBIGUITY);
	}

	public static boolean isSuperClass(@Nonnull final PhpClass superClass, @Nonnull final PhpClass subClass, boolean allowAmbiguity)
	{
		final Ref<Boolean> isSuperClassRef = new Ref<>(false);
		processSuperClasses(subClass, false, allowAmbiguity, curClass -> {
			if(classesEqual(curClass, superClass))
			{
				isSuperClassRef.set(true);
			}
			return !isSuperClassRef.get();
		});
		return isSuperClassRef.get();
	}

	public static boolean processMethods(PhpClass phpClass,
										 PhpClass initialClass,
										 @Nonnull HierarchyMethodProcessor methodProcessor,
										 boolean processOwnMembersOnly)
	{
		return processMembersInternal(phpClass, new HashSet<>(), null, initialClass, methodProcessor, Method.INSTANCEOF,
				processOwnMembersOnly);
	}

	public static boolean processFields(PhpClass phpClass,
										PhpClass initialClass,
										@Nonnull HierarchyFieldProcessor fieldProcessor,
										boolean processOwnMembersOnly)
	{
		return processMembersInternal(phpClass, new HashSet<>(), null, initialClass, fieldProcessor, Field.INSTANCEOF, processOwnMembersOnly);
	}

	private static boolean processMembersInternal(@Nullable final PhpClass phpClass,
												  @Nonnull Set<? super PhpClass> visited,
												  @Nullable Map<String, PhpTraitUseRule> conflictResolution, final PhpClass initialClass,
												  @Nonnull final HierarchyMemberProcessor processor, Condition<PsiElement> condition, boolean processOwnMembersOnly)
	{
		if(phpClass == null || !visited.add(phpClass))
		{
			return true;
		}
		//System.out.println(phpClass.getFQN() + " conflictResolution = " + conflictResolution);
		boolean processMethods = condition == Method.INSTANCEOF;
		for(PhpClassMember member : processMethods ? phpClass.getOwnMethodsMap().values() : phpClass.getOwnFieldMap().values())
		{
			PhpTraitUseRule rule = conflictResolution != null ? conflictResolution.get(member.getFQN()) : null;
			//System.out.println("member = " + member.getFQN());
			//System.out.println("rule = " + (rule!=null?rule.getText():"null") );
			if(rule != null && rule.getAlias() == null && processMethods)
			{
				//System.out.println("*** OVERRIDE");
			}
			else
			{
				if(processMethods ?
						!((HierarchyMethodProcessor) processor).process((Method) member, phpClass, initialClass) :
						!((HierarchyFieldProcessor) processor).process((Field) member, phpClass, initialClass)
				)
				{
					return false;
				}
			}
		}

		if(!processOwnMembersOnly && phpClass.hasTraitUses())
		{
			final Map<String, PhpTraitUseRule> newConflictResolution = new HashMap<>();
			List<PhpTraitUseRule> rules = phpClass.getTraitUseRules();
			for(PhpTraitUseRule rule : rules)
			{
				if(!rule.isInsteadOf())
				{
					if(processMethods)
					{
						List<Method> methods = rule.getMethods();
						for(Method member : methods)
						{
							if(member.isValid() && !((HierarchyMethodProcessor) processor).process(member, phpClass, initialClass))
							{
								return false;
							}
						}
					}
				}
				else
				{
					MethodReference o = rule.getOriginalReference();
					assert o != null;
					ClassReference override = rule.getOverride();
					assert override != null;
					newConflictResolution.put(o.getNamespaceName() + override.getName() + "." + o.getName(), rule);
				}
			}

			// @link http://www.php.net/manual/en/language.oop5.traits.php
			// An inherited member from a base class is overridden by a member inserted by a Trait.
			// The precedence order is that members from the current class override Trait methods, which in return override inherited methods.
			for(PhpClass trait : phpClass.getTraits())
			{
				if(!processMembersInternal(trait, visited, newConflictResolution, initialClass, processor, condition, processOwnMembersOnly))
				{
					return false;
				}
			}
		}

		if(!processOwnMembersOnly)
		{
			if(!processMembersInternal(phpClass.getSuperClass(), visited, null, initialClass, processor, condition, processOwnMembersOnly))
			{
				return false;
			}
			for(PhpClass phpInterface : phpClass.getImplementedInterfaces())
			{
				if(!processMembersInternal(phpInterface, visited, null, initialClass, processor, condition, processOwnMembersOnly))
				{
					return false;
				}
			}
			for(PhpClass mixin : phpClass.getMixins())
			{
				if(!processMembersInternal(mixin, visited, null, initialClass, processor, condition, processOwnMembersOnly))
				{
					return false;
				}
			}
		}

		return true;
	}

	public static boolean processOverridingMembers(PhpClassMember member, PhpIndex phpIndex, HierarchyClassMemberProcessor memberProcessor)
	{
		if(member instanceof Method)
		{
			if(!methodCanHaveOverride((Method) member))
			{
				return true;
			}
			final String methodName = member.getName();
			PhpClass me = member.getContainingClass();
			if(me != null)
			{
				final Collection<PhpClass> allSubclasses = new ArrayList<>(phpIndex.getAllSubclasses(me.getFQN()));
				if(me.isTrait())
				{
					allSubclasses.addAll(phpIndex.getTraitUsages(me));
				}
				for(PhpClass myChild : allSubclasses)
				{
					if(myChild != null)
					{
						Method method = myChild.findOwnMethodByName(methodName);
						if(method != null)
						{
							if(!memberProcessor.process(method, myChild, me))
							{
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		else /*if (member instanceof Field)*/
		{
			final PhpClass me = member.getContainingClass();
			if(me != null)
			{
				final String fieldName = member.getName();
				final Collection<PhpClass> allSubclasses = phpIndex.getAllSubclasses(me.getFQN());
				for(PhpClass myChild : allSubclasses)
				{
					boolean isConstant = ((Field) member).isConstant();
					final Field field = myChild.findOwnFieldByName(fieldName, isConstant);
					if(field != null)
					{
						if(!memberProcessor.process(field, myChild, me))
						{
							return false;
						}
					}
				}
				return true;
			}
			return false;
		}
	}


	public static boolean methodCanHaveOverride(@Nonnull Method element)
	{
		return element.getContainingClass() != null && !element.getAccess().isPrivate();
	}

	public static boolean processSuperMembers(@Nonnull PhpClassMember member, @Nonnull final HierarchyClassMemberProcessor memberProcessor)
	{
		final HashSet<PhpClass> processed = new HashSet<>();
		// TODO IMPROVE - potentially expensive call: leads to full PSI tree mat. on PHPDoc @property & @method
		final String memberName = member.getName();
		boolean fieldIsConstant = (member instanceof Field) && ((Field) member).isConstant();
		final Queue<PhpClassMember> members = new ArrayDeque<>();
		members.add(member);
		while(!members.isEmpty())
		{
			// TODO IMPROVE - potentially expensive call: leads to full PSI tree mat. on PHPDoc @property & @method
			final PhpClass me = members.poll().getContainingClass();
			if(me == null)
			{
				return false;
			}
			Collection<PhpClass> parents = getImmediateParents(me);
			for(PhpClass mySuper : parents)
			{
				if(!processed.add(mySuper))
				{
					continue;
				}
				PhpClassMember foundMember =
						member instanceof Field ? mySuper.findFieldByName(memberName, fieldIsConstant) : mySuper.findMethodByName(memberName);
				if(foundMember != null && !foundMember.getModifier().isPrivate())
				{
					members.add(foundMember);
					if(!memberProcessor.process(foundMember, me, mySuper))
					{
						return false;
					}
				}
			}
		}
		return true;
	}


	public static List<PhpClass> getImmediateParents(PhpClass me)
	{
		List<PhpClass> parents = new ArrayList<>();
		PhpClass superClass = me.getSuperClass();
		if(superClass != null)
		{
			parents.add(superClass);
		}
		ContainerUtil.addAll(parents, me.getImplementedInterfaces());
		ContainerUtil.addAll(parents, me.getTraits());
		return parents;
	}

	@FunctionalInterface
	public interface HierarchyClassMemberProcessor extends HierarchyMemberProcessor
	{
		boolean process(PhpClassMember classMember, PhpClass subClass, PhpClass baseClass);
	}

	@FunctionalInterface
	public interface HierarchyMethodProcessor extends HierarchyMemberProcessor
	{
		boolean process(Method method, PhpClass subClass, PhpClass baseClass);
	}

	@FunctionalInterface
	public interface HierarchyFieldProcessor extends HierarchyMemberProcessor
	{
		boolean process(Field field, PhpClass subClass, PhpClass baseClass);
	}

	public interface HierarchyMemberProcessor
	{
	}

	public static Collection<PhpClass> getDirectSubclasses(@Nonnull PhpClass psiClass)
	{
		if(psiClass.isFinal())
		{
			return Collections.emptyList();
		}

		final PhpIndex phpIndex = PhpIndex.getInstance(psiClass.getProject());
		return phpIndex.getDirectSubclasses(psiClass.getFQN());
	}

	public static Collection<PhpClass> getAllSubclasses(@Nonnull PhpClass psiClass)
	{
		if(psiClass.isFinal())
		{
			return Collections.emptyList();
		}

		final PhpIndex phpIndex = PhpIndex.getInstance(psiClass.getProject());
		return phpIndex.getAllSubclasses(psiClass.getFQN());
	}

	@Nullable
	public static PhpClass getObject(@Nonnull final Project project)
	{
		Iterator iterator = PhpIndex.getInstance(project).getClassesByFQN(PhpType._OBJECT_FQN).iterator();
		return iterator.hasNext() ? (PhpClass) iterator.next() : null;
	}

	public static boolean isMyTrait(@Nonnull final PhpClass me, @Nonnull final PhpClass trait, @Nullable Collection<? super PhpClass> visited)
	{
		if(!trait.isTrait())
		{
			return false;
		}
		if(visited == null)
		{
			visited = new HashSet<>();
		}
		for(final PhpClass candidate : me.getTraits())
		{
			if(!visited.add(candidate))
			{
				continue;
			}
			if(classesEqual(trait, candidate) || isMyTrait(candidate, trait, visited))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean classesEqual(@Nullable PhpClass one, @Nullable PhpClass another)
	{
		if(one != null && another != null)
		{
			if(one == another)
			{
				return true;
			}
			else if(one instanceof PhpClassAlias || another instanceof PhpClassAlias)
			{
				if(one instanceof PhpClassAlias)
				{
					one = ((PhpClassAlias) one).getOriginal();
				}
				if(another instanceof PhpClassAlias)
				{
					another = ((PhpClassAlias) another).getOriginal();
				}
				return classesEqual(one, another);
			}
			else if(StringUtil.equalsIgnoreCase(one.getFQN(), another.getFQN()))
			{
				return true;
			}
		}
		return false;
	}

}
