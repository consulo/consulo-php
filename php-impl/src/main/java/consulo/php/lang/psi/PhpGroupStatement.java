package consulo.php.lang.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface PhpGroupStatement extends PhpPsiElement
{
	PhpGroupStatement[] EMPTY_ARRAY = new PhpGroupStatement[0];

	ArrayFactory<PhpGroupStatement> ARRAY_FACTORY = new ArrayFactory<PhpGroupStatement>()
	{
		@Nonnull
		@Override
		public PhpGroupStatement[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new PhpGroupStatement[i];
		}
	};

	@Nonnull
	PsiElement[] getStatements();
}
