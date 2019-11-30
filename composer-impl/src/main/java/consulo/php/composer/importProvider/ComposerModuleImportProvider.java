package consulo.php.composer.importProvider;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTable;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.moduleImport.ModuleImportContext;
import consulo.moduleImport.ModuleImportProvider;
import consulo.php.composer.ComposerFileTypeFactory;
import consulo.php.composer.ComposerIcons;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.roots.impl.ProductionContentFolderTypeProvider;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2019-04-24
 */
public class ComposerModuleImportProvider implements ModuleImportProvider<ModuleImportContext>
{
	@Nonnull
	@Override
	public String getName()
	{
		return "Composer";
	}

	@Nonnull
	@Override
	public String getFileSample()
	{
		return "<b>Composer</b> project";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return ComposerIcons.Composer;
	}

	@Override
	public boolean canImport(@Nonnull File file)
	{
		return new File(file, ComposerFileTypeFactory.COMPOSER_JSON).exists();
	}

	@RequiredReadAction
	@Override
	public void process(@Nonnull ModuleImportContext context, @Nonnull Project project, @Nonnull ModifiableModuleModel modifiableModuleModel, @Nonnull Consumer<Module> consumer)
	{
		String fileToImport = context.getFileToImport();

		File targetDirectory = new File(fileToImport);

		VirtualFile targetVFile = LocalFileSystem.getInstance().findFileByIoFile(targetDirectory);

		assert targetVFile != null;

		Module rootModule = modifiableModuleModel.newModule(targetDirectory.getName(), targetDirectory.getPath());
		consumer.accept(rootModule);

		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(rootModule);

		ModifiableRootModel modifiableModel = moduleRootManager.getModifiableModel();

		ContentEntry contentEntry = modifiableModel.addContentEntry(targetVFile);
		contentEntry.addFolder(targetVFile.getUrl() + "/src", ProductionContentFolderTypeProvider.getInstance());

		Sdk sdk = ContainerUtil.getFirstItem(SdkTable.getInstance().getSdksOfType(PhpSdkType.getInstance()));

		PhpMutableModuleExtension<?> phpModuleExtension = modifiableModel.getExtensionWithoutCheck(PhpMutableModuleExtension.class);
		assert phpModuleExtension != null;
		phpModuleExtension.setEnabled(true);
		phpModuleExtension.getInheritableSdk().set(null, sdk);

		modifiableModel.addModuleExtensionSdkEntry(phpModuleExtension);

		WriteAction.run(modifiableModel::commit);
	}
}
