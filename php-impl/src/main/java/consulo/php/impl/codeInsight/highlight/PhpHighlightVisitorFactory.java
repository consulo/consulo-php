package consulo.php.impl.codeInsight.highlight;

import com.jetbrains.php.lang.psi.PhpFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.editor.rawHighlight.HighlightVisitorFactory;
import consulo.language.psi.PsiFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 26/03/2023
 */
@ExtensionImpl
public class PhpHighlightVisitorFactory implements HighlightVisitorFactory
{
	@Override
	public boolean suitableForFile(@Nonnull PsiFile psiFile)
	{
		return psiFile instanceof PhpFile;
	}

	@Nonnull
	@Override
	public HighlightVisitor createVisitor()
	{
		return new PhpHighlightVisitor();
	}
}
