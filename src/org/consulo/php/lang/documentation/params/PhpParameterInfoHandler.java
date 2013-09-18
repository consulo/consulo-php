package org.consulo.php.lang.documentation.params;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.impl.PhpFileImpl;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.lang.parameterInfo.ParameterInfoUtils;
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jun 26, 2008 2:45:35 AM
 */
public class PhpParameterInfoHandler implements ParameterInfoHandler
{
	private static final Logger LOG = Logger.getInstance(PhpParameterInfoHandler.class.getName());
	public static final String DEFAULT_PARAMETER_CLOSE_CHARS = ",)";

	@Override
	public boolean couldShowInLookup()
	{
		return true;
	}

	@Override
	public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context)
	{
		return null;
	}

	@Override
	public Object[] getParametersForDocumentation(Object p, ParameterInfoContext context)
	{
		return null;
	}

	@Override
	public Object findElementForParameterInfo(CreateParameterInfoContext context)
	{
		final PsiFile psiFile = context.getFile();
		if(psiFile instanceof PhpFileImpl)
		{
			final PhpMethodReference phpMethodReference = PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), PhpMethodReference.class, false);
			if(phpMethodReference != null)
			{
				final PsiElement element = phpMethodReference.resolve();
				if(element != null && element instanceof PhpMethod)
				{
					context.setItemsToShow(new Object[]{element});
				}
			}
			return phpMethodReference;
		}
		return null;
	}

	@Override
	public void showParameterInfo(@NotNull Object element, CreateParameterInfoContext context)
	{
		context.showHint((PsiElement) element, ((PsiElement) element).getTextOffset(), this);
	}

	@Override
	public Object findElementForUpdatingParameterInfo(UpdateParameterInfoContext context)
	{
		final PsiFile psiFile = context.getFile();
		if(psiFile instanceof PhpFileImpl)
		{
			return PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), PhpMethodReference.class, false);
		}
		return null;
	}

	@Override
	public void updateParameterInfo(@NotNull Object element, UpdateParameterInfoContext context)
	{
		int index = -1;
		final int caret = context.getOffset();
		if(element instanceof PhpMethodReference)
		{
			final PhpParameterList callArgs = PsiTreeUtil.getChildOfType((PsiElement) element, PhpParameterList.class);
			LOG.assertTrue(callArgs != null);
			index = ParameterInfoUtils.getCurrentParameterIndex(callArgs.getNode(), caret, PhpTokenTypes.opCOMMA);
			// If we are just before the arguments
			if(index == -1 && callArgs.getTextOffset() == caret + 1)
			{
				index = 0;
			}
		}
		else
		{
			if(caret > ((PhpMethodReference) element).getTextRange().getEndOffset())
			{
				index = 0;
			}
		}
		context.setCurrentParameter(index);
	}

	@Override
	public String getParameterCloseChars()
	{
		return DEFAULT_PARAMETER_CLOSE_CHARS;
	}

	@Override
	public boolean tracksParameterIndex()
	{
		return true;
	}

	@Override
	public void updateUI(Object element, ParameterInfoUIContext context)
	{
		LOG.assertTrue(element instanceof PhpMethod);
		PhpMethod phpMethod = (PhpMethod) element;

		// Index to show
		final int index = context.getCurrentParameterIndex();

		final StringBuilder buff = new StringBuilder();
		// here we store index of current argument
		int start = -1;
		int end = -1;

		final PhpParameter[] parameters = phpMethod.getParameters();
		if(parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if(i > 0)
				{
					buff.append(", ");
				}
				final PhpParameter parameter = parameters[i];
				String paramName = "$" + parameter.getName();
				if(paramName.equals("$"))
				{
					paramName = "";
				}

				if(start == -1 && i == index)
				{
					start = buff.length();
					end = start + paramName.length();
				}
				buff.append(paramName);
			}
		}
		else
		{
			buff.append(CodeInsightBundle.message("parameter.info.no.parameters"));
		}

		context.setupUIComponentPresentation(buff.toString(), start, end, !context.isUIComponentEnabled(), false, false, context.getDefaultParameterColor());
	}
}
