package consulo.php.completion.keywords;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import consulo.annotations.RequiredReadAction;
import consulo.codeInsight.completion.CompletionProvider;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.psi.impl.*;
import consulo.php.module.util.PhpModuleExtensionUtil;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 20.08.19.
 */
public class PhpKeywordsCompletionProvider implements CompletionProvider
{
	@RequiredReadAction
	@Override
	public void addCompletions(@Nonnull CompletionParameters completionParameters, ProcessingContext processingContext, @Nonnull CompletionResultSet completionResultSet)
	{
		PsiElement position = completionParameters.getPosition();
		PsiElement parent = position.getParent();

		PhpLanguageLevel languageLevel = PhpModuleExtensionUtil.getLanguageLevel(position);

		if(parent instanceof PhpFieldReferenceImpl)
		{
			return; // skip for $var->...
		}

		if(parent instanceof PhpClassImpl)
		{
			fillKeywordsForClassParent(completionResultSet, languageLevel);
		}
		else if(parent.getParent() instanceof PhpGroupStatementImpl)
		{
			parent = parent.getParent().getParent();

			if(parent instanceof PhpClassMethodImpl)
			{
				fillKeywordsForClassMethodParent(completionResultSet, languageLevel);
			}
			else if(parent instanceof PhpFunctionImpl)
			{
				fillKeywordsForFunctionParent(completionResultSet, languageLevel);
			}
			else if(parent instanceof PhpFileImpl)
			{
				fillGlobalKeywords(completionResultSet, languageLevel);
			}
		}
	}

	private static void fillBasicStatementKeywords(CompletionResultSet resultSet, PhpLanguageLevel languageLevel)
	{
		fillLookupElement(resultSet, PhpKeywords.CLONE, PhpKeywords.ECHO, PhpKeywords.FOR,
				PhpKeywords.FOREACH, PhpKeywords.GLOBAL, PhpKeywords.IF,
				PhpKeywords.NEW, PhpKeywords.PRINT, PhpKeywords.SWITCH, PhpKeywords.THROW,
				PhpKeywords.TRY, PhpKeywords.WHILE);

		if(languageLevel.isAtLeast(PhpLanguageLevel.PHP_5_3))
		{
			fillLookupElement(resultSet, PhpKeywords.GOTO);
		}

		if(languageLevel.isAtLeast(PhpLanguageLevel.PHP_5_5))
		{
			fillLookupElement(resultSet, PhpKeywords.YIELD);
		}
	}

	private static void fillGlobalKeywords(CompletionResultSet resultSet, PhpLanguageLevel languageLevel)
	{
		fillLookupElement(resultSet, PhpKeywords.ABSTRACT, PhpKeywords.FUNCTION, PhpKeywords.EXTENDS,
				PhpKeywords.CLASS, PhpKeywords.INTERFACE,
				PhpKeywords.USE);

		if(languageLevel.isAtLeast(PhpLanguageLevel.PHP_5_3))
		{
			fillLookupElement(resultSet, PhpKeywords.NAMESPACE);
		}

		if(languageLevel.isAtLeast(PhpLanguageLevel.PHP_5_4))
		{
			fillLookupElement(resultSet, PhpKeywords.TRAIT);
		}

		fillBasicStatementKeywords(resultSet, languageLevel);
	}

	private static void fillKeywordsForClassParent(CompletionResultSet resultSet, PhpLanguageLevel languageLevel)
	{
		fillLookupElement(resultSet, PhpKeywords.ABSTRACT, PhpKeywords.CONST, PhpKeywords.FINAL,
				PhpKeywords.PUBLIC, PhpKeywords.PRIVATE, PhpKeywords.PROTECTED,
				PhpKeywords.STATIC, PhpKeywords.FUNCTION, PhpKeywords.USE,
				PhpKeywords.VAR, PhpKeywords.EXTENDS, PhpKeywords.IMPLEMENTS);
	}

	private static void fillKeywordsForFunctionParent(CompletionResultSet resultSet, PhpLanguageLevel languageLevel)
	{
		fillLookupElement(resultSet, PhpKeywords.RETURN);

		fillBasicStatementKeywords(resultSet, languageLevel);
	}

	private static void fillKeywordsForClassMethodParent(CompletionResultSet resultSet, PhpLanguageLevel languageLevel)
	{
		resultSet.addElement(LookupElementBuilder.create(PhpKeywords.SELF).bold());
		resultSet.addElement(LookupElementBuilder.create(PhpKeywords.PARENT).bold());

		fillKeywordsForFunctionParent(resultSet, languageLevel);
	}

	private static void fillLookupElement(CompletionResultSet resultSet, String... elements)
	{
		for(String element : elements)
		{
			resultSet.addElement(LookupElementBuilder.create(element).bold().withInsertHandler(AddSpaceInsertHandler.INSTANCE));
		}
	}
}
