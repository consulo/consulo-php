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
import com.jetbrains.php.lang.documentation.PhpNamedElementDocSource;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.refactoring.PhpNameStyle;
import com.jetbrains.php.refactoring.PhpNamespaceBraceConverter;
import com.jetbrains.php.util.PhpContractUtil;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import com.jetbrains.php.lang.lexer.PhpLexer;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Small PHP utilities.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpUtilitiesTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    @Test
    public void testNameStyle() {
        List<String> parts = List.of("form", "Factory", "Interface");
        Assertions.assertEquals(List.of("formFactoryInterface", "factoryInterface", "interface"),
            PhpNameStyle.DECAPITALIZE.withStyle(PhpNameStyle.CAMEL_CASE).generateNames(parts));
        Assertions.assertEquals(List.of("form_factory_interface", "factory_interface", "interface"),
            PhpNameStyle.DECAPITALIZE.withStyle(PhpNameStyle.SNAKE_CASE).generateNames(parts));
    }

    @Test
    public void testContract() {
        PhpContractUtil.assertFqn("\\App\\Foo");
        Assertions.assertThrows(AssertionError.class, () -> PhpContractUtil.assertFqn("App\\Foo"));
    }

    @Test
    public void testNamespacesAndDeprecation(Context context) {
        PhpFile file = (PhpFile) context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            namespace App\\A {
                /** @deprecated since 2.0, use New */
                class Old {}
                #[\\Deprecated(message: 'gone')]
                class Attributed {}
                class Current {}
            }
            namespace App\\B {
            }
            """);
        Assertions.assertEquals(List.of("App\\A", "App\\B"),
            PhpNamespaceBraceConverter.getAllNamespaces(file).stream().map(PhpNamespace::getName).toList());

        List<PhpClass> classes = List.copyOf(PsiTreeUtil.findChildrenOfType(file, PhpClass.class));
        Assertions.assertEquals("since 2.0, use New", new PhpNamedElementDocSource(classes.get(0), false).getDeprecationInfo());
        Assertions.assertEquals("gone", new PhpNamedElementDocSource(classes.get(1), false).getDeprecationInfo());
        Assertions.assertNull(new PhpNamedElementDocSource(classes.get(2), false).getDeprecationInfo());
    }

    @Test
    public void testLexerSplitsStrings(Context context) {
        PhpLexer lexer = new PhpLexer(false);
        lexer.start("<?php new \\Foo('it\\'s', \"x\", '', b'y');");
        List<String> tokens = new ArrayList<>();
        while (lexer.getTokenType() != null) {
            if (lexer.getTokenType() == PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE || lexer.getTokenType() == PhpTokenTypes.STRING_LITERAL
                || lexer.getTokenType() == PhpTokenTypes.chSINGLE_QUOTE || lexer.getTokenType() == PhpTokenTypes.chDOUBLE_QUOTE) {
                tokens.add(lexer.getTokenType() + ":" + lexer.getTokenText());
            }
            lexer.advance();
        }
        Assertions.assertEquals(List.of(
            "single quote:'", "single quoted string:it\\'s", "single quote:'",
            "double quote:\"", "string:x", "double quote:\"",
            "single quote:'", "single quote:'",
            "single quote:b'", "single quoted string:y", "single quote:'"
        ), tokens);
    }
}
