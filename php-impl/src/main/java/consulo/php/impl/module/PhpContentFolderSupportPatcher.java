package consulo.php.impl.module;

import consulo.annotation.component.ExtensionImpl;
import consulo.content.ContentFolderTypeProvider;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.module.content.layer.ContentFolderSupportPatcher;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.php.module.extension.PhpModuleExtension;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2019-04-16
 */
@ExtensionImpl
public class PhpContentFolderSupportPatcher implements ContentFolderSupportPatcher
{
	@Override
	public void patch(@Nonnull ModifiableRootModel model, @Nonnull Set<ContentFolderTypeProvider> set)
	{
		PhpModuleExtension<?> extension = model.getExtension(PhpModuleExtension.class);
		if(extension != null)
		{
			set.add(ProductionContentFolderTypeProvider.getInstance());
		}
	}
}
