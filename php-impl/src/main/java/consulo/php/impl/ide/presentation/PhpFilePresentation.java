package consulo.php.impl.ide.presentation;

import com.jetbrains.php.lang.psi.PhpFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.navigation.ItemPresentation;
import consulo.navigation.ItemPresentationProvider;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
@ExtensionImpl
public class PhpFilePresentation implements ItemPresentationProvider<PhpFile>
{
	@Nonnull
	@Override
	public Class<PhpFile> getItemClass()
	{
		return PhpFile.class;
	}

	@Override
	public ItemPresentation getPresentation(final PhpFile item)
	{
		return new ItemPresentation()
		{
			@Nullable
			@Override
			public String getPresentableText()
			{
				return item.getName();
			}

			@Nullable
			@Override
			public String getLocationString()
			{
				return null;
			}

			@Nullable
			@Override
			public Image getIcon()
			{
				return IconDescriptorUpdaters.getIcon(item, 0);
			}
		};
	}
}
