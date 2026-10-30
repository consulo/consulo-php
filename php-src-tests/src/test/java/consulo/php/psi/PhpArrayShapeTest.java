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

package consulo.php.psi;

import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpDocTagAliasTypeProvider;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpAnyExpressionArgument;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpArrayShapeRegistry;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpShapeEntriesIndex.PhpShapeEntry;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;

/**
 * PHPDoc array shapes and phpstan/psalm type aliases.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpArrayShapeTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static final String CODE = """
        <?php
        namespace App;

        /**
         * @psalm-type ButtonOptions = array{label: string, size?: 'sm'|'md'}
         * @phpstan-import-type Theme from ThemeProvider as Colors
         */
        class Button {
            /**
             * @param array{
             *     label: string,
             *     size?: 'sm'|'md',
             *     icon: array{name: string, position?: string},
             *     Foo::BAR: int
             * } $options
             * @param ButtonOptions $aliased
             */
            public function render(array $options, array $aliased, $plain) {}
        }
        """;

    private static PsiFile parse(Context context) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, CODE);
    }

    private static List<String> keys(List<PhpShapeEntry> entries) {
        return entries.stream().map(entry -> entry.getKey().getValue() + (entry.isOptional() ? "?" : "")).toList();
    }

    @Test
    public void testParameterShape(Context context) {
        PsiFile file = parse(context);
        Function render = PsiTreeUtil.findChildrenOfType(file, Function.class).iterator().next();

        List<PhpShapeEntry> entries = PhpArrayShapeRegistry.INSTANCE.getParameterValues(render, 0);
        // one entry per union member of a value
        Assertions.assertEquals(List.of("'label'", "'size'?", "'size'?", "'icon'", "Foo::BAR"), keys(entries));
        Assertions.assertEquals("'md'", entries.get(2).getValueType());
        Assertions.assertInstanceOf(PhpAnyExpressionArgument.class, entries.get(4).getKey());
        Assertions.assertEquals(List.of("'name'", "'position'?"), keys(entries.get(3).getChildren()));

        Assertions.assertEquals(List.of(), PhpArrayShapeRegistry.INSTANCE.getParameterValues(render, 2));
        Assertions.assertEquals(List.of("0", "1"), keys(PhpArrayShapeRegistry.parseShape("list{int, string}")));
    }

    @Test
    public void testTypeAliases(Context context) {
        PsiFile file = parse(context);
        PhpClass button = PsiTreeUtil.findChildOfType(file, PhpClass.class);
        PhpDocComment classDoc = button.getDocComment();

        Collection<PhpDocTag> declared = PhpDocTagAliasTypeProvider.resolveLocalTypeFromTypeAlias("ButtonOptions", classDoc);
        Assertions.assertEquals(1, declared.size());
        PhpDocTag declaration = declared.iterator().next();
        PhpDocType aliasedType = PsiTreeUtil.getChildOfType(declaration, PhpDocType.class);
        Assertions.assertNotNull(aliasedType);
        Assertions.assertEquals(2, PhpArrayShapeRegistry.parseShape(aliasedType.getText()).size() - 1);

        // the import is found under its alias, its type is the source class
        PhpDocTag imported = PhpDocTagAliasTypeProvider.resolveLocalTypeFromTypeAlias("Colors", classDoc).iterator().next();
        Assertions.assertTrue(imported.getName().endsWith(PhpDocTagAliasTypeProvider.IMPORT_TYPE_TAG_POSTFIX));
        Assertions.assertEquals("ThemeProvider", PsiTreeUtil.getChildOfType(imported, PhpDocType.class).getText());

        // a parameter type naming the alias resolves to its declaration
        PhpDocType parameterType = PsiTreeUtil.findChildrenOfType(file, PhpDocType.class).stream()
            .filter(type -> type.getText().equals("ButtonOptions")).findFirst().orElseThrow();
        Assertions.assertEquals(List.of(declaration), List.copyOf(PhpDocTagAliasTypeProvider.resolveTypeAliases(parameterType)));
    }
}
