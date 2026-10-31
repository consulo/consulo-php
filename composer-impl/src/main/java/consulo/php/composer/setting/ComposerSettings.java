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

package consulo.php.composer.setting;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.component.persist.StoragePathMacros;
import consulo.externalSystem.setting.AbstractExternalSystemSettings;
import consulo.externalSystem.setting.DelegatingExternalSystemSettingsListener;
import consulo.externalSystem.setting.ExternalSystemSettingsListener;
import consulo.project.Project;
import consulo.util.xml.serializer.annotation.AbstractCollection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;

/**
 * The linked Composer packages of a project.
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
@State(name = "ComposerSettings", storages = @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/composer"))
public class ComposerSettings extends AbstractExternalSystemSettings<ComposerSettings, ComposerProjectSettings, ComposerSettingsListener>
    implements PersistentStateComponent<ComposerSettings.MyState> {

    @Inject
    public ComposerSettings(Project project) {
        super(ComposerSettingsListener.TOPIC, project);
    }

    public static ComposerSettings getInstance(Project project) {
        return project.getInstance(ComposerSettings.class);
    }

    @Override
    public void subscribe(ExternalSystemSettingsListener<ComposerProjectSettings> listener) {
        getProject().getMessageBus().connect(getProject()).subscribe(ComposerSettingsListener.TOPIC, new ListenerAdapter(listener));
    }

    @Override
    protected void copyExtraSettingsFrom(ComposerSettings settings) {
    }

    @Override
    protected void checkSettings(ComposerProjectSettings old, ComposerProjectSettings current) {
    }

    @Override
    public MyState getState() {
        MyState state = new MyState();
        fillState(state);
        return state;
    }

    @Override
    public void loadState(MyState state) {
        super.loadState(state);
    }

    private static class ListenerAdapter extends DelegatingExternalSystemSettingsListener<ComposerProjectSettings>
        implements ComposerSettingsListener {
        ListenerAdapter(ExternalSystemSettingsListener<ComposerProjectSettings> delegate) {
            super(delegate);
        }
    }

    public static class MyState implements State<ComposerProjectSettings> {
        private final Set<ComposerProjectSettings> myProjectSettings = new TreeSet<>();

        @Override
        @AbstractCollection(surroundWithTag = false, elementTypes = {ComposerProjectSettings.class})
        public Set<ComposerProjectSettings> getLinkedExternalProjectsSettings() {
            return myProjectSettings;
        }

        @Override
        public void setLinkedExternalProjectsSettings(@Nullable Set<ComposerProjectSettings> settings) {
            if (settings != null) {
                myProjectSettings.addAll(settings);
            }
        }
    }
}
