/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.php.completion;


import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.AutoPopupController;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.completion.lookup.PrioritizedLookupElement;
import consulo.language.editor.ui.awt.EditorTextField;
import consulo.language.editor.ui.awt.TextFieldCompletionProvider;
import consulo.disposer.Disposable;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Completion helpers, in the shape of PhpStorm's {@code PhpCompletionUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpCompletionUtil
{
	private PhpCompletionUtil()
	{
	}

	/**
	 * Opens the completion popup again after an insert, like after {@code Foo::}
	 */
	public static void showCompletion(@Nonnull InsertionContext context)
	{
		AutoPopupController.getInstance(context.getProject()).scheduleAutoPopup(context.getEditor());
	}

	/**
	 * Adds the classes and interfaces whose short names match the prefix matcher of the result set
	 */
	@RequiredReadAction
	@SuppressWarnings("rawtypes")
	public static void addClasses(@Nonnull String prefix,
								  @Nonnull CompletionResultSet result,
								  @Nonnull PhpIndex phpIndex,
								  @Nullable InsertHandler insertHandler)
	{
		CompletionResultSet resultSet = result.withPrefixMatcher(prefix);
		for(String name : phpIndex.getAllClassNames(resultSet.getPrefixMatcher()))
		{
			for(PhpClass phpClass : phpIndex.getClassesByName(name))
			{
				resultSet.addElement(new PhpClassLookupElement(phpClass, true, insertHandler));
			}
		}
	}

	/**
	 * @param namespaceFqn namespace with the leading backslash: {@code \App\Entity}
	 * @return classes directly in the namespace
	 */
	@Nonnull
	@RequiredReadAction
	public static List<PhpClass> filterByNamespace(@Nonnull Collection<PhpClass> classes, @Nonnull String namespaceFqn)
	{
		String namespace = PhpLangUtil.toFQN(namespaceFqn);
		String expected = namespace.endsWith("\\") ? namespace : namespace + "\\";
		List<PhpClass> result = new ArrayList<>();
		for(PhpClass phpClass : classes)
		{
			if(PhpLangUtil.equalsNamespaceNames(PhpLangUtil.getParentNamespaceFQN(phpClass.getFQN()), expected))
			{
				result.add(phpClass);
			}
		}
		return result;
	}

	/**
	 * Turns the text field into a class name field with completion of class names; the inserted value is the FQN without the leading backslash
	 *
	 * @param defaultNamespace namespace whose classes are offered first, like {@code \\App\\Entity}
	 * @param classFilter      only the classes accepted by the filter are offered
	 */
	public static void installClassCompletion(@Nonnull EditorTextField textField,
											  @Nullable String defaultNamespace,
											  @Nonnull Disposable parentDisposable,
											  @Nullable Predicate<PhpClass> classFilter)
	{
		Project project = textField.getProject();
		if(project == null)
		{
			return;
		}
		new ClassNameCompletionProvider(project, defaultNamespace, classFilter).apply(textField, textField.getText());
	}

	private static class ClassNameCompletionProvider extends TextFieldCompletionProvider
	{
		private final Project myProject;
		@Nullable
		private final String myDefaultNamespace;
		@Nullable
		private final Predicate<PhpClass> myClassFilter;

		private ClassNameCompletionProvider(@Nonnull Project project, @Nullable String defaultNamespace, @Nullable Predicate<PhpClass> classFilter)
		{
			super(true);
			myProject = project;
			// namespaces with the trailing separator, like getParentNamespaceFQN
			myDefaultNamespace = defaultNamespace == null ? null : StringUtil.trimEnd(PhpLangUtil.toFQN(defaultNamespace), "\\") + "\\";
			myClassFilter = classFilter;
		}

		@Nonnull
		@Override
		public String getPrefix(@Nonnull String currentTextPrefix)
		{
			// the short name is matched, the typed namespace filters the classes
			return currentTextPrefix.substring(currentTextPrefix.lastIndexOf('\\') + 1);
		}

		@Override
		@RequiredReadAction
		public void addCompletionVariants(@Nonnull String text, int offset, @Nonnull String prefix, @Nonnull CompletionResultSet result)
		{
			String typed = text.substring(0, offset);
			int namespaceEnd = typed.lastIndexOf('\\');
			String namespace = namespaceEnd < 0 ? null : PhpLangUtil.toFQN(typed.substring(0, namespaceEnd + 1));

			PhpIndex phpIndex = PhpIndex.getInstance(myProject);
			for(String name : phpIndex.getAllClassNames(result.getPrefixMatcher()))
			{
				for(PhpClass phpClass : phpIndex.getClassesByName(name))
				{
					String classNamespace = PhpLangUtil.getParentNamespaceFQN(phpClass.getFQN());
					if(namespace != null && !PhpLangUtil.equalsNamespaceNames(classNamespace, namespace))
					{
						continue;
					}
					if(myClassFilter != null && !myClassFilter.test(phpClass))
					{
						continue;
					}

					String fqn = PhpLangUtil.toPresentableFQN(phpClass.getFQN());
					LookupElementBuilder element = LookupElementBuilder.create(phpClass, phpClass.getName())
						.withLookupString(fqn)
						.withTypeText(classNamespace, true)
						.withInsertHandler((context, item) -> context.getDocument().replaceString(0, context.getTailOffset(), fqn));

					boolean preferred = myDefaultNamespace != null && PhpLangUtil.equalsNamespaceNames(classNamespace, myDefaultNamespace);
					result.addElement(preferred ? PrioritizedLookupElement.withPriority(element, 1) : element);
				}
			}
		}
	}
}
