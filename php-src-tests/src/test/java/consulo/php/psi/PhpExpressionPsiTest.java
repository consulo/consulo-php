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
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.ArrayIndex;
import com.jetbrains.php.lang.psi.elements.AssignmentExpression;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList;
import com.jetbrains.php.lang.psi.elements.PhpFieldType;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
import com.jetbrains.php.lang.psi.elements.PhpTypeDeclaration;
import com.jetbrains.php.lang.psi.elements.SelfAssignmentExpression;
import com.jetbrains.php.lang.psi.elements.TernaryExpression;
import com.jetbrains.php.lang.psi.elements.UnaryExpression;
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
 * PhpStorm-shaped expression and type declaration PSI.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpExpressionPsiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static PsiFile parse(Context context, String text) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
    }

    private static <T extends PsiElement> List<T> all(PsiFile file, Class<T> aClass) {
        return List.copyOf(PsiTreeUtil.findChildrenOfType(file, aClass));
    }

    @Test
    public void testAssignments(Context context) {
        PsiFile file = parse(context, "<?php\n$a = foo();\n$b .= 'x';\n$c['k'] = 1;\n");
        List<AssignmentExpression> assignments = all(file, AssignmentExpression.class);
        Assertions.assertEquals(3, assignments.size());

        Assertions.assertEquals("$a", assignments.get(0).getVariable().getText());
        Assertions.assertEquals("foo()", assignments.get(0).getValue().getText());

        SelfAssignmentExpression self = (SelfAssignmentExpression) assignments.get(1);
        Assertions.assertEquals(".=", self.getOperation().getText());
        Assertions.assertEquals("'x'", self.getValue().getText());

        Assertions.assertInstanceOf(ArrayAccessExpression.class, assignments.get(2).getVariable());
    }

    @Test
    public void testTernaryAndUnary(Context context) {
        PsiFile file = parse(context, "<?php\n$a = $c ? 'yes' : 'no';\n$b = $d ?: 'default';\n$e = !$f;\n");
        List<TernaryExpression> ternaries = all(file, TernaryExpression.class);
        Assertions.assertEquals(2, ternaries.size());

        TernaryExpression ternary = ternaries.get(0);
        Assertions.assertFalse(ternary.isShort());
        Assertions.assertEquals("$c", ternary.getCondition().getText());
        Assertions.assertEquals("'yes'", ternary.getTrueVariant().getText());
        Assertions.assertEquals("'no'", ternary.getFalseVariant().getText());

        TernaryExpression elvis = ternaries.get(1);
        Assertions.assertTrue(elvis.isShort());
        Assertions.assertEquals("$d", elvis.getTrueVariant().getText());
        Assertions.assertEquals("'default'", elvis.getFalseVariant().getText());

        UnaryExpression unary = all(file, UnaryExpression.class).get(0);
        Assertions.assertEquals("!", unary.getOperation().getText());
        Assertions.assertEquals("$f", unary.getValue().getText());
    }

    @Test
    public void testArrayIndex(Context context) {
        PsiFile file = parse(context, "<?php\n$options['label'];\n$list[] = 1;\n");
        List<ArrayAccessExpression> accesses = all(file, ArrayAccessExpression.class);

        ArrayAccessExpression access = accesses.get(0);
        Assertions.assertEquals("$options", access.getValue().getText());
        ArrayIndex index = access.getIndex();
        Assertions.assertNotNull(index);
        Assertions.assertEquals("'label'", index.getValue().getText());
        // the string is two levels below the index, as Symfony patterns expect
        Assertions.assertSame(index, index.getValue().getParent());

        Assertions.assertNull(accesses.get(1).getIndex() == null ? null : accesses.get(1).getIndex().getValue());
    }

    @Test
    public void testTypeDeclarations(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App;

            use Psr\\Container\\ContainerInterface;

            enum Suit: string { case A = 'a'; }

            class Foo {
                public ?ContainerInterface $container = null, $other;
                const int LIMIT = 10;
                private $untyped;

                public function __construct(private readonly Bar $bar, int|null $count, $plain) {}

                public function build(ContainerInterface $container): ?static {}
            }
            """);
        PhpClass foo = all(file, PhpClass.class).stream().filter(c -> "Foo".equals(c.getName())).findFirst().orElseThrow();
        Method construct = foo.findOwnMethodByName("__construct");
        Parameter[] parameters = construct.getParameters();

        PhpTypeDeclaration barType = parameters[0].getTypeDeclaration();
        Assertions.assertEquals("Bar", barType.getText());
        Assertions.assertEquals(List.of("\\App\\Bar"), List.copyOf(barType.getType().getTypes()));
        Assertions.assertFalse(barType.isNullable());

        PhpTypeDeclaration countType = parameters[1].getTypeDeclaration();
        Assertions.assertEquals(List.of("int", "null"), countType.getClassReferences().stream().map(ClassReference::getText).toList());
        Assertions.assertTrue(countType.isNullable());
        Assertions.assertNull(parameters[2].getTypeDeclaration());
        // the parameter type still resolves through the wrapper
        Assertions.assertTrue(parameters[1].getDeclaredType().getTypes().contains("\\int"));

        // properties share the type of their list
        Field container = foo.findOwnFieldByName("container", false);
        PhpTypeDeclaration fieldType = container.getTypeDeclaration();
        Assertions.assertInstanceOf(PhpFieldType.class, fieldType);
        Assertions.assertTrue(fieldType.isNullable());
        Assertions.assertSame(fieldType, foo.findOwnFieldByName("other", false).getTypeDeclaration());
        Assertions.assertEquals(List.of("\\Psr\\Container\\ContainerInterface", "\\null"),
            List.copyOf(container.getType().getTypes()).stream().sorted().toList());
        Assertions.assertNull(foo.findOwnFieldByName("untyped", false).getTypeDeclaration());
        Assertions.assertEquals("int", foo.findOwnFieldByName("LIMIT", true).getTypeDeclaration().getText());

        // a promoted parameter is a field with the parameter type
        Assertions.assertEquals("Bar", foo.findOwnFieldByName("bar", false).getTypeDeclaration().getText());

        PhpClassFieldsList list = PsiTreeUtil.getParentOfType(container, PhpClassFieldsList.class);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.getFields().size());
        Assertions.assertSame(fieldType, list.getFieldType());
        Assertions.assertEquals("public", list.getModifierList().getText());

        // the return type is a type declaration too
        Method build = foo.findOwnMethodByName("build");
        PhpTypeDeclaration returnType = PsiTreeUtil.getChildOfType(build, PhpTypeDeclaration.class);
        Assertions.assertInstanceOf(PhpReturnType.class, returnType);
        Assertions.assertTrue(returnType.isNullable());
        Assertions.assertEquals("ContainerInterface", build.getParameters()[0].getTypeDeclaration().getText());

        PhpClass suit = all(file, PhpClass.class).stream().filter(c -> "Suit".equals(c.getName())).findFirst().orElseThrow();
        Assertions.assertEquals("string", PsiTreeUtil.getChildOfType(suit, PhpTypeDeclaration.class).getText());
    }
}
