package consulo.php.lang.psi;

import consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;

/**
 * @author jay
 * @date Jun 4, 2008 11:40:27 AM
 */
public interface PhpNamedElement extends PsiNameIdentifierOwner, PsiNamedElement, PhpElement
{
	PhpDocComment getDocComment();
}
