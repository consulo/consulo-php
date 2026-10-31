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

import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.Task;
import consulo.externalSystem.importing.ImportSpecBuilder;
import consulo.externalSystem.model.task.ProgressExecutionMode;
import consulo.externalSystem.service.project.ExternalSystemProjectRefresher;
import consulo.fileEditor.FileEditorManager;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.ComposerUtil;
import consulo.php.composer.localize.ComposerLocalize;
import consulo.php.composer.setting.ComposerExecutionSettings;
import consulo.php.composer.setting.ComposerProjectSettings;
import consulo.php.composer.setting.ComposerSettings;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.ProcessOutputTypes;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.project.Project;
import consulo.project.ui.notification.NotificationService;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creates the Composer project of the new project wizard in the background - a minimal {@code composer.json}, or
 * {@code composer create-project <package>} of a template - and then links and imports it through the external system.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public final class ComposerProjectCreator {
    private static final Logger LOG = Logger.getInstance(ComposerProjectCreator.class);

    /**
     * @param packageName      the template package, {@code null} for an empty project
     * @param requiredPackages required into the project created from the template
     */
    public record Request(String name,
                          @Nullable String packageName,
                          List<String> requiredPackages,
                          @Nullable String composerExecutable,
                          @Nullable String phpExecutable) {
    }

    private ComposerProjectCreator() {
    }

    /**
     * Must be called once the project is initialized; the work runs in a background task with a progress.
     */
    public static void create(Project project, VirtualFile contentRoot, Request request) {
        Task.Backgroundable.queue(project, ComposerLocalize.newProjectCreating(), indicator -> {
            Path directory = contentRoot.toNioPath();
            // an existing package in the chosen directory is imported as it is
            if (!Files.isRegularFile(directory.resolve(ComposerConstants.COMPOSER_JSON))) {
                String failure = request.packageName() == null
                    ? writeEmptyProject(directory, request.name())
                    : createProject(directory, request, indicator);
                if (failure != null) {
                    notifyFailure(project, failure);
                    return;
                }
            }
            contentRoot.refresh(false, true);
            project.getApplication().invokeLater(() -> {
                if (project.isDisposed()) {
                    return;
                }
                linkAndImport(project, contentRoot.getPath(), request.composerExecutable());
                VirtualFile composerJson = contentRoot.findChild(ComposerConstants.COMPOSER_JSON);
                if (composerJson != null) {
                    FileEditorManager.getInstance(project).openFile(composerJson, true);
                }
            });
        });
    }

    /**
     * @return the failure, {@code null} on success
     */
    public static @Nullable String writeEmptyProject(Path directory, String name) {
        try {
            Files.createDirectories(directory.resolve(ComposerConstants.DEFAULT_SOURCE_DIRECTORY));
            Files.writeString(directory.resolve(ComposerConstants.COMPOSER_JSON), createEmptyComposerJson(name), StandardCharsets.UTF_8);
            return null;
        }
        catch (IOException e) {
            LOG.warn("Cannot write composer.json to " + directory, e);
            return String.valueOf(e.getMessage());
        }
    }

    /**
     * A {@code composer.json} with a PSR-4 autoload of {@code App\} from {@code src/}
     */
    public static String createEmptyComposerJson(String name) {
        return """
            {
                "name": "app/%s",
                "type": "project",
                "require": {},
                "autoload": {
                    "psr-4": {
                        "App\\\\": "src/"
                    }
                }
            }
            """.formatted(toPackageName(name));
    }

    /**
     * The name part of a package name: lowercase letters, digits and {@code _.-}
     */
    public static String toPackageName(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            boolean allowed = c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_' || c == '.' || c == '-';
            builder.append(allowed ? c : '-');
        }
        String result = builder.toString().replaceAll("-{2,}", "-").replaceAll("^[-_.]+|[-_.]+$", "");
        return result.isEmpty() ? "project" : result;
    }

    /**
     * {@code composer create-project} refuses a directory which is not empty (the new project has its configuration
     * there already): the project is created next to it and moved in.
     *
     * @return the failure, {@code null} on success
     */
    private static @Nullable String createProject(Path directory, Request request, ProgressIndicator indicator) {
        String packageName = request.packageName();
        assert packageName != null;

        Path parent = directory.toAbsolutePath().getParent();
        Path temporary = null;
        try {
            temporary = parent == null
                ? Files.createTempDirectory("composer-create-project")
                : Files.createTempDirectory(parent, "." + directory.getFileName() + "-composer");
            Path target = temporary.resolve("project");

            ComposerExecutionSettings settings = new ComposerExecutionSettings(
                request.composerExecutable() == null ? ComposerConstants.DEFAULT_EXECUTABLE : request.composerExecutable(),
                request.phpExecutable()
            );

            List<String> createArguments = List.of("create-project", packageName, target.toString());
            String failure = run(settings, temporary, createArguments, indicator);
            if (failure == null && !request.requiredPackages().isEmpty()) {
                List<String> requireArguments = new ArrayList<>();
                requireArguments.add("require");
                requireArguments.addAll(request.requiredPackages());
                failure = run(settings, target, requireArguments, indicator);
            }
            if (failure != null) {
                return failure;
            }

            moveChildren(target, directory);
            return null;
        }
        catch (IOException e) {
            LOG.warn("composer create-project " + packageName + " failed in " + directory, e);
            return String.valueOf(e.getMessage());
        }
        finally {
            if (temporary != null) {
                FileUtil.delete(temporary);
            }
        }
    }

    private static @Nullable String run(ComposerExecutionSettings settings,
                                        Path workDirectory,
                                        List<String> arguments,
                                        ProgressIndicator indicator) {
        GeneralCommandLine commandLine = ComposerUtil.createCommandLine(settings, workDirectory, arguments);
        String presentation = ComposerUtil.toPresentation(arguments);
        ProcessHandler processHandler;
        try {
            processHandler = ProcessHandlerBuilder.create(commandLine).killable().build();
        }
        catch (ExecutionException e) {
            return ComposerLocalize.errorCannotRun(presentation, String.valueOf(e.getMessage())).get();
        }

        indicator.setText(LocalizeValue.of(presentation));
        // filled on the output reader thread
        StringBuffer errors = new StringBuffer();
        AtomicInteger exitCode = new AtomicInteger(-1);
        processHandler.addProcessListener(new ProcessListener() {
            @Override
            public void onTextAvailable(ProcessEvent event, Key outputType) {
                String text = event.getText().strip();
                if (!text.isEmpty() && (outputType == ProcessOutputTypes.STDOUT || outputType == ProcessOutputTypes.STDERR)) {
                    indicator.setText2(LocalizeValue.of(text));
                }
                if (outputType == ProcessOutputTypes.STDERR) {
                    errors.append(event.getText());
                }
            }

            @Override
            public void processTerminated(ProcessEvent event) {
                exitCode.set(event.getExitCode());
            }
        });

        processHandler.startNotify();
        while (!processHandler.waitFor(200)) {
            if (indicator.isCanceled()) {
                processHandler.destroyProcess();
                processHandler.waitFor();
                return ComposerLocalize.errorCancelled(presentation).get();
            }
        }

        int code = exitCode.get();
        if (code != 0) {
            String output = errors.toString().strip();
            return output.isEmpty() ? ComposerLocalize.errorCommandFailed(presentation, code).get() : output;
        }
        return null;
    }

    /**
     * Moves the created project into the directory of the new project; what is there already stays
     */
    private static void moveChildren(Path from, Path to) throws IOException {
        try (DirectoryStream<Path> children = Files.newDirectoryStream(from)) {
            for (Path child : children) {
                Path target = to.resolve(child.getFileName().toString());
                if (Files.exists(target)) {
                    LOG.warn("Not moving " + child + " - " + target + " exists already");
                    continue;
                }
                Files.move(child, target);
            }
        }
    }

    /**
     * Links the package to Composer and imports it - as linking a {@code composer.json} in its tool window does.
     */
    private static void linkAndImport(Project project, String path, @Nullable String composerExecutable) {
        if (ComposerUtil.findProjectSettings(project, path) == null) {
            ComposerProjectSettings projectSettings = new ComposerProjectSettings();
            projectSettings.setExternalProjectPath(path);
            projectSettings.setComposerExecutable(composerExecutable);
            ComposerSettings.getInstance(project).linkProject(projectSettings);
        }
        ExternalSystemProjectRefresher.getInstance().refreshProjects(
            new ImportSpecBuilder(project, ComposerConstants.SYSTEM_ID)
                .forceWhenUptodate()
                .use(ProgressExecutionMode.IN_BACKGROUND_ASYNC)
        );
    }

    private static void notifyFailure(Project project, String reason) {
        NotificationService.getInstance().newError(ComposerNotificationGroupContributor.GROUP)
            .title(ComposerLocalize.systemName())
            .content(ComposerLocalize.newProjectFailed(reason))
            .notify(project);
    }
}
