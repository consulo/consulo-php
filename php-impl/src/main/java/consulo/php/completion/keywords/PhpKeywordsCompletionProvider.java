package consulo.php.completion.keywords;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
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
	private static void addElement(CompletionResultSet resultSet, String element)
	{
		resultSet.addElement(LookupElementBuilder.create(element).bold().withInsertHandler(AddSpaceInsertHandler.INSTANCE));
	}

	private static void setBasicStatementKeywords(CompletionResultSet resultSet)
	{
		addElement(resultSet, "clone");
		addElement(resultSet, "echo");
		addElement(resultSet, "for");
		addElement(resultSet, "foreach");
		addElement(resultSet, "foreach");
		addElement(resultSet, "global");
		addElement(resultSet, "goto");
		addElement(resultSet, "if");
		addElement(resultSet, "new");
		addElement(resultSet, "print");
		addElement(resultSet, "switch");
		addElement(resultSet, "throw");
		addElement(resultSet, "try");
		addElement(resultSet, "while");
		addElement(resultSet, "yield");
	}

	private static void setGlobalKeywords(CompletionResultSet resultSet)
	{
		addElement(resultSet, "abstract");
		addElement(resultSet, "function");
		addElement(resultSet, "extends");
		addElement(resultSet, "class");
		addElement(resultSet, "namespace");
		addElement(resultSet, "interface");
		addElement(resultSet, "trait");
		addElement(resultSet, "use");

		setBasicStatementKeywords(resultSet);
	}

	private static void setKeywordsForClassParent(CompletionResultSet resultSet)
	{
		addElement(resultSet, "abstarct");
		addElement(resultSet, "const");
		addElement(resultSet, "final");
		addElement(resultSet, "public");
		addElement(resultSet, "private");
		addElement(resultSet, "protected");
		addElement(resultSet, "static");
		addElement(resultSet, "function");
		addElement(resultSet, "use");
		addElement(resultSet, "var");
		addElement(resultSet, "extends");
		addElement(resultSet, "implements");
	}

	private static void setKeywordsForFunctionParent(CompletionResultSet resultSet)
	{
		addElement(resultSet, "return");

		setBasicStatementKeywords(resultSet);
	}

	private static void setKeywordsForClassMethodParent(CompletionResultSet resultSet)
	{
		resultSet.addElement(LookupElementBuilder.create("self::").bold().withIcon(AllIcons.Nodes.Class)
				.withInsertHandler(AddSpaceInsertHandler.INSTANCE));
		resultSet.addElement(LookupElementBuilder.create("parent::").bold().withIcon(AllIcons.Nodes.Class)
				.withInsertHandler(AddSpaceInsertHandler.INSTANCE));

		setKeywordsForFunctionParent(resultSet);
	}

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
}
