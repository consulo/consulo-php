package consulo.php.impl.lang.documentation.params;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.CodeInsightBundle;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.parameterInfo.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.logging.Logger;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;

import jakarta.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 26, 2008 2:45:35 AM
 */
@ExtensionImpl
public class PhpParameterInfoHandler implements ParameterInfoHandler
{
	private static final Logger LOG = Logger.getInstance(PhpParameterInfoHandler.class);

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
	public Object findElementForParameterInfo(CreateParameterInfoContext context)
	{
		final PsiFile psiFile = context.getFile();
		if(psiFile instanceof PhpFileImpl)
		{
			final MethodReference phpMethodReference = PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), MethodReference.class, false);
			if(phpMethodReference != null)
			{
				final PsiElement element = phpMethodReference.resolve();
				if(element != null && element instanceof Function)
				{
					context.setItemsToShow(new Object[]{element});
				}
			}
			return phpMethodReference;
		}
		return null;
	}

	@Override
	public void showParameterInfo(@Nonnull Object element, CreateParameterInfoContext context)
	{
		context.showHint((PsiElement) element, ((PsiElement) element).getTextOffset(), this);
	}

	@Override
	public Object findElementForUpdatingParameterInfo(UpdateParameterInfoContext context)
	{
		final PsiFile psiFile = context.getFile();
		if(psiFile instanceof PhpFileImpl)
		{
			return PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), MethodReference.class, false);
		}
		return null;
	}

	@Override
	public void updateParameterInfo(@Nonnull Object element, UpdateParameterInfoContext context)
	{
		int index = -1;
		final int caret = context.getOffset();
		if(element instanceof MethodReference)
		{
			final ParameterList callArgs = PsiTreeUtil.getChildOfType((PsiElement) element, ParameterList.class);
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
			if(caret > ((MethodReference) element).getTextRange().getEndOffset())
			{
				index = 0;
			}
		}
		context.setCurrentParameter(index);
	}

	@Override
	public void updateUI(Object element, ParameterInfoUIContext context)
	{
		LOG.assertTrue(element instanceof Function);
		Function phpMethod = (Function) element;

		// Index to show
		final int index = context.getCurrentParameterIndex();

		final StringBuilder buff = new StringBuilder();
		// here we store index of current argument
		int start = -1;
		int end = -1;

		final Parameter[] parameters = phpMethod.getParameters();
		if(parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if(i > 0)
				{
					buff.append(", ");
				}
				final Parameter parameter = parameters[i];
				String paramName = parameter.getName();
				if(paramName.equals("$"))
				{
					paramName = "";
				}

				if(start == -1 && i == index)
				{
					start = buff.length();
					end = start + paramName.length();
				}

				PhpType type = parameter.getType();
				if(type != PhpType.EMPTY)
				{
					buff.append(type.toString()).append(" ");
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

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
