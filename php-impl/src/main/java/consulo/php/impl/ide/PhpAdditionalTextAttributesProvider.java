package consulo.php.impl.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AdditionalTextAttributesProvider;
import consulo.colorScheme.EditorColorsScheme;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 30-Aug-22
 */
@ExtensionImpl
public class PhpAdditionalTextAttributesProvider implements AdditionalTextAttributesProvider
{
	@Nonnull
	@Override
	public String getColorSchemeName()
	{
		return EditorColorsScheme.DEFAULT_SCHEME_NAME;
	}

	@Nonnull
	@Override
	public String getColorSchemeFile()
	{
		return "/colorSchemes/Default.xml";
	}
}
