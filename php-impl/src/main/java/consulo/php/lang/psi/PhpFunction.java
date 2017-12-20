package consulo.php.lang.psi;

import consulo.php.lang.psi.resolve.types.PhpTypeOwner;
import org.jetbrains.annotations.NotNull;
import com.intellij.util.ArrayFactory;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface PhpFunction extends PhpModifierListOwner, PhpTypeOwner, PhpBraceOwner
{
	PhpFunction[] EMPTY_ARRAY = new PhpFunction[0];

	ArrayFactory<PhpFunction> ARRAY_FACTORY = new ArrayFactory<PhpFunction>()
	{
		@NotNull
		@Override
		public PhpFunction[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new PhpFunction[i];
		}
	};

	PhpParameter[] getParameters();

	PhpParameterList getParameterList();
}
