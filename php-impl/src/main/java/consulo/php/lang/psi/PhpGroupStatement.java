package consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface PhpGroupStatement extends PhpElement
{
	PhpGroupStatement[] EMPTY_ARRAY = new PhpGroupStatement[0];

	ArrayFactory<PhpGroupStatement> ARRAY_FACTORY = new ArrayFactory<PhpGroupStatement>()
	{
		@NotNull
		@Override
		public PhpGroupStatement[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new PhpGroupStatement[i];
		}
	};

	@NotNull
	PsiElement[] getStatements();
}
