package net.jay.plugins.php.completion;

import java.io.InputStream;
import java.util.List;

import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.elements.ClassConstantReference;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import net.jay.plugins.php.lang.psi.elements.FieldReference;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.MethodReference;
import net.jay.plugins.php.lang.psi.elements.NewExpression;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.Variable;

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
		final IElementType[] iElementTypes = PHPTokenTypes.tsKEYWORDS.getTypes();
		keywords = new String[iElementTypes.length];
		int i = 0;

		final @NonNls String keywordMarker = " keyword";

		for(IElementType type : iElementTypes)
		{
			String s = type.toString();
			if(s.endsWith(keywordMarker))
				s = s.substring(0, s.length() - keywordMarker.length());
			keywords[i++] = s;
		}
	}

	public PhpCompletionData()
	{
		CompletionVariant variant = new CompletionVariant(new ElementFilter()
		{
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
				if(parent instanceof Variable)
				{
					return false;
				}
				if(parent instanceof ClassReference)
				{
					return false;
				}
				if(parent instanceof MethodReference)
				{
					return false;
				}
				if(parent instanceof ClassConstantReference)
				{
					return false;
				}
				if(parent instanceof FieldReference)
				{
					return false;
				}
				return true;
			}

			public boolean isClassAcceptable(Class aClass)
			{
				return true;
			}
		});
		variant.includeScopeClass(LeafPsiElement.class);
		variant.addCompletionFilter(TrueFilter.INSTANCE);
		variant.addCompletion(new ContextGetter()
		{
			public Object[] get(PsiElement psiElement, CompletionContext completionContext)
			{
				if(psiElement.getLanguage() != PHPFileType.PHP.getLanguage())
				{
					return ArrayUtil.EMPTY_OBJECT_ARRAY;
				}
				//noinspection ConstantConditions
				if(psiElement.getNode().getElementType() == PHPTokenTypes.IDENTIFIER && psiElement.getParent() instanceof Method && ((Method) psiElement.getParent()).getNameNode() == psiElement.getNode())
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
		variant.includeScopeClass(Variable.class);
		variant.addCompletionFilter(TrueFilter.INSTANCE);
		variant.addCompletion(superGlobals);
		registerVariant(variant);

		variant = new CompletionVariant(new ElementFilter()
		{
			@SuppressWarnings({"RedundantIfStatement"})
			public boolean isAcceptable(Object element, PsiElement context)
			{
				if(context.getParent() instanceof NewExpression)
				{
					return true;
				}
				return false;
			}

			public boolean isClassAcceptable(Class hintClass)
			{
				return true;
			}
		});
		variant.includeScopeClass(ClassReference.class);
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
				Method method = klass.getConstructor();
				if(method != null && method.getParameters().length > 0)
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

	public String findPrefix(PsiElement psiElement, int i)
	{
		PsiReference reference = psiElement.getContainingFile().findReferenceAt(i);
		String prefix = super.findPrefix(psiElement, i);
		if(reference instanceof Variable && prefix.startsWith("$"))
		{
			prefix = prefix.substring(1);
		}
		return prefix;
	}
}