package consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpFile extends PsiFile, PhpElement
{
	@NotNull
	PhpElement[] getTopLevelElements();
}
