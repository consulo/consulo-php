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

import com.jetbrains.php.config.PhpLanguageFeature;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.refactoring.PhpNameUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

/**
 * {@link PhpLangUtil}, {@link PhpNameUtil}, {@link PhpPsiUtil} and language levels.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpApiUtilTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    @Test
    public void testLangUtil() {
        Assertions.assertTrue(PhpLangUtil.equalsClassNames("\\App\\Foo", "\\app\\FOO"));
        Assertions.assertFalse(PhpLangUtil.equalsClassNames("App\\Foo", "\\App\\Foo"));
        Assertions.assertTrue(PhpLangUtil.equalsClassNames(null, null));
        Assertions.assertFalse(PhpLangUtil.equalsClassNames(null, "Foo"));

        Assertions.assertEquals("\\App\\Foo", PhpLangUtil.toFQN("App\\Foo"));
        Assertions.assertEquals("\\App\\Foo", PhpLangUtil.toFQN("\\App\\Foo"));
        Assertions.assertEquals("App\\Foo", PhpLangUtil.toPresentableFQN("\\App\\Foo"));
        Assertions.assertEquals("Foo", PhpLangUtil.toName("\\App\\Foo"));
        Assertions.assertEquals("\\App\\", PhpLangUtil.getParentNamespaceFQN("\\App\\Foo"));
        Assertions.assertEquals("\\", PhpLangUtil.getParentNamespaceFQN("Foo"));
        Assertions.assertEquals("\\App\\Foo", PhpLangUtil.toFQN("\\App\\", "Foo"));
        Assertions.assertEquals("\\Foo", PhpLangUtil.toFQN("", "Foo"));
        Assertions.assertTrue(PhpLangUtil.isGlobalNamespaceFQN("\\"));
    }

    @Test
    public void testNameUtil() {
        Assertions.assertTrue(PhpNameUtil.isValidNamespaceFullName("App\\Controller\\FooController", PhpLanguageLevel.DEFAULT));
        Assertions.assertFalse(PhpNameUtil.isValidNamespaceFullName("\\App\\Foo", PhpLanguageLevel.DEFAULT));
        Assertions.assertTrue(PhpNameUtil.isValidNamespaceFullName("\\App\\Foo", true, PhpLanguageLevel.DEFAULT));
        Assertions.assertFalse(PhpNameUtil.isValidNamespaceFullName("app.mailer", true, PhpLanguageLevel.DEFAULT));
        Assertions.assertFalse(PhpNameUtil.isValidNamespaceFullName("App\\\\Foo", PhpLanguageLevel.DEFAULT));
        Assertions.assertFalse(PhpNameUtil.isValidNamespaceFullName("App\\", PhpLanguageLevel.DEFAULT));
        // namespaced names are single tokens since PHP 8.0, so reserved words are fine inside them
        Assertions.assertTrue(PhpNameUtil.isValidNamespaceFullName("My\\Sweet\\Class", PhpLanguageLevel.PHP_8_0));
        Assertions.assertFalse(PhpNameUtil.isValidNamespaceFullName("My\\Sweet\\Class", PhpLanguageLevel.PHP_7_4));

        Assertions.assertTrue(PhpNameUtil.isValidClassName("FooController"));
        Assertions.assertFalse(PhpNameUtil.isValidClassName("Class"));
        Assertions.assertFalse(PhpNameUtil.isValidClassName("string"));
        Assertions.assertFalse(PhpNameUtil.isValidClassName("1Foo"));

        Assertions.assertEquals(List.of("foo", "Bar", "Car"), PhpNameUtil.splitName("fooBarCar"));
        Assertions.assertEquals(List.of("my", "HTTP", "Client"), PhpNameUtil.splitName("myHTTPClient"));
        Assertions.assertEquals(List.of("foo", "bar"), PhpNameUtil.splitName("foo_bar"));
        Assertions.assertEquals(List.of("oauth", "2", "Client"), PhpNameUtil.splitName("oauth2Client"));
    }

    @Test
    public void testLanguageLevel() {
        Assertions.assertTrue(PhpLanguageLevel.PHP_8_0.hasFeature(PhpLanguageFeature.ATTRIBUTES));
        Assertions.assertFalse(PhpLanguageLevel.PHP_7_4.hasFeature(PhpLanguageFeature.PROPERTY_PROMOTION));
        Assertions.assertTrue(PhpLanguageLevel.PHP_7_4.compareTo(PhpLanguageLevel.PHP_8_0) < 0);
    }

    @Test
    public void testPsiUtil(Context context) {
        PhpFile file = (PhpFile) context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            namespace App;

            /**
             * @ORM\\Entity
             */
            class Foo {
                public function bar(int $a, /* comment */ string $b): string { return 'x'; }
            }
            if (true) { interface Baz {} }
            $anonymous = new class {};
            """);

        List<PhpClass> classes = PhpPsiUtil.findAllClasses(file);
        Assertions.assertEquals(List.of("Foo", "Baz"), classes.stream().map(PhpClass::getName).toList());
        Assertions.assertEquals(1, PhpPsiUtil.findClasses(file, PhpClass::isInterface).size());

        Method method = classes.get(0).findOwnMethodByName("bar");
        Assertions.assertNotNull(method);
        PsiElement parameterList = PhpPsiUtil.getChildOfType(method, PhpElementTypes.PARAMETER_LIST);
        Assertions.assertNotNull(parameterList);

        PsiElement first = method.getParameters()[0];
        PsiElement comma = PhpPsiUtil.getNextSiblingIgnoreWhitespace(first, true);
        Assertions.assertEquals(",", comma.getText());
        Assertions.assertEquals(method.getParameters()[1], PhpPsiUtil.getNextSiblingIgnoreWhitespace(comma, true));
        Assertions.assertTrue(PhpPsiUtil.getNextSiblingIgnoreWhitespace(comma, false).getText().startsWith("/*"));
        Assertions.assertEquals(first, PhpPsiUtil.getPrevSiblingIgnoreWhitespace(comma, true));

        StringLiteralExpression string = PsiTreeUtil.findChildOfType(method, StringLiteralExpression.class);
        Assertions.assertEquals(method, PhpPsiUtil.getParentByCondition(string, true, Method.INSTANCEOF, null));
        Assertions.assertEquals(classes.get(0), PhpPsiUtil.getParentByCondition(string, true, PhpClass.INSTANCEOF, null));
        Assertions.assertNull(PhpPsiUtil.getParentByCondition(string, true, PhpClass.INSTANCEOF, Method.INSTANCEOF));
        Assertions.assertNull(PhpPsiUtil.getParentByCondition(string, true, PhpDocTag.INSTANCEOF, null));
        Assertions.assertTrue(PhpPsiUtil.isOfType(parameterList, PhpElementTypes.PARAMETER_LIST));
    }
}
