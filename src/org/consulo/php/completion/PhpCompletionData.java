package org.consulo.php.completion;

import java.io.InputStream;
import java.util.List;

import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpClassConstantReference;
import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpFieldReference;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpMethodReference;
import org.consulo.php.lang.psi.PhpNewExpression;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.jetbrains.annotations.NonNls;
import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.BasicInsertHandler;
import com.intellij.codeInsight.completion.CompletionContext;
import com.intellij.codeInsight.completion.CompletionData;
import com.intellij.codeInsight.completion.CompletionVariant;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.filters.ContextGetter;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.TrueFilter;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;
import com.intellij.util.text.LineReader;

/**
 * @author Maxim
 */
public class PhpCompletionData extends CompletionData
{

	@NonNls
	public static String[] predefinedFuncs = new String[0];

	/**
	 * predefined php functions initialization
	 */
	static
	{
		try
		{
			InputStream predefined = PhpCompletionData.class.getResourceAsStream("functions.prdf");
			if(predefined != null)
			{
				LineReader lineReader = new LineReader(predefined);

				//noinspection unchecked
				List<byte[]> list = lineReader.readLines();
				predefinedFuncs = new String[list.size()];
				for(int i = 0; i < list.size(); i++)
				{
					byte[] bytes = list.get(i);
					predefinedFuncs[i] = new String(bytes);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@NonNls
	private static String[] keywords;

	static
	{
		final IElementType[] iElementTypes = PhpTokenTypes.KEYWORDS.getTypes();
		keywords = new String[iElementTypes.length];
		int i = 0;

		final @NonNls String keywordMarker = " keyword";

		for(IElementType type : iElementTypes)
		{
			String s = type.toString();
			if(s.endsWith(keywordMarker))
			{
				s = s.substring(0, s.length() - keywordMarker.length());
			}
			keywords[i++] = s;
		}
	}

	public PhpCompletionData()
	{
		CompletionVariant variant = new CompletionVariant(new ElementFilter()
		{
			@Override
			@SuppressWarnings({"RedundantIfStatement"})
			public boolean isAcceptable(Object o, PsiElement psiElement)
			{
				//        System.out.println("object: " + o);
				//        System.out.println("element: " + psiElement);
				//        System.out.println("parent: " + psiElement.getParent());
				//        System.out.println("text:");
				//        System.out.println(psiElement.getParent().getParent().getText());
				//        System.out.println("--------");
				final PsiElement parent = psiElement.getParent();
				if(parent instanceof PhpVariableReference)
				{
					return false;
				}
				if(parent instanceof PhpClassReference)
				{
					return false;
				}
				if(parent instanceof PhpMethodReference)
				{
					return false;
				}
				if(parent instanceof PhpClassConstantReference)
				{
					return false;
				}
				if(parent instanceof PhpFieldReference)
				{
					return false;
				}
				return true;
			}

			@Override
			public boolean isClassAcceptable(Class aClass)
			{
				return true;
			}
		});
		variant.includeScopeClass(LeafPsiElement.class);
		variant.addCompletionFilter(TrueFilter.INSTANCE);
		variant.addCompletion(new ContextGetter()
		{
			@Override
			public Object[] get(PsiElement psiElement, CompletionContext completionContext)
			{
				if(psiElement.getLanguage() != PhpFileType.INSTANCE.getLanguage())
				{
					return ArrayUtil.EMPTY_OBJECT_ARRAY;
				}
				//noinspection ConstantConditions
				if(psiElement.getNode().getElementType() == PhpTokenTypes.IDENTIFIER && psiElement.getParent() instanceof PhpFunction && ((PhpFunction) psiElement.getParent()).getNameIdentifier() == psiElement)
				{
					return new Object[]{
							"__construct",
							"__destruct"
					};
				}
				final Object[] result = new Object[predefinedFuncs.length + keywords.length];
				System.arraycopy(predefinedFuncs, 0, result, 0, predefinedFuncs.length);
				System.arraycopy(keywords, 0, result, predefinedFuncs.length, keywords.length);
				return result;
			}
		}, TailType.NONE);
		registerVariant(variant);

		variant = new CompletionVariant(TrueFilter.INSTANCE);
		variant.includeScopeClass(PhpVariableReference.class);
		variant.addCompletionFilter(TrueFilter.INSTANCE);
		variant.addCompletion(superGlobals);
		registerVariant(variant);

		variant = new CompletionVariant(new ElementFilter()
		{
			@Override
			@SuppressWarnings({"RedundantIfStatement"})
			public boolean isAcceptable(Object element, PsiElement context)
			{
				if(context.getParent() instanceof PhpNewExpression)
				{
					return true;
				}
				return false;
			}

			@Override
			public boolean isClassAcceptable(Class hintClass)
			{
				return true;
			}
		});
		variant.includeScopeClass(PhpClassReference.class);
		variant.addCompletionFilter(TrueFilter.INSTANCE);
		variant.setInsertHandler(new BasicInsertHandler()
		{
			@Override
			public void handleInsert(InsertionContext context, LookupElement element)
			{
				super.handleInsert(context, element);
				Editor editor = context.getEditor();
				EditorModificationUtil.insertStringAtCaret(editor, "()");
				PsiDocumentManager.getInstance(editor.getProject()).commitDocument(editor.getDocument());
				PhpClass klass = (PhpClass) element.getObject();
				PhpFunction phpMethod = klass.getConstructor();
				if(phpMethod != null && phpMethod.getParameters().length > 0)
				{
					editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
				}
			}
		});
		registerVariant(variant);
	}

	public static final String[] superGlobals = new String[]{
			"_GET",
			"_POST",
			"_SERVER",
			"_FILES",
			"_COOKIE",
			"_SESSION",
			"_REQUEST",
			"_ENV",
			"GLOBALS"
	};

	@Override
	public String findPrefix(PsiElement psiElement, int i)
	{
		PsiReference reference = psiElement.getContainingFile().findReferenceAt(i);
		String prefix = super.findPrefix(psiElement, i);
		if(reference instanceof PhpVariableReference && prefix.startsWith("$"))
		{
			prefix = prefix.substring(1);
		}
		return prefix;
	}
}