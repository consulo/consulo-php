package consulo.php.impl.completion;

import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.Variable;

/**
 * @author VISTALL
 * @since 2019-05-26
 */
public enum PhpNamingPolicy
{
	NOTHING
			{
				@Override
				public CharSequence getName(PhpNamedElement element)
				{
					// variables are completed with the '$'
					return element instanceof Variable || element instanceof Parameter ? "$" + element.getName() : element.getName();
				}
			},
	WITHOUT_DOLLAR
			{
				@Override
				public CharSequence getName(PhpNamedElement element)
				{
					return element.getNameCS();
				}
			};

	public abstract CharSequence getName(PhpNamedElement element);
}
