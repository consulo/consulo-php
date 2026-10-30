package consulo.php.composer.importProvider;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.concurrent.coroutine.ReadLock;
import consulo.application.concurrent.coroutine.WriteLock;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.content.ContentFolderTypeProvider;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.content.TestContentFolderTypeProvider;
import consulo.localize.LocalizeValue;
import consulo.module.ModifiableModuleModel;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.creation.importing.ModuleImportContext;
import consulo.module.creation.importing.ModuleImportProvider;
import consulo.php.composer.ComposerFileTypeFactory;
import consulo.php.composer.ComposerJsonReader;
import consulo.php.composer.importer.ComposerImporter;
import consulo.php.composer.importer.ComposerPackage;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.StringUtil;
import consulo.util.concurrent.coroutine.Coroutine;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public boolean canImport(@Nonnull Path file) {
        return Files.exists(file.resolve(ComposerFileTypeFactory.COMPOSER_JSON));
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

                VirtualFile composerJson = targetVFile.findChild(ComposerFileTypeFactory.COMPOSER_JSON);
                ComposerPackage composerPackage = composerJson == null ? null : ComposerJsonReader.read(project, composerJson);

                // source folders from the PSR-4 autoload, src/ when there is none
                if (composerPackage == null || composerPackage.psr4Autoload().isEmpty()) {
                    contentEntry.addFolder(targetVFile.getUrl() + "/src", ProductionContentFolderTypeProvider.getInstance());
                }
                else {
                    addFolders(contentEntry, targetVFile, composerPackage.psr4Autoload(), ProductionContentFolderTypeProvider.getInstance());
                    addFolders(contentEntry, targetVFile, composerPackage.psr4AutoloadDev(), TestContentFolderTypeProvider.getInstance());
                }

                Sdk sdk = ContainerUtil.getFirstItem(SdkTable.getInstance().getSdksOfType(PhpSdkType.getInstance()));

                PhpMutableModuleExtension<?> phpModuleExtension = modifiableModel.getExtensionWithoutCheck(PhpMutableModuleExtension.class);
                assert phpModuleExtension != null;
                phpModuleExtension.setEnabled(true);
                phpModuleExtension.getInheritableSdk().set(null, sdk);

                modifiableModel.addModuleExtensionSdkEntry(phpModuleExtension);

                // frameworks configure the module from the required packages, like Maven importers
                if (composerPackage != null) {
                    for (ComposerImporter importer : project.getApplication().getExtensionList(ComposerImporter.class)) {
                        if (importer.isApplicable(composerPackage)) {
                            importer.process(rootModule, modifiableModel, composerPackage, targetVFile);
                        }
                    }
                }

                return modifiableModel;
            })
            .toCoroutine()
            .then(WriteLock.apply((modifiableRootModel, continuation) -> {
                modifiableRootModel.commit();
                return null;
            }));
    }

    private static void addFolders(ContentEntry contentEntry,
                                   VirtualFile packageRoot,
                                   Map<String, List<String>> psr4,
                                   ContentFolderTypeProvider type) {
        Set<String> urls = new LinkedHashSet<>();
        for (List<String> directories : psr4.values()) {
            for (String directory : directories) {
                String relative = StringUtil.trimEnd(StringUtil.trimStart(directory, "./"), "/");
                if (relative.isEmpty()) {
                    continue;
                }
                VirtualFile folder = packageRoot.findFileByRelativePath(relative);
                if (folder != null && folder.isDirectory()) {
                    urls.add(folder.getUrl());
                }
            }
        }
        for (String url : urls) {
            contentEntry.addFolder(url, type);
        }
    }
}
