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

import consulo.externalSystem.model.Key;
import consulo.externalSystem.model.ProjectKeys;
import consulo.externalSystem.service.project.AbstractExternalEntityData;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.importer.ComposerPackage;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code composer.json} of an imported module - a child of its {@link ProjectKeys#MODULE} node. Holds plain
 * serializable copies of the {@link ComposerPackage}, the data nodes are serialized.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
public class ComposerPackageData extends AbstractExternalEntityData {
    private static final long serialVersionUID = 1L;

    /**
     * Processed after the module and its content roots exist.
     */
    public static final Key<ComposerPackageData> KEY = Key.create(ComposerPackageData.class, ProjectKeys.CONTENT_ROOT.getProcessingWeight() + 1);

    private final @Nullable String myName;
    private final Map<String, String> myRequire;
    private final Map<String, String> myRequireDev;
    private final Map<String, List<String>> myPsr4Autoload;
    private final Map<String, List<String>> myPsr4AutoloadDev;
    private final String myPackageDirectory;

    public ComposerPackageData(ComposerPackage composerPackage, String packageDirectory) {
        super(ComposerConstants.SYSTEM_ID);
        myName = composerPackage.name();
        myRequire = new LinkedHashMap<>(composerPackage.require());
        myRequireDev = new LinkedHashMap<>(composerPackage.requireDev());
        myPsr4Autoload = copy(composerPackage.psr4Autoload());
        myPsr4AutoloadDev = copy(composerPackage.psr4AutoloadDev());
        myPackageDirectory = packageDirectory;
    }

    public ComposerPackage getPackage() {
        return new ComposerPackage(myName, myRequire, myRequireDev, myPsr4Autoload, myPsr4AutoloadDev);
    }

    /**
     * @return the directory of the {@code composer.json}
     */
    public String getPackageDirectory() {
        return myPackageDirectory;
    }

    private static Map<String, List<String>> copy(Map<String, List<String>> map) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return result;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof ComposerPackageData that) || !super.equals(o)) {
            return false;
        }
        return Objects.equals(myName, that.myName)
            && myRequire.equals(that.myRequire)
            && myRequireDev.equals(that.myRequireDev)
            && myPsr4Autoload.equals(that.myPsr4Autoload)
            && myPsr4AutoloadDev.equals(that.myPsr4AutoloadDev)
            && myPackageDirectory.equals(that.myPackageDirectory);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(myName, myRequire, myRequireDev, myPsr4Autoload, myPsr4AutoloadDev, myPackageDirectory);
    }
}
