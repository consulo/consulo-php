package consulo.php.lang.psi;

import javax.annotation.Nonnull;

import consulo.psi.PsiPackage;
import com.intellij.util.ArrayFactory;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public interface PhpPackage extends PsiPackage
{
	PhpPackage[] EMPTY_ARRAY = new PhpPackage[0];

	ArrayFactory<PhpPackage> ARRAY_FACTORY = new ArrayFactory<PhpPackage>()
	{
		@Nonnull
		@Override
		public PhpPackage[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new PhpPackage[i];
		}
	};

	@Nonnull
	String getNamespaceName();
}
