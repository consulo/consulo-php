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

import consulo.application.ReadAction;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkTable;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.application.WriteAction;
import consulo.application.dumb.IndexNotReadyException;
import consulo.it.HeadlessProjects;
import consulo.module.ModifiableModuleModel;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import org.jspecify.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A headless project with one module over a directory of PHP files.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
final class PhpProjects {
    private static final long TIMEOUT_SECONDS = 120;

    private PhpProjects() {
    }

    /**
     * A PHP SDK set up like from the SDK settings: its roots are the bundled runtime stubs
     */
    static Sdk createPhpSdk() throws Exception {
        Sdk sdk = SdkTable.getInstance().createSdk("PHP " + System.nanoTime(), PhpSdkType.getInstance());
        SdkModificator modificator = sdk.getSdkModificator();
        modificator.setHomePath("/usr/bin");
        modificator.commitChanges();
        PhpSdkType.getInstance().setupSdkPaths(sdk);
        WriteAction.run(() -> SdkTable.getInstance().addSdk(sdk));
        return sdk;
    }

    /**
     * @param files relative path to the file text
     */
    static Project open(HeadlessProjects projects, Map<String, String> files) throws Exception {
        return open(projects, files, null);
    }

    /**
     * @param phpSdk SDK of the PHP module extension, null for a module without the PHP extension
     */
    static Project open(HeadlessProjects projects, Map<String, String> files, @Nullable Sdk phpSdk) throws Exception {
        Path directory = projects.newDirectory();
        for (Map.Entry<String, String> entry : files.entrySet()) {
            Path file = directory.resolve(entry.getKey());
            Files.createDirectories(file.getParent());
            Files.writeString(file, entry.getValue());
        }

        Project project = projects.open(directory);
        VirtualFile directoryFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory);
        assertThat(directoryFile).isNotNull();

        ModuleManager moduleManager = ModuleManager.getInstance(project);
        WriteAction.run(() -> {
            ModifiableModuleModel moduleModel = moduleManager.getModifiableModel();
            Module module = moduleModel.newModule("main", directory.toString());
            moduleModel.commit();

            ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
            rootModel.addContentEntry(directoryFile);
            if (phpSdk != null) {
                PhpMutableModuleExtension<?> extension = rootModel.getExtensionWithoutCheck(PhpMutableModuleExtension.class);
                assertThat(extension).isNotNull();
                extension.setEnabled(true);
                extension.getInheritableSdk().set(null, phpSdk);
                // what the module settings do for an extension with an SDK: the SDK becomes a dependency
                rootModel.addModuleExtensionSdkEntry(extension);
            }
            rootModel.commit();
        });

        HeadlessProjects.awaitIdle(project);
        return project;
    }

    /**
     * Polls {@code query} in a read action until its answer satisfies {@code ready}, waiting out dumb mode
     */
    static <T> T waitForIndex(Supplier<T> query, Predicate<T> ready) throws Exception {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS);
        T answer = null;
        while (System.currentTimeMillis() < deadline) {
            answer = ReadAction.compute(() -> {
                try {
                    return query.get();
                }
                catch (IndexNotReadyException e) {
                    return null;
                }
            });
            if (answer != null && ready.test(answer)) {
                return answer;
            }
            Thread.sleep(50);
        }
        throw new AssertionError("timed out waiting for the index, last answer: " + answer);
    }
}
