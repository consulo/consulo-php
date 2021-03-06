package consulo.php.lang.formatting;

import javax.annotation.Nonnull;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;

/**
 * @author jay
 * @date Jun 14, 2008 6:33:48 PM
 */
public class PhpFormattingModelBuilder implements FormattingModelBuilder
{

	/**
	 * Requests building the formatting model for a section of the file containing
	 * the specified PSI element and its children.
	 *
	 * @param element  the top element for which formatting is requested.
	 * @param settings the code style settings used for formatting.
	 * @return the formatting model for the file.
	 */
	@Override
	@Nonnull
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings)
	{
		return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), null, settings);
	}

	@Override
	public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
	{
		return null;
	}
}
