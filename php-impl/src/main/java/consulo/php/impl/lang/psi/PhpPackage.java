package consulo.php.impl.lang.psi;

import consulo.language.psi.PsiPackage;
import consulo.util.collection.ArrayFactory;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public interface PhpPackage extends PsiPackage
{
	PhpPackage[] EMPTY_ARRAY = new PhpPackage[0];

	ArrayFactory<PhpPackage> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new PhpPackage[i];

	@Nonnull
	String getNamespaceName();
}
