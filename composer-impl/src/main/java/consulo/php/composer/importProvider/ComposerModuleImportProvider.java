package consulo.php.composer.importProvider;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.concurrent.coroutine.ReadLock;
import consulo.application.concurrent.coroutine.WriteLock;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.localize.LocalizeValue;
import consulo.module.ModifiableModuleModel;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.creation.importing.ModuleImportContext;
import consulo.module.creation.importing.ModuleImportProvider;
import consulo.php.composer.ComposerFileTypeFactory;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;
import consulo.util.concurrent.coroutine.Coroutine;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2019-04-24
 */
@ExtensionImpl
public class ComposerModuleImportProvider implements ModuleImportProvider<ModuleImportContext> {
    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Composer");
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return ComposerIconGroup.composer();
    }

    @Override
    public boolean canImport(@Nonnull File file) {
        return new File(file, ComposerFileTypeFactory.COMPOSER_JSON).exists();
    }

    @Override
    public Coroutine<Object, Object> process(@Nonnull ModuleImportContext context, @Nonnull Project project, @Nonnull ModifiableModuleModel modifiableModuleModel, @Nonnull Consumer<Module> consumer) {
        return ReadLock.apply((i, continuation) -> {
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

                return modifiableModel;
            })
            .toCoroutine()
            .then(WriteLock.apply((modifiableRootModel, continuation) -> {
                modifiableRootModel.commit();
                return null;
            }));
    }
}
