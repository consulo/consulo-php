package consulo.php.lang.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PhpElement extends PsiElement
{
	public static final PhpElement[] EMPTY_ARRAY = new PhpElement[0];

	public static ArrayFactory<PhpElement> ARRAY_FACTORY = new ArrayFactory<PhpElement>()
	{
		@Nonnull
		@Override
		public PhpElement[] create(int count)
		{
			return count == 0 ? EMPTY_ARRAY : new PhpElement[count];
		}
	};

	PhpElement getFirstPsiChild();

	PhpElement getNextPsiSibling();

	PhpElement getPrevPsiSibling();
}
