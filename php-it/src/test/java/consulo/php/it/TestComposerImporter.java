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

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.module.Module;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.php.composer.importer.ComposerImporter;
import consulo.php.composer.importer.ComposerPackage;
import consulo.virtualFileSystem.VirtualFile;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A framework importer in the style of the Symfony plugin: records the packages requiring {@code acme/framework}.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class TestComposerImporter implements ComposerImporter {
    public static final String FRAMEWORK = "acme/framework";

    /**
     * {@code <module>: <package name> @ <package root>}
     */
    public static final List<String> PROCESSED = new CopyOnWriteArrayList<>();

    @Override
    public boolean isApplicable(ComposerPackage composerPackage) {
        return composerPackage.requires(FRAMEWORK);
    }

    @RequiredReadAction
    @Override
    public void process(Module module, ModifiableRootModel model, ComposerPackage composerPackage, VirtualFile packageRoot) {
        PROCESSED.add(module.getName() + ": " + composerPackage.name() + " @ " + packageRoot.getName());
    }
}
