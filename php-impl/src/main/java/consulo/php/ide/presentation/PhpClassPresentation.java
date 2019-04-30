package consulo.php.ide.presentation;

import javax.annotation.Nullable;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.ide.IconDescriptorUpdaters;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpClassPresentation implements ItemPresentationProvider<NavigationItem>
{
	@Override
	public ItemPresentation getPresentation(final NavigationItem item)
	{
		PhpClass phpClass = (PhpClass) item;
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
			public String getLocationString()
			{
				Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(phpClass);

				String location = null;
				if(moduleForPsiElement != null)
				{
					PhpModuleExtension<?> extension = ModuleUtilCore.getExtension(moduleForPsiElement, PhpModuleExtension.class);
					if(extension != null && extension.getLanguageLevel().isAtLeast(PhpLanguageLevel.PHP_5_3))
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
