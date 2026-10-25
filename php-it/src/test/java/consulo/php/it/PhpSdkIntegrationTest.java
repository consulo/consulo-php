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

import consulo.application.WriteAction;
import consulo.application.content.impl.internal.bundle.SdkImpl;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.it.HeadlessApplicationExtension;
import consulo.php.sdk.PhpRuntimeStubs;
import consulo.php.sdk.PhpSdkType;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jdom.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runtime stubs as the roots of PHP SDKs, also after the plugin jar moved.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(HeadlessApplicationExtension.class)
public class PhpSdkIntegrationTest {
    private static final String OLD_PLUGIN_STUBS = "zip:///old/plugins/consulo.php/lib/consulo.php.jar!/phpstorm-stubs";

    @Test
    public void newSdkIsUpToDate() throws Exception {
        Sdk sdk = PhpProjects.createPhpSdk();

        assertThat(PhpSdkType.isRuntimeStubsOutdated(sdk)).isFalse();
    }

    /**
     * An SDK saved by an older plugin location: its stubs root is replaced, other roots stay
     */
    @Test
    public void stubsOfAnOldPluginLocationAreReplaced() throws Exception {
        Path home = Files.createTempDirectory("consulo-php-sdk");
        Path extensions = Files.createDirectories(home.resolve("extensions"));
        Sdk sdk = loadSdk(home, OLD_PLUGIN_STUBS, VirtualFileUtil.pathToUrl(extensions.toString()));

        assertThat(sdk.getRootProvider().getUrls(BinariesOrderRootType.ID)).contains(OLD_PLUGIN_STUBS);
        assertThat(PhpSdkType.isRuntimeStubsOutdated(sdk)).isTrue();

        WriteAction.run(() -> PhpSdkType.updateRuntimeStubs(sdk));

        VirtualFile stubs = PhpRuntimeStubs.getRoot();
        assertThat(stubs).isNotNull();
        assertThat(sdk.getRootProvider().getUrls(BinariesOrderRootType.ID))
            .containsExactlyInAnyOrder(VirtualFileUtil.pathToUrl(extensions.toString()), stubs.getUrl());
        assertThat(sdk.getRootProvider().getUrls(SourcesOrderRootType.ID)).containsExactly(stubs.getUrl());
        assertThat(PhpSdkType.isRuntimeStubsOutdated(sdk)).isFalse();
    }

    @Test
    public void oldStubsDirectoryIsRecognized() {
        assertThat(PhpRuntimeStubs.isRuntimeStubsUrl("file:///opt/consulo/plugins/consulo.php/php-runtime-stubs")).isTrue();
        assertThat(PhpRuntimeStubs.isRuntimeStubsUrl(OLD_PLUGIN_STUBS)).isTrue();
        assertThat(PhpRuntimeStubs.isRuntimeStubsUrl("file:///home/user/php/extensions")).isFalse();
    }

    private static Sdk loadSdk(Path home, String... binaryUrls) throws Exception {
        Element composite = new Element("root").setAttribute("type", "composite");
        for (String url : binaryUrls) {
            composite.addContent(new Element("root").setAttribute("type", "simple").setAttribute("url", url));
        }
        Element state = new Element("sdk");
        state.addContent(new Element("name").setAttribute("value", "PHP " + System.nanoTime()));
        state.addContent(new Element("type").setAttribute("value", PhpSdkType.getInstance().getId()));
        state.addContent(new Element("homePath").setAttribute("value", home.toString()));
        state.addContent(new Element("roots").addContent(new Element(BinariesOrderRootType.ID).addContent(composite)));

        SdkImpl sdk = (SdkImpl) SdkTable.getInstance().createSdk("PHP", PhpSdkType.getInstance());
        sdk.readExternal(state, SdkTable.getInstance());
        return sdk;
    }
}
