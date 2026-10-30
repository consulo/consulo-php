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
import com.jetbrains.php.lang.psi.elements.ConcatenationExpression;
import com.jetbrains.php.lang.psi.elements.ForeachStatement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpCallableFunction;
import com.jetbrains.php.lang.psi.elements.PhpCallableMethod;
import com.jetbrains.php.lang.psi.elements.PhpCase;
import com.jetbrains.php.lang.psi.elements.PhpSwitch;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

/**
 * Statements, callables and arguments in the shape of PhpStorm's API.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpStatementApiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static PsiFile parse(Context context, String text) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
    }

    private static <T extends PsiElement> List<T> all(PsiFile file, Class<T> aClass) {
        return List.copyOf(PsiTreeUtil.findChildrenOfType(file, aClass));
    }

    @Test
    public void testSwitch(Context context) {
        PsiFile file = parse(context, "<?php\nswitch ($a) { case 'x': foo(); break; case B::C: default: bar(); }\n");
        PhpSwitch phpSwitch = all(file, PhpSwitch.class).get(0);
        PhpCase[] cases = phpSwitch.getAllCases();
        Assertions.assertEquals(3, cases.length);
        Assertions.assertEquals("'x'", cases[0].getCondition().getText());
        Assertions.assertEquals("B::C", cases[1].getCondition().getText());
        Assertions.assertTrue(cases[2].isDefault());
        Assertions.assertNull(cases[2].getCondition());
    }

    @Test
    public void testForeachAndConcatenation(Context context) {
        PsiFile file = parse(context, "<?php\nforeach ($list as $k => $v) { $s = 'a' . $v; }\n");
        ForeachStatement foreach = all(file, ForeachStatement.class).get(0);
        Assertions.assertEquals("$list", foreach.getArray().getText());
        Assertions.assertEquals("k", foreach.getKey().getName());
        Assertions.assertEquals("v", foreach.getValue().getName());
        Assertions.assertEquals("'a' . $v", all(file, ConcatenationExpression.class).get(0).getText());
    }

    @Test
    public void testCallables(Context context) {
        PsiFile file = parse(context, "<?php\n$f = $this->foo(...);\n$g = strlen(...);\n$h = $this->bar(1);\n");
        List<MethodReference> methods = all(file, MethodReference.class);
        Assertions.assertInstanceOf(PhpCallableMethod.class, methods.get(0));
        Assertions.assertEquals("foo", methods.get(0).getName());
        Assertions.assertFalse(methods.get(1) instanceof PhpCallableMethod);
        PhpCallableFunction function = (PhpCallableFunction) all(file, FunctionReference.class).stream()
            .filter(PhpCallableFunction.class::isInstance).findFirst().orElseThrow();
        Assertions.assertEquals("strlen", function.getName());
    }

    @Test
    public void testNamedArguments(Context context) {
        PsiFile file = parse(context, "<?php\ntrans('id', [], domain: 'messages');\ntrans('id', [], 'admin');\n");
        List<FunctionReference> calls = all(file, FunctionReference.class);
        Assertions.assertEquals("'messages'", calls.get(0).getParameterList().getParameter("domain", 2).getText());
        Assertions.assertEquals("'admin'", calls.get(1).getParameterList().getParameter("domain", 2).getText());
        Assertions.assertNull(calls.get(0).getParameterList().getParameter("locale", 3));
    }

    @Test
    public void testTypeHelpers(Context context) {
        Assertions.assertTrue(PhpType.from(PhpType.INT, PhpType.STRING).getTypes().containsAll(List.of("\\int", "\\string")));
        Assertions.assertTrue(PhpType.from("\\Foo[]").isArray());
        Assertions.assertFalse(PhpType.from("\\Foo").isArray());
        Assertions.assertTrue(PhpType.from("\\bool").isBoolean());
        Assertions.assertEquals(1, PhpType.from("\\Foo").types().count());
    }
}
