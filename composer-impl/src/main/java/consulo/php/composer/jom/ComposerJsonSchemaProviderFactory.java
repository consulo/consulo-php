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

package consulo.php.composer.jom;

import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider;
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory;
import com.jetbrains.jsonSchema.extension.SchemaType;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.php.composer.ComposerFileTypeFactory;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Validates and completes {@code composer.json} by the schema of Composer ({@code res/composer-schema.json} of
 * composer/composer, MIT license).
 *
 * @author VISTALL
 * @since 2026-10-31
 */
@ExtensionImpl
public class ComposerJsonSchemaProviderFactory implements JsonSchemaProviderFactory, DumbAware {
    @Override
    public List<JsonSchemaFileProvider> getProviders(Project project) {
        return List.of(new ComposerJsonSchemaFileProvider());
    }

    private static class ComposerJsonSchemaFileProvider implements JsonSchemaFileProvider {
        @Override
        public boolean isAvailable(VirtualFile file) {
            return ComposerFileTypeFactory.COMPOSER_JSON.equals(file.getName());
        }

        @Override
        public String getName() {
            return "composer.json";
        }

        @Override
        public @Nullable VirtualFile getSchemaFile() {
            return JsonSchemaProviderFactory.getResourceFile(ComposerJsonSchemaProviderFactory.class, "/json-schema/composer-schema.json");
        }

        @Override
        public SchemaType getSchemaType() {
            return SchemaType.embeddedSchema;
        }

        @Override
        public @Nullable String getRemoteSource() {
            return "https://getcomposer.org/schema.json";
        }
    }
}
