package consulo.php.module;

import java.util.Set;

import javax.annotation.Nonnull;

import com.intellij.openapi.roots.ModifiableRootModel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.roots.ContentFolderSupportPatcher;
import consulo.roots.ContentFolderTypeProvider;
import consulo.roots.impl.ProductionContentFolderTypeProvider;

/**
 * @author VISTALL
 * @since 2019-04-16
 */
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
