package consulo.php.lang.psi;

import javax.annotation.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface PhpClassReference extends PhpElement, PsiPolyVariantReference
{
	@Nullable
	PhpClassReference getQualifier();

	@Nullable
	String getReferenceName();

	@Nullable
	PsiElement getReferenceElement();
}
