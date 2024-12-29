package consulo.php.impl.ide.presentation;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.navigation.ItemPresentation;
import consulo.navigation.ItemPresentationProvider;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.util.PhpModuleExtensionUtil;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
@ExtensionImpl
public class PhpClassPresentation implements ItemPresentationProvider<PhpClass>
{
	@Nonnull
	@Override
	public Class<PhpClass> getItemClass()
	{
		return PhpClass.class;
	}

	@Nonnull
	@Override
	public ItemPresentation getPresentation(final PhpClass phpClass)
	{
		return new ItemPresentation()
		{
 			@Nullable
			@Override
			public String getPresentableText()
			{
				return phpClass.getName();
			}

			@Nullable
			@Override
			@RequiredReadAction
			public String getLocationString()
			{
				Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(phpClass);

				String location = null;
				if(moduleForPsiElement != null)
				{
					if(PhpModuleExtensionUtil.getLanguageLevel(moduleForPsiElement).isAtLeast(PhpLanguageLevel.PHP_5_3))
					{
						location = phpClass.getNamespaceName();

						if(!StringUtil.isEmpty(location))
						{
							location = "(" + location + ")";
						}
					}
				}
				return location;
			}

			@Nullable
			@Override
			public Image getIcon()
			{
				return IconDescriptorUpdaters.getIcon(phpClass, 0);
			}
		};
	}
}
