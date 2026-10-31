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

package consulo.php.it;

import consulo.application.Application;
import consulo.application.ReadAction;
import consulo.content.ContentFolderTypeProvider;
import consulo.content.base.ExcludedContentFolderTypeProvider;
import consulo.content.bundle.Sdk;
import consulo.externalSystem.model.DataNode;
import consulo.externalSystem.model.ProjectKeys;
import consulo.externalSystem.model.project.ContentRootData;
import consulo.externalSystem.model.project.ModuleData;
import consulo.externalSystem.model.task.ExternalSystemTaskId;
import consulo.externalSystem.model.task.ExternalSystemTaskNotificationListenerAdapter;
import consulo.externalSystem.model.task.ExternalSystemTaskType;
import consulo.externalSystem.model.task.TaskData;
import consulo.externalSystem.rt.model.ExternalSystemSourceType;
import consulo.externalSystem.service.project.ProjectData;
import consulo.externalSystem.service.project.manage.ProjectDataManager;
import consulo.externalSystem.util.DisposeAwareProjectChange;
import consulo.externalSystem.util.ExternalSystemApiUtil;
import consulo.it.HeadlessProjectExtension;
import consulo.it.HeadlessProjects;
import consulo.language.content.ProductionContentFolderTypeProvider;
import consulo.language.content.TestContentFolderTypeProvider;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ContentFolder;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.importer.ComposerPackage;
import consulo.php.composer.newProject.ComposerProjectTemplate;
import consulo.php.composer.project.ComposerPackageData;
import consulo.php.composer.project.ComposerProjectResolver;
import consulo.php.composer.task.ComposerTaskManager;
import consulo.php.composer.wizard.ComposerProjectCreator;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Composer as an external system over a real project: resolving {@code composer.json} into the module data and
 * importing it, the framework importers and the templates of the new project wizard. No {@code composer} runs.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtendWith(HeadlessProjectExtension.class)
public class ComposerIntegrationTest {
    private static final String COMPOSER_JSON = """
        {
            "name": "acme/shop",
            "type": "project",
            "require": {
                "php": ">=8.2",
                "acme/framework": "^1.0"
            },
            "require-dev": {
                "phpunit/phpunit": "^11.0"
            },
            "autoload": {
                "psr-4": {
                    "App\\\\": "src/",
                    "Acme\\\\Lib\\\\": ["lib/", "missing/"]
                }
            },
            "autoload-dev": {
                "psr-4": {
                    "App\\\\Tests\\\\": "tests/"
                }
            },
            "scripts": {
                "cache:clear": "bin/console cache:clear",
                "test": ["@cache:clear", "phpunit"]
            }
        }
        """;

    @Test
    public void resolveBuildsModuleWithAutoloadRoots(HeadlessProjects projects) throws Exception {
        Path directory = createPackage(projects, COMPOSER_JSON);
        Project project = projects.open(directory);

        DataNode<ProjectData> projectNode = resolve(project, directory);

        String directoryName = directory.getFileName().toString();
        assertThat(projectNode.getData().getOwner()).isEqualTo(ComposerConstants.SYSTEM_ID);

        DataNode<ModuleData> moduleNode = ExternalSystemApiUtil.find(projectNode, ProjectKeys.MODULE);
        assertThat(moduleNode).isNotNull();
        assertThat(moduleNode.getData().getExternalName()).isEqualTo(directoryName);

        DataNode<ContentRootData> contentRootNode = ExternalSystemApiUtil.find(moduleNode, ProjectKeys.CONTENT_ROOT);
        assertThat(contentRootNode).isNotNull();
        ContentRootData contentRoot = contentRootNode.getData();
        assertThat(paths(contentRoot, ExternalSystemSourceType.SOURCE)).containsExactlyInAnyOrder(path(directory, "src"), path(directory, "lib"));
        assertThat(paths(contentRoot, ExternalSystemSourceType.TEST)).containsExactly(path(directory, "tests"));
        assertThat(paths(contentRoot, ExternalSystemSourceType.EXCLUDED))
            .containsExactlyInAnyOrder(path(directory, "vendor/composer"), path(directory, "var/cache"))
            .doesNotContain(path(directory, "vendor"));

        DataNode<ComposerPackageData> packageNode = ExternalSystemApiUtil.find(moduleNode, ComposerPackageData.KEY);
        assertThat(packageNode).isNotNull();
        ComposerPackage composerPackage = packageNode.getData().getPackage();
        assertThat(composerPackage.name()).isEqualTo("acme/shop");
        assertThat(composerPackage.requires("acme/framework")).isTrue();
        assertThat(composerPackage.requires("phpunit/phpunit")).isTrue();
        assertThat(composerPackage.psr4Autoload()).containsEntry("Acme\\Lib\\", List.of("lib/", "missing/"));

        List<String> tasks = new ArrayList<>();
        for (DataNode<TaskData> task : ExternalSystemApiUtil.findAll(projectNode, ProjectKeys.TASK)) {
            tasks.add(task.getData().getName());
        }
        assertThat(tasks).containsExactly("install", "update", "dump-autoload", "cache:clear", "test");
    }

    @Test
    public void scriptsRunThroughRunScript() {
        assertThat(ComposerTaskManager.toArguments("install")).containsExactly("install");
        assertThat(ComposerTaskManager.toArguments("dump-autoload")).containsExactly("dump-autoload");
        assertThat(ComposerTaskManager.toArguments("cache:clear")).containsExactly("run-script", "cache:clear");
    }

    /**
     * Imported as the platform imports a refreshed project: the module with its source roots, the PHP extension with
     * the PHP SDK, and the framework importers run on its model
     */
    @Test
    public void importConfiguresModule(HeadlessProjects projects) throws Exception {
        Sdk phpSdk = PhpProjects.createPhpSdk();
        Path directory = createPackage(projects, COMPOSER_JSON);
        Project project = projects.open(directory);
        TestComposerImporter.PROCESSED.clear();

        DataNode<ProjectData> projectNode = resolve(project, directory);
        importData(project, projectNode);
        HeadlessProjects.awaitIdle(project);

        String moduleName = directory.getFileName().toString();
        ReadAction.run(() -> {
            Module module = ModuleManager.getInstance(project).findModuleByName(moduleName);
            assertThat(module).isNotNull();

            ContentEntry[] contentEntries = ModuleRootManager.getInstance(module).getContentEntries();
            assertThat(contentEntries).hasSize(1);
            assertThat(folders(contentEntries[0], ProductionContentFolderTypeProvider.getInstance()))
                .containsExactlyInAnyOrder(url(directory, "src"), url(directory, "lib"));
            assertThat(folders(contentEntries[0], TestContentFolderTypeProvider.getInstance())).containsExactly(url(directory, "tests"));
            assertThat(folders(contentEntries[0], ExcludedContentFolderTypeProvider.getInstance())).contains(url(directory, "vendor/composer"));

            PhpModuleExtension<?> extension = module.getExtension(PhpModuleExtension.class);
            assertThat(extension).isNotNull();
            assertThat(extension.getSdk()).isNotNull();
            assertThat(extension.getSdk().getSdkType()).isEqualTo(phpSdk.getSdkType());
        });

        assertThat(TestComposerImporter.PROCESSED).containsExactly(moduleName + ": acme/shop @ " + moduleName);
    }

    @Test
    public void importerIsNotAppliedWithoutItsPackage(HeadlessProjects projects) throws Exception {
        Path directory = createPackage(projects, """
            {
                "name": "acme/plain",
                "require": {"php": ">=8.2"}
            }
            """);
        Project project = projects.open(directory);
        TestComposerImporter.PROCESSED.clear();

        DataNode<ProjectData> projectNode = resolve(project, directory);
        importData(project, projectNode);

        Module module = ReadAction.compute(() -> ModuleManager.getInstance(project).findModuleByName(directory.getFileName().toString()));
        assertThat(module).isNotNull();
        assertThat(TestComposerImporter.PROCESSED).isEmpty();
    }

    /**
     * The {@code composer.json} of an empty project of the new project wizard: {@code src/} as the source root
     */
    @Test
    public void emptyProjectOfTheWizard(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        assertThat(ComposerProjectCreator.writeEmptyProject(directory, "My Shop!")).isNull();
        Project project = projects.open(directory);

        DataNode<ProjectData> projectNode = resolve(project, directory);

        DataNode<ModuleData> moduleNode = ExternalSystemApiUtil.find(projectNode, ProjectKeys.MODULE);
        assertThat(moduleNode).isNotNull();
        DataNode<ComposerPackageData> packageNode = ExternalSystemApiUtil.find(moduleNode, ComposerPackageData.KEY);
        assertThat(packageNode).isNotNull();
        assertThat(packageNode.getData().getPackage().name()).isEqualTo("app/my-shop");
        assertThat(packageNode.getData().getPackage().psr4Autoload()).containsEntry("App\\", List.of("src/"));

        DataNode<ContentRootData> contentRootNode = ExternalSystemApiUtil.find(moduleNode, ProjectKeys.CONTENT_ROOT);
        assertThat(contentRootNode).isNotNull();
        assertThat(paths(contentRootNode.getData(), ExternalSystemSourceType.SOURCE)).containsExactly(path(directory, "src"));
    }

    @Test
    public void packageNames() {
        assertThat(ComposerProjectCreator.toPackageName("My Shop!")).isEqualTo("my-shop");
        assertThat(ComposerProjectCreator.toPackageName("shop_2.0")).isEqualTo("shop_2.0");
        assertThat(ComposerProjectCreator.toPackageName("???")).isEqualTo("project");
    }

    @Test
    public void templatesAreListed(HeadlessProjects projects) {
        List<ComposerProjectTemplate> templates = Application.get().getExtensionList(ComposerProjectTemplate.class);
        assertThat(templates).anySatisfy(template -> {
            assertThat(template.getId()).isEqualTo(TestComposerProjectTemplate.ID);
            assertThat(template.getPackageName()).isEqualTo("acme/skeleton");
            assertThat(template.getRequiredPackages()).isEmpty();
        });
    }

    private static Path createPackage(HeadlessProjects projects, String composerJson) throws Exception {
        Path directory = projects.newDirectory();
        Files.writeString(directory.resolve(ComposerConstants.COMPOSER_JSON), composerJson);
        for (String child : List.of("src", "lib", "tests", "vendor/composer", "vendor/acme/framework/src")) {
            Files.createDirectories(directory.resolve(child));
        }
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory);
        return directory;
    }

    private static DataNode<ProjectData> resolve(Project project, Path directory) {
        ExternalSystemTaskId id = ExternalSystemTaskId.create(ComposerConstants.SYSTEM_ID, ExternalSystemTaskType.RESOLVE_PROJECT, project);
        DataNode<ProjectData> projectNode = new ComposerProjectResolver().resolveProjectInfo(
            id,
            directory.toString(),
            false,
            null,
            new ExternalSystemTaskNotificationListenerAdapter() {
            }
        );
        assertThat(projectNode).isNotNull();
        return projectNode;
    }

    /**
     * What the platform does with a resolved project: the data services in a project change (write) action
     */
    private static void importData(Project project, DataNode<ProjectData> projectNode) {
        ProjectDataManager dataManager = Application.get().getInstance(ProjectDataManager.class);
        ExternalSystemApiUtil.executeProjectChangeAction(true, new DisposeAwareProjectChange(project) {
            @Override
            public void execute() {
                dataManager.importData(projectNode.getKey(), Collections.singleton(projectNode), project, true);
            }
        });
    }

    private static List<String> paths(ContentRootData contentRoot, ExternalSystemSourceType type) {
        List<String> result = new ArrayList<>();
        for (ContentRootData.SourceRoot root : contentRoot.getPaths(type)) {
            result.add(root.getPath());
        }
        return result;
    }

    private static String path(Path directory, String relative) {
        return ExternalSystemApiUtil.toCanonicalPath(directory.resolve(relative).toString());
    }

    private static List<String> folders(ContentEntry contentEntry, ContentFolderTypeProvider type) {
        List<String> result = new ArrayList<>();
        for (ContentFolder folder : contentEntry.getFolders(folderType -> folderType == type)) {
            result.add(folder.getUrl());
        }
        return result;
    }

    private static String url(Path directory, String relative) {
        return "file://" + ExternalSystemApiUtil.toCanonicalPath(directory.resolve(relative).toString());
    }
}
