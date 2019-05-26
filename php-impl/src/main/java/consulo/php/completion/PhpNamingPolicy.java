package consulo.php.completion;

import com.jetbrains.php.lang.psi.elements.PhpNamedElement;

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
					return element.getName();
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
