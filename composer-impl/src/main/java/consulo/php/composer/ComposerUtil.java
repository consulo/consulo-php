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

package consulo.php.composer;

import consulo.annotation.access.RequiredReadAction;
import consulo.application.Application;
import consulo.application.ReadAction;
import consulo.application.platform.PlatformTarget;
import consulo.application.platform.PlatformTargetManager;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.php.composer.setting.ComposerExecutionSettings;
import consulo.php.composer.setting.ComposerProjectSettings;
import consulo.php.composer.setting.ComposerSettings;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.platform.Platform;
import consulo.process.cmd.GeneralCommandLine;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-31
 */
public final class ComposerUtil {
    private ComposerUtil() {
    }

    /**
     * A package may be linked by its directory or by its {@code composer.json} - both name the same settings.
     */
    public static @Nullable ComposerProjectSettings findProjectSettings(Project project, String packagePath) {
        Path packageDirectory = toPackageDirectory(toPath(project, packagePath));
        for (ComposerProjectSettings settings : ComposerSettings.getInstance(project).getLinkedProjectsSettings()) {
            String linkedProjectPath = settings.getExternalProjectPath();
            if (linkedProjectPath != null && toPackageDirectory(toPath(project, linkedProjectPath)).equals(packageDirectory)) {
                return settings;
            }
        }
        return null;
    }

    /**
     * @param fileOrDirectory {@code composer.json} or the directory holding it
     */
    public static Path toPackageDirectory(Path fileOrDirectory) {
        if (Files.isDirectory(fileOrDirectory)) {
            return fileOrDirectory;
        }
        Path fileName = fileOrDirectory.getFileName();
        if (fileName != null && ComposerConstants.COMPOSER_JSON.equals(fileName.toString())) {
            Path parent = fileOrDirectory.getParent();
            return parent == null ? fileOrDirectory : parent;
        }
        return fileOrDirectory;
    }

    public static ComposerExecutionSettings createExecutionSettings(Project project, String linkedProjectPath) {
        ComposerProjectSettings settings = findProjectSettings(project, linkedProjectPath);
        String composerExecutable = settings == null ? null : settings.getComposerExecutable();
        Path packageDirectory = toPackageDirectory(toPath(project, linkedProjectPath));
        Module module = ReadAction.compute(() -> findModule(project, packageDirectory));
        return new ComposerExecutionSettings(
            composerExecutable == null ? ComposerConstants.DEFAULT_EXECUTABLE : composerExecutable,
            findPhpExecutable(module)
        );
    }

    /**
     * The module whose content root is the package directory
     */
    @RequiredReadAction
    public static @Nullable Module findModule(Project project, Path packageDirectory) {
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            for (VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots()) {
                if (contentRoot.isInLocalLikeFileSystem() && contentRoot.toNioPath().equals(packageDirectory)) {
                    return module;
                }
            }
        }
        return null;
    }

    /**
     * The interpreter of the PHP SDK of the module, or of the first PHP SDK
     */
    public static @Nullable String findPhpExecutable(@Nullable Module module) {
        PhpModuleExtension<?> extension = module == null ? null : module.getExtension(PhpModuleExtension.class);
        Sdk sdk = extension == null ? null : extension.getSdk();
        if (sdk == null) {
            sdk = ContainerUtil.getFirstItem(SdkTable.getInstance().getSdksOfType(PhpSdkType.getInstance()));
        }
        return sdk == null || sdk.getHomePath() == null ? null : PhpSdkType.getExecutable(sdk).toString();
    }

    /**
     * {@code composer <arguments>} in the package directory; a {@code composer.phar} runs with the PHP interpreter
     */
    public static GeneralCommandLine createCommandLine(ComposerExecutionSettings settings, Path workDirectory, List<String> arguments) {
        List<String> command = new ArrayList<>();
        String composer = settings.getComposerExecutable();
        String php = settings.getPhpExecutable();
        if (composer.endsWith(".phar") && php != null) {
            command.add(php);
        }
        command.add(composer);
        command.add("--no-interaction");
        command.addAll(arguments);

        GeneralCommandLine commandLine = new GeneralCommandLine(command);
        // on the machine of the package
        commandLine.withPlatform(platformOf(workDirectory));
        commandLine.withWorkingDirectory(workDirectory);
        commandLine.withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE);
        return commandLine;
    }

    /**
     * A path of the settings - a path of the machine of the project.
     */
    public static Path toPath(@Nullable Project project, String path) {
        Platform platform = project != null ? project.getPlatform() : Platform.current();
        return platform == Platform.current() ? Path.of(path) : platform.fs().getPath(path);
    }

    private static Platform platformOf(Path path) {
        if (path.getFileSystem() == FileSystems.getDefault()) {
            return Platform.current();
        }
        PlatformTarget target = Application.get().getInstance(PlatformTargetManager.class).findTargetOf(path);
        Platform platform = target == null ? null : target.getPlatform();
        return platform != null ? platform : Platform.current();
    }

    public static String toPresentation(List<String> arguments) {
        return "composer " + String.join(" ", arguments);
    }
}
