package consulo.php.impl.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileTemplate.FileTemplateContributor;
import consulo.fileTemplate.FileTemplateRegistrator;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 30-Aug-22
 */
@ExtensionImpl
public class PhpFileTemplateContributor implements FileTemplateContributor
{
	@Override
	public void register(@Nonnull FileTemplateRegistrator fileTemplateRegistrator)
	{
		fileTemplateRegistrator.registerInternalTemplate("PHP Class");
		fileTemplateRegistrator.registerInternalTemplate("PHP File");
		fileTemplateRegistrator.registerInternalTemplate("PHP Interface");
		fileTemplateRegistrator.registerInternalTemplate("Php Trait");
	}
}
