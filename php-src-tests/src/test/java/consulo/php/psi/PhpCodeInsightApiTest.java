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

import com.jetbrains.php.PhpPresentationUtil;
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil;
import com.jetbrains.php.completion.PhpClassLookupElement;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpGroupUseElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.PhpUseList;
import com.jetbrains.php.lang.psi.elements.impl.PhpPromotedFieldParameterImpl;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

/**
 * Imports, lookup elements and promoted parameters.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpCodeInsightApiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static final String CODE = """
        <?php
        namespace App\\Controller;

        use Symfony\\Component\\Routing\\Attribute\\Route;
        use Doctrine\\ORM\\Mapping as ORM;
        use Foo\\{Bar, Baz as Qux};
        use function strlen;
        use const PHP_EOL;

        class HomeController {
            public function __construct(private readonly Mailer $mailer, string $plain, int ...$ids) {}
        }
        """;

    private static PsiFile parse(Context context, String text) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
    }

    private static PhpClass homeController(PsiFile file) {
        return PsiTreeUtil.findChildOfType(file, PhpClass.class);
    }

    @Test
    public void testImports(Context context) {
        PhpPsiElement scope = PhpCodeInsightUtil.findScopeForUseOperator(homeController(parse(context, CODE)));
        Assertions.assertNotNull(scope);

        List<PhpUseList> imports = PhpCodeInsightUtil.collectImports(scope);
        Assertions.assertEquals(5, imports.size());

        Map<String, String> aliases = PhpCodeInsightUtil.getAliasesInScope(scope);
        Assertions.assertEquals(Map.of(
            "Route", "\\Symfony\\Component\\Routing\\Attribute\\Route",
            "ORM", "\\Doctrine\\ORM\\Mapping",
            "Bar", "\\Foo\\Bar",
            "Qux", "\\Foo\\Baz"
        ), aliases);

        Assertions.assertEquals("ORM", PhpCodeInsightUtil.findImportedName(scope, "Doctrine\\ORM\\Mapping", PhpGroupUseElement.PhpUseKeyword.CLASS));
        Assertions.assertEquals("strlen", PhpCodeInsightUtil.findImportedName(scope, "\\strlen", PhpGroupUseElement.PhpUseKeyword.FUNCTION));
        Assertions.assertNull(PhpCodeInsightUtil.findImportedName(scope, "\\strlen", PhpGroupUseElement.PhpUseKeyword.CLASS));
        Assertions.assertEquals("PHP_EOL", PhpCodeInsightUtil.findImportedName(scope, "\\PHP_EOL", PhpGroupUseElement.PhpUseKeyword.CONST));

        PhpUse qux = imports.get(2).getDeclarations()[1];
        Assertions.assertEquals("Baz", qux.getName());
        Assertions.assertEquals("Qux", qux.getAliasName());
        Assertions.assertEquals(PhpGroupUseElement.PhpUseKeyword.FUNCTION, imports.get(3).getDeclarations()[0].getUseKeyword());

        Assertions.assertFalse(PhpCodeInsightUtil.canImport(scope, "\\Other\\Route", PhpGroupUseElement.PhpUseKeyword.CLASS));
        Assertions.assertTrue(PhpCodeInsightUtil.canImport(scope, "\\Symfony\\Component\\Routing\\Attribute\\Route", PhpGroupUseElement.PhpUseKeyword.CLASS));
        Assertions.assertTrue(PhpCodeInsightUtil.canImport(scope, "\\Other\\Mailer", PhpGroupUseElement.PhpUseKeyword.CLASS));
    }

    @Test
    public void testQualifiedNames(Context context) {
        PhpPsiElement scope = PhpCodeInsightUtil.findScopeForUseOperator(homeController(parse(context, CODE)));

        Assertions.assertEquals("Route", PhpCodeInsightUtil.createQualifiedName(scope, "\\Symfony\\Component\\Routing\\Attribute\\Route", null, false));
        Assertions.assertEquals("ORM\\Column", PhpCodeInsightUtil.createQualifiedName(scope, "\\Doctrine\\ORM\\Mapping\\Column", null, false));
        Assertions.assertEquals("Admin\\UserController", PhpCodeInsightUtil.createQualifiedName(scope, "\\App\\Controller\\Admin\\UserController", null, false));
        Assertions.assertEquals("\\App\\Entity\\User", PhpCodeInsightUtil.createQualifiedName(scope, "App\\Entity\\User", null, false));
        Assertions.assertEquals("array_map", PhpCodeInsightUtil.createQualifiedName(scope, "\\array_map", null, true));
        Assertions.assertEquals("\\App\\Controller", PhpCodeInsightUtil.getNamespaceFQN(scope));

        PhpPsiElement global = PhpCodeInsightUtil.findScopeForUseOperator(homeController(parse(context, "<?php\nuse Foo\\Bar;\nclass A {}")));
        Assertions.assertNotNull(global);
        Assertions.assertEquals("\\", PhpCodeInsightUtil.getNamespaceFQN(global));
        Assertions.assertEquals("App\\Foo", PhpCodeInsightUtil.createQualifiedName(global, "\\App\\Foo", null, false));
        Assertions.assertEquals("Bar", PhpCodeInsightUtil.createQualifiedName(global, "\\Foo\\Bar", null, false));
    }

    @Test
    public void testPromotedParameters(Context context) {
        Method constructor = homeController(parse(context, CODE)).findOwnMethodByName("__construct");
        Parameter[] parameters = constructor.getParameters();

        Assertions.assertInstanceOf(PhpPromotedFieldParameterImpl.class, parameters[0]);
        Assertions.assertFalse(parameters[1] instanceof PhpPromotedFieldParameterImpl);
        Assertions.assertEquals("(\\App\\Controller\\Mailer $mailer, string $plain, int ...$ids)",
            PhpPresentationUtil.formatParameters(null, parameters).toString());
    }

    @Test
    public void testLookupElements(Context context) {
        PhpClass phpClass = homeController(parse(context, CODE));

        // icons can not be created in the headless test environment: check what is rendered, not the presentation
        PhpClassLookupElement classElement = new PhpClassLookupElement(phpClass, true, null);
        Assertions.assertEquals("HomeController", classElement.getLookupString());
        Assertions.assertEquals(" (App\\Controller)", classElement.tailText);
        Assertions.assertEquals(phpClass, classElement.getObject());

        PhpLookupElement methodElement = new PhpLookupElement(phpClass.findOwnMethodByName("__construct"));
        Assertions.assertEquals("__construct", methodElement.getLookupString());
        Assertions.assertTrue(methodElement.tailText.startsWith("(\\App\\Controller\\Mailer $mailer"));

        PhpLookupElement namespaceElement = new PhpLookupElement("App\\Controller\\", null, null, null, null, null);
        Assertions.assertEquals("App\\Controller\\", namespaceElement.getLookupString());
        Assertions.assertEquals("App\\Controller\\", namespaceElement.getObject());

        Assertions.assertEquals(1, PhpPsiUtil.findAllClasses((com.jetbrains.php.lang.psi.PhpFile) phpClass.getContainingFile()).size());
    }
}
