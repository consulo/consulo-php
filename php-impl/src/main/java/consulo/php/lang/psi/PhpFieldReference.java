package consulo.php.lang.psi;

import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date May 15, 2008 11:23:18 AM
 */
public interface PhpFieldReference extends PhpPsiElement, PsiReference
{

	public boolean canReadName();

	@Nullable
	public String getFieldName();

	@Nullable
	public ClassReference getClassReference();

	@Nullable
	public PsiElement getObjectReference();
}
