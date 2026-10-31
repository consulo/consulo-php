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

import consulo.application.ReadAction;
import consulo.externalSystem.model.DataNode;
import consulo.externalSystem.model.ProjectKeys;
import consulo.externalSystem.model.project.ContentRootData;
import consulo.externalSystem.model.project.ModuleData;
import consulo.externalSystem.model.task.ExternalSystemTaskId;
import consulo.externalSystem.model.task.ExternalSystemTaskNotificationEvent;
import consulo.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import consulo.externalSystem.model.task.TaskData;
import consulo.externalSystem.rt.model.ExternalSystemException;
import consulo.externalSystem.rt.model.ExternalSystemSourceType;
import consulo.externalSystem.service.project.ExternalSystemProjectResolver;
import consulo.externalSystem.service.project.ProjectData;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.ComposerJson;
import consulo.php.composer.ComposerJsonReader;
import consulo.php.composer.ComposerUtil;
import consulo.php.composer.importer.ComposerPackage;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.php.composer.setting.ComposerExecutionSettings;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static consulo.php.composer.ComposerConstants.SYSTEM_ID;

/**
 * Turns a {@code composer.json} into one module: content root = package directory, the PSR-4 {@code autoload}
 * directories as source roots ({@code src/} without any), the {@code autoload-dev} ones as test roots, and generated
 * directories excluded - {@code vendor/} itself stays, PHP needs the vendor classes. The {@code composer.json} is read
 * through its JOM model, nothing has to be installed.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerProjectResolver implements ExternalSystemProjectResolver<ComposerExecutionSettings> {
    @Override
    public @Nullable DataNode<ProjectData> resolveProjectInfo(ExternalSystemTaskId id,
                                                              String projectPath,
                                                              boolean isPreviewMode,
                                                              @Nullable ComposerExecutionSettings settings,
                                                              ExternalSystemTaskNotificationListener listener)
        throws ExternalSystemException, IllegalArgumentException, IllegalStateException {
        Path packageDirectory = ComposerUtil.toPackageDirectory(Path.of(projectPath));
        Path composerJsonPath = packageDirectory.resolve(ComposerConstants.COMPOSER_JSON);
        if (!Files.isRegularFile(composerJsonPath)) {
            throw new IllegalArgumentException(ComposerLocalize.errorNoComposerJson(packageDirectory).get());
        }

        Project project = id.findProject();
        if (project == null) {
            throw new IllegalStateException("No project for " + id);
        }

        listener.onStatusChange(new ExternalSystemTaskNotificationEvent(id, ComposerLocalize.statusReading().get()));

        VirtualFile composerJson = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(composerJsonPath);
        if (composerJson == null) {
            throw new IllegalArgumentException(ComposerLocalize.errorNoComposerJson(packageDirectory).get());
        }

        Resolved resolved = ReadAction.compute(() -> {
            Module module = ComposerUtil.findModule(project, packageDirectory);
            return new Resolved(ComposerJsonReader.readJson(project, composerJson), module == null ? null : module.getName());
        });
        ComposerJson json = resolved.json();
        if (json == null) {
            throw new ExternalSystemException(ComposerLocalize.errorCannotRead(composerJsonPath).get());
        }

        // a module already over the package (like the one of the new project wizard) is taken over, not doubled
        String moduleName = resolved.moduleName();
        if (moduleName == null) {
            moduleName = String.valueOf(packageDirectory.getFileName());
        }
        return createProjectNode(packageDirectory, projectPath, moduleName, json);
    }

    @Override
    public boolean cancelTask(ExternalSystemTaskId taskId, ExternalSystemTaskNotificationListener listener) {
        return false;
    }

    public static DataNode<ProjectData> createProjectNode(Path packageDirectory, String linkedProjectPath, String moduleName, ComposerJson json) {
        String directory = packageDirectory.toString();
        ComposerPackage composerPackage = json.composerPackage();

        ProjectData projectData = new ProjectData(SYSTEM_ID, moduleName, directory, linkedProjectPath);
        DataNode<ProjectData> projectNode = new DataNode<>(ProjectKeys.PROJECT, projectData, null);

        Set<String> taskNames = new LinkedHashSet<>();
        for (String command : ComposerConstants.COMMAND_TASKS) {
            taskNames.add(command);
            projectNode.createChild(ProjectKeys.TASK, new TaskData(SYSTEM_ID, command, linkedProjectPath, describeCommand(command).get()));
        }
        for (Map.Entry<String, List<String>> script : json.scripts().entrySet()) {
            if (taskNames.add(script.getKey())) {
                String description = String.join(" && ", script.getValue());
                projectNode.createChild(ProjectKeys.TASK, new TaskData(SYSTEM_ID, script.getKey(), linkedProjectPath, description));
            }
        }

        ModuleData moduleData = new ModuleData(moduleName, SYSTEM_ID, moduleName, directory, linkedProjectPath);
        DataNode<ModuleData> moduleNode = projectNode.createChild(ProjectKeys.MODULE, moduleData);

        ContentRootData contentRoot = new ContentRootData(SYSTEM_ID, directory);
        Set<Path> sources = autoloadDirectories(packageDirectory, composerPackage.psr4Autoload());
        if (composerPackage.psr4Autoload().isEmpty()) {
            sources.add(packageDirectory.resolve(ComposerConstants.DEFAULT_SOURCE_DIRECTORY));
        }
        for (Path source : sources) {
            contentRoot.storePath(ExternalSystemSourceType.SOURCE, source.toString());
        }
        for (Path test : autoloadDirectories(packageDirectory, composerPackage.psr4AutoloadDev())) {
            if (!sources.contains(test)) {
                contentRoot.storePath(ExternalSystemSourceType.TEST, test.toString());
            }
        }
        for (String excluded : ComposerConstants.EXCLUDED_DIRECTORIES) {
            contentRoot.storePath(ExternalSystemSourceType.EXCLUDED, packageDirectory.resolve(excluded).toString());
        }
        moduleNode.createChild(ProjectKeys.CONTENT_ROOT, contentRoot);

        moduleNode.createChild(ComposerPackageData.KEY, new ComposerPackageData(composerPackage, directory));
        return projectNode;
    }

    public static LocalizeValue describeCommand(String command) {
        return switch (command) {
            case ComposerConstants.TASK_INSTALL -> ComposerLocalize.taskInstallDescription();
            case ComposerConstants.TASK_UPDATE -> ComposerLocalize.taskUpdateDescription();
            case ComposerConstants.TASK_DUMP_AUTOLOAD -> ComposerLocalize.taskDumpAutoloadDescription();
            default -> LocalizeValue.empty();
        };
    }

    /**
     * The existing directories of a PSR-4 autoload inside the package
     */
    private static Set<Path> autoloadDirectories(Path packageDirectory, Map<String, List<String>> psr4) {
        Set<Path> result = new LinkedHashSet<>();
        for (List<String> directories : psr4.values()) {
            for (String directory : directories) {
                String relative = StringUtil.trimEnd(StringUtil.trimStart(directory, "./"), "/");
                if (relative.isEmpty()) {
                    continue;
                }
                Path path = packageDirectory.resolve(relative).normalize();
                if (path.startsWith(packageDirectory) && Files.isDirectory(path)) {
                    result.add(path);
                }
            }
        }
        return result;
    }

    private record Resolved(@Nullable ComposerJson json, @Nullable String moduleName) {
    }
}
