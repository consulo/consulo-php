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

package com.jetbrains.php.completion.insert;


import com.jetbrains.php.codeInsight.PhpCodeInsightUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.PhpGroupUseElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.refactoring.PhpAliasImporter;
import consulo.document.Document;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;

/**
 * Completes references to PHP elements, in the shape of PhpStorm's {@code PhpReferenceInsertHandler}:
 * <ul>
 * <li>a class in PHP code is imported, or written fully qualified when its name is taken</li>
 * <li>a class outside of PHP code (YAML, XML, strings) is written fully qualified: {@code \App\Foo}</li>
 * <li>a function or method gets its parentheses</li>
 * </ul>
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpReferenceInsertHandler implements InsertHandler<LookupElement>
{
	private static final PhpReferenceInsertHandler INSTANCE = new PhpReferenceInsertHandler();

	@Nonnull
	public static PhpReferenceInsertHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void handleInsert(@Nonnull InsertionContext context, @Nonnull LookupElement lookupElement)
	{
		Object object = lookupElement.getObject();
		if(object instanceof PhpClass phpClass)
		{
			insertClass(context, phpClass);
		}
		else if(object instanceof Function function)
		{
			insertParentheses(context, function);
		}
	}

	private static void insertClass(InsertionContext context, PhpClass phpClass)
	{
		String fqn = phpClass.getFQN();
		Document document = context.getDocument();
		if(!(context.getFile() instanceof PhpFile))
		{
			// the lookup string is the short name: put the namespace in front of it
			document.insertString(context.getStartOffset(), PhpLangUtil.getParentNamespaceFQN(fqn));
			return;
		}

		PsiDocumentManager documentManager = PsiDocumentManager.getInstance(context.getProject());
		documentManager.commitDocument(document);
		PsiElement element = context.getFile().findElementAt(context.getStartOffset());
		PhpPsiElement scope = element == null ? null : PhpCodeInsightUtil.findScopeForUseOperator(element);
		if(scope == null || isFqnTyped(document, context.getStartOffset()))
		{
			return;
		}

		String name = PhpCodeInsightUtil.createQualifiedName(scope, fqn, element, false);
		if(!PhpLangUtil.isFqn(name))
		{
			if(!name.equals(phpClass.getName()))
			{
				document.replaceString(context.getStartOffset(), context.getTailOffset(), name);
			}
			return;
		}
		if(!PhpCodeInsightUtil.canImport(scope, fqn, PhpGroupUseElement.PhpUseKeyword.CLASS))
		{
			document.replaceString(context.getStartOffset(), context.getTailOffset(), fqn);
			return;
		}
		PhpAliasImporter.insertUseStatement(fqn, scope);
		documentManager.doPostponedOperationsAndUnblockDocument(document);
	}

	/**
	 * The name is already qualified, like {@code \App\Fo<caret>}
	 */
	private static boolean isFqnTyped(Document document, int startOffset)
	{
		return startOffset > 0 && document.getCharsSequence().charAt(startOffset - 1) == '\\';
	}

	private static void insertParentheses(InsertionContext context, Function function)
	{
		if(PhpInsertHandlerUtil.isStringAtCaret(context.getEditor(), "("))
		{
			return;
		}
		PhpInsertHandlerUtil.insertStringAtCaret(context.getEditor(), "()");
		if(function.getParameters().length > 0)
		{
			context.getEditor().getCaretModel().moveToOffset(context.getEditor().getCaretModel().getOffset() - 1);
		}
	}
}
