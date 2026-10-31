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

package consulo.php.composer.wizard;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.bundle.Sdk;
import consulo.externalSystem.model.ExternalSystemDataKeys;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.creation.scratch.NewModuleBuilder;
import consulo.module.creation.scratch.NewModuleBuilderProcessor;
import consulo.module.creation.scratch.NewModuleContext;
import consulo.module.creation.scratch.NewModuleContextGroup;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.ComposerUtil;
import consulo.php.composer.icon.ComposerIconGroup;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.php.composer.newProject.ComposerProjectTemplate;
import consulo.php.icon.PhpIconGroup;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.platform.Platform;
import consulo.project.Project;
import consulo.project.startup.StartupManager;
import consulo.ui.ex.wizard.WizardStep;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * New project/module items of the "PHP" group: "Composer" for an empty project (a minimal {@code composer.json}) and an item
 * per {@link ComposerProjectTemplate}, created by {@code composer create-project}. Once the project is initialized the package is
 * created in the background, then linked to Composer and imported - the import takes over the module of the wizard.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl(id = "composer")
public class ComposerNewModuleBuilder implements NewModuleBuilder {
    @Override
    public void setupContext(NewModuleContext context) {
        NewModuleContextGroup group = context.addGroup("php", ComposerLocalize.newProjectGroup(), PhpIconGroup.php());
        group.add(ComposerLocalize.newProjectItem(), ComposerIconGroup.composer(), createProcessor(null));
        for (ComposerProjectTemplate template : Application.get().getExtensionList(ComposerProjectTemplate.class)) {
            group.add(template.getName(), template.getIcon(), createProcessor(template));
        }
    }

    private static NewModuleBuilderProcessor<ComposerNewModuleWizardContext> createProcessor(@Nullable ComposerProjectTemplate template) {
        return new NewModuleBuilderProcessor<>() {
            @Override
            public ComposerNewModuleWizardContext createContext(boolean isNewProject) {
                return new ComposerNewModuleWizardContext(isNewProject, template);
            }

            @Override
            public void buildSteps(Consumer<WizardStep<ComposerNewModuleWizardContext>> consumer, ComposerNewModuleWizardContext context) {
                consumer.accept(new ComposerNewModuleSetupStep(context));
            }

            @RequiredReadAction
            @Override
            public void process(ComposerNewModuleWizardContext context, ContentEntry contentEntry, ModifiableRootModel modifiableRootModel) {
                setupModule(context, contentEntry, modifiableRootModel);
            }
        };
    }

    @RequiredReadAction
    private static void setupModule(ComposerNewModuleWizardContext context, ContentEntry contentEntry, ModifiableRootModel rootModel) {
        Sdk sdk = context.getPhpSdk();
        PhpMutableModuleExtension<?> extension = rootModel.getExtensionWithoutCheck(PhpMutableModuleExtension.class);
        if (extension != null) {
            extension.setEnabled(true);
            if (sdk != null) {
                extension.getInheritableSdk().set(null, sdk);
                rootModel.addModuleExtensionSdkEntry(extension);
            }
        }

        ComposerProjectTemplate template = context.getTemplate();
        if (template == null) {
            contentEntry.addFolder(contentEntry.getUrl() + "/" + ComposerConstants.DEFAULT_SOURCE_DIRECTORY, ProductionContentFolderTypeProvider.getInstance());
        }

        VirtualFile contentRoot = contentEntry.getFile();
        if (contentRoot == null) {
            return;
        }
        Project project = rootModel.getProject();
        // as the import of an existing package does: the Composer tool window is decided on when the project opens,
        // and the package is only linked once it exists
        project.putUserData(ExternalSystemDataKeys.NEWLY_IMPORTED_PROJECT, Boolean.TRUE);

        ComposerProjectCreator.Request request = new ComposerProjectCreator.Request(
            context.getName(),
            template == null ? null : template.getPackageName(),
            template == null ? List.of() : template.getRequiredPackages(),
            context.getComposerExecutable(),
            findPhpExecutable(sdk)
        );
        // this runs before the root model is committed (and for a new project, before it is open)
        StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> ComposerProjectCreator.create(project, contentRoot, request));
    }

    private static @Nullable String findPhpExecutable(@Nullable Sdk sdk) {
        String home = sdk == null ? null : sdk.getHomePath();
        if (home == null) {
            return ComposerUtil.findPhpExecutable(null);
        }
        return PhpSdkType.getExecutable(Platform.current(), Path.of(home)).toString();
    }
}
