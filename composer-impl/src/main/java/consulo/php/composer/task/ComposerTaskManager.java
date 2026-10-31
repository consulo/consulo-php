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

package consulo.php.composer.task;

import consulo.externalSystem.model.task.ExternalSystemTaskId;
import consulo.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import consulo.externalSystem.rt.model.ExternalSystemException;
import consulo.externalSystem.task.ExternalSystemTaskManager;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.ComposerUtil;
import consulo.php.composer.setting.ComposerExecutionSettings;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the tasks the resolver offers: {@code install}, {@code update}, {@code dump-autoload} as Composer commands, any
 * other task is a script of {@code composer.json} run by {@code composer run-script}. Script parameters of a task run
 * configuration are appended to the command.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerTaskManager implements ExternalSystemTaskManager<ComposerExecutionSettings> {
    private final ComposerCommand myCommand = new ComposerCommand();

    @Override
    public void executeTasks(ExternalSystemTaskId id,
                             List<String> taskNames,
                             String projectPath,
                             @Nullable ComposerExecutionSettings settings,
                             List<String> vmOptions,
                             List<String> scriptParameters,
                             @Nullable String debuggerSetup,
                             ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {
        ComposerExecutionSettings executionSettings = settings != null ? settings : new ComposerExecutionSettings(ComposerConstants.DEFAULT_EXECUTABLE, null);
        Path packageDirectory = ComposerUtil.toPackageDirectory(Path.of(projectPath));
        for (String taskName : taskNames) {
            List<String> arguments = toArguments(taskName);
            arguments.addAll(scriptParameters);
            myCommand.run(id, executionSettings, packageDirectory, arguments, listener);
        }
    }

    @Override
    public boolean cancelTask(ExternalSystemTaskId id, ExternalSystemTaskNotificationListener listener) {
        return myCommand.cancel();
    }

    public static List<String> toArguments(String taskName) {
        List<String> arguments = new ArrayList<>();
        if (ComposerConstants.COMMAND_TASKS.contains(taskName)) {
            arguments.add(taskName);
        }
        else {
            arguments.add("run-script");
            arguments.add(taskName);
        }
        return arguments;
    }
}
