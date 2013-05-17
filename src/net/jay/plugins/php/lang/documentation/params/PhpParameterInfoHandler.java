package net.jay.plugins.php.lang.documentation.params;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.MethodReference;
import net.jay.plugins.php.lang.psi.elements.Parameter;
import net.jay.plugins.php.lang.psi.elements.ParameterList;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 26, 2008 2:45:35 AM
 */
public class PhpParameterInfoHandler implements ParameterInfoHandler {
  private static final Logger LOG = Logger.getInstance(PhpParameterInfoHandler.class.getName());
  public static final String DEFAULT_PARAMETER_CLOSE_CHARS = ",)";

  public boolean couldShowInLookup() {
    return true;
  }

  public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
    return null;
  }

  public Object[] getParametersForDocumentation(Object p, ParameterInfoContext context) {
    return null;
  }

  public Object findElementForParameterInfo(CreateParameterInfoContext context) {
    final PsiFile psiFile = context.getFile();
    if (psiFile instanceof PHPFile) {
      final MethodReference methodReference = PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), MethodReference.class, false);
      if (methodReference != null) {
        final PsiElement element = methodReference.resolve();
        if (element != null && element instanceof Method) {
          context.setItemsToShow(new Object[]{element});
        }
      }
      return methodReference;
    }
    return null;
  }

  public void showParameterInfo(@NotNull Object element, CreateParameterInfoContext context) {
    context.showHint((PsiElement) element, ((PsiElement) element).getTextOffset(), this);
  }

  public Object findElementForUpdatingParameterInfo(UpdateParameterInfoContext context) {
    final PsiFile psiFile = context.getFile();
    if (psiFile instanceof PHPFile) {
      return PsiTreeUtil.findElementOfClassAtOffset(psiFile, context.getOffset(), MethodReference.class, false);
    }
    return null;
  }

  public void updateParameterInfo(@NotNull Object element, UpdateParameterInfoContext context) {
    int index = -1;
    final int caret = context.getOffset();
    if (element instanceof MethodReference) {
      final ParameterList callArgs = PsiTreeUtil.getChildOfType((PsiElement) element, ParameterList.class);
      LOG.assertTrue(callArgs != null);
      index = ParameterInfoUtils.getCurrentParameterIndex(callArgs.getNode(), caret, PHPTokenTypes.opCOMMA);
// If we are just before the arguments
      if (index == -1 && callArgs.getTextOffset() == caret + 1) {
        index = 0;
      }
    } else {
      if (caret > ((MethodReference) element).getTextRange().getEndOffset()) {
        index = 0;
      }
    }
    context.setCurrentParameter(index);
  }

  public String getParameterCloseChars() {
    return DEFAULT_PARAMETER_CLOSE_CHARS;
  }

  public boolean tracksParameterIndex() {
    return true;
  }

  public void updateUI(Object element, ParameterInfoUIContext context) {
    LOG.assertTrue(element instanceof Method);
    Method method = (Method) element;

    // Index to show
    final int index = context.getCurrentParameterIndex();

    final StringBuilder buff = new StringBuilder();
    // here we store index of current argument
    int start = -1;
    int end = -1;

    final Parameter[] parameters = method.getParameters();
    if (parameters.length > 0) {
      for (int i = 0; i < parameters.length; i++) {
        if (i > 0) {
          buff.append(", ");
        }
        final Parameter parameter = parameters[i];
        String paramName = "$" + parameter.getName();
        if (paramName.equals("$")) {
          paramName = "";
        }

        if (start == -1 && i == index) {
          start = buff.length();
          end = start + paramName.length();
        }
        buff.append(paramName);
      }
    } else {
      buff.append(CodeInsightBundle.message("parameter.info.no.parameters"));
    }

    context.setupUIComponentPresentation(
      buff.toString(),
      start,
      end,
      !context.isUIComponentEnabled(),
      false,
      false,
      context.getDefaultParameterColor());
  }
}
