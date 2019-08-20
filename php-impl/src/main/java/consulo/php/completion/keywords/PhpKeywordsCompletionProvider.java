package consulo.php.completion.keywords;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import consulo.annotations.RequiredReadAction;
import consulo.codeInsight.completion.CompletionProvider;
import consulo.php.lang.psi.impl.*;

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

		if(parent instanceof PhpFieldReferenceImpl)
		{
			return; // skip for $var->...
		}

		if(parent instanceof PhpClassImpl)
		{
			setKeywordsForClassParent(completionResultSet);
		}
		else if(parent.getParent() instanceof PhpGroupStatementImpl)
		{
			parent = parent.getParent().getParent();

			if(parent instanceof PhpClassMethodImpl)
			{
				setKeywordsForClassMethodParent(completionResultSet);
			}
			else if(parent instanceof PhpFunctionImpl)
			{
				setKeywordsForFunctionParent(completionResultSet);
			}
			else if(parent instanceof PhpFileImpl)
			{
				setGlobalKeywords(completionResultSet);
			}
		}
	}

	private static void setBasicStatementKeywords(CompletionResultSet resultSet)
	{
		addLookupElements(resultSet, PhpKeywords.CLONE, PhpKeywords.ECHO, PhpKeywords.FOR,
				PhpKeywords.FOREACH, PhpKeywords.GLOBAL, PhpKeywords.GOTO, PhpKeywords.IF,
				PhpKeywords.NEW, PhpKeywords.PRINT, PhpKeywords.SWITCH, PhpKeywords.THROW,
				PhpKeywords.TRY, PhpKeywords.WHILE, PhpKeywords.YIELD);
	}

	private static void setGlobalKeywords(CompletionResultSet resultSet)
	{
		addLookupElements(resultSet, PhpKeywords.ABSTRACT, PhpKeywords.FUNCTION, PhpKeywords.EXTENDS,
				PhpKeywords.CLASS, PhpKeywords.NAMESPACE, PhpKeywords.INTERFACE,
				PhpKeywords.TRAIT, PhpKeywords.USE);

		setBasicStatementKeywords(resultSet);
	}

	private static void setKeywordsForClassParent(CompletionResultSet resultSet)
	{
		addLookupElements(resultSet, PhpKeywords.ABSTRACT, PhpKeywords.CONST, PhpKeywords.FINAL,
				PhpKeywords.PUBLIC, PhpKeywords.PRIVATE, PhpKeywords.PROTECTED,
				PhpKeywords.STATIC, PhpKeywords.FUNCTION, PhpKeywords.USE,
				PhpKeywords.VAR, PhpKeywords.EXTENDS, PhpKeywords.IMPLEMENTS);
	}

	private static void setKeywordsForFunctionParent(CompletionResultSet resultSet)
	{
		addLookupElements(resultSet, PhpKeywords.RETURN);

		setBasicStatementKeywords(resultSet);
	}

	private static void addLookupElements(CompletionResultSet resultSet, String... elements)
	{
		for(String element : elements)
		{
			resultSet.addElement(LookupElementBuilder.create(element).bold().withInsertHandler(AddSpaceInsertHandler.INSTANCE));
		}
	}

	private static void setKeywordsForClassMethodParent(CompletionResultSet resultSet)
	{
		resultSet.addElement(LookupElementBuilder.create(PhpKeywords.SELF).bold());
		resultSet.addElement(LookupElementBuilder.create(PhpKeywords.PARENT).bold());

		setKeywordsForFunctionParent(resultSet);
	}
}
