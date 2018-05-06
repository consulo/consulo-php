package consulo.php.lang.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpFile extends PsiFile, PhpElement
{
	@Nonnull
	PhpElement[] getTopLevelElements();
}
