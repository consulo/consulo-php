/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.php.composer.project;

import consulo.annotation.access.RequiredWriteAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.externalSystem.model.DataNode;
import consulo.externalSystem.model.Key;
import consulo.externalSystem.model.ProjectKeys;
import consulo.externalSystem.model.project.ModuleData;
import consulo.externalSystem.service.project.manage.ProjectDataService;
import consulo.externalSystem.util.DisposeAwareProjectChange;
import consulo.externalSystem.util.ExternalSystemApiUtil;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.php.composer.importer.ComposerImporter;
import consulo.php.composer.importer.ComposerPackage;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;

/**
 * Configures an imported module from its {@code composer.json}: the PHP module extension is enabled (with the first
 * PHP SDK, unless the module has one) and the {@link ComposerImporter}s run on its model. The model is committed in the
 * project change (write) action of the data import, as the platform data services do - never on its own.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerPackageDataService implements ProjectDataService<ComposerPackageData, Module> {
    private static final Logger LOG = Logger.getInstance(ComposerPackageDataService.class);

    private final Application myApplication;

    @Inject
    public ComposerPackageDataService(Application application) {
        myApplication = application;
    }

    @Override
    public Key<ComposerPackageData> getTargetDataKey() {
        return ComposerPackageData.KEY;
    }

    @Override
    public void importData(Collection<DataNode<ComposerPackageData>> toImport, Project project, boolean synchronous) {
        if (toImport.isEmpty()) {
            return;
        }
        List<ComposerImporter> importers = myApplication.getExtensionList(ComposerImporter.class);
        ExternalSystemApiUtil.executeProjectChangeAction(synchronous, new DisposeAwareProjectChange(project) {
            @Override
            public void execute() {
                ModuleManager moduleManager = ModuleManager.getInstance(project);
                for (DataNode<ComposerPackageData> node : toImport) {
                    DataNode<ModuleData> moduleNode = ExternalSystemApiUtil.findParent(node, ProjectKeys.MODULE);
                    Module module = moduleNode == null ? null : moduleManager.findModuleByName(moduleNode.getData().getInternalName());
                    if (module == null) {
                        LOG.warn("Composer package is not imported, there is no module for " + node.getData().getPackageDirectory());
                        continue;
                    }
                    configure(module, node.getData(), importers);
                }
            }
        });
    }

    @Override
    public void removeData(Collection<? extends Module> toRemove, Project project, boolean synchronous) {
    }

    @RequiredWriteAction
    private static void configure(Module module, ComposerPackageData data, List<ComposerImporter> importers) {
        ModifiableRootModel model = ModuleRootManager.getInstance(module).getModifiableModel();
        try {
            PhpMutableModuleExtension<?> extension = model.getExtensionWithoutCheck(PhpMutableModuleExtension.class);
            if (extension != null) {
                extension.setEnabled(true);
                if (extension.getInheritableSdk().isNull()) {
                    Sdk sdk = ContainerUtil.getFirstItem(SdkTable.getInstance().getSdksOfType(PhpSdkType.getInstance()));
                    if (sdk != null) {
                        extension.getInheritableSdk().set(null, sdk);
                    }
                }
                if (model.findModuleExtensionSdkEntry(extension) == null) {
                    model.addModuleExtensionSdkEntry(extension);
                }
            }

            // frameworks configure the module from the required packages, like Maven importers
            ComposerPackage composerPackage = data.getPackage();
            VirtualFile packageRoot = LocalFileSystem.getInstance().findFileByPath(data.getPackageDirectory());
            if (packageRoot != null) {
                for (ComposerImporter importer : importers) {
                    if (importer.isApplicable(composerPackage)) {
                        importer.process(module, model, composerPackage, packageRoot);
                    }
                }
            }

            if (model.isChanged()) {
                model.commit();
            }
            else {
                model.dispose();
            }
        }
        catch (RuntimeException e) {
            if (!model.isDisposed()) {
                model.dispose();
            }
            throw e;
        }
    }
}
