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

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpAttribute;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpEnumCase;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionClassConstantArgument;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionScalarArgument;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.config.PhpLanguageLevel;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

/**
 * PHP 8 attribute and enum PSI.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpAttributePsiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static PsiFile parse(Context context, String text) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
    }

    private static PhpClass findClass(PsiFile file, String name) {
        for (PhpClass phpClass : PsiTreeUtil.findChildrenOfType(file, PhpClass.class)) {
            if (name.equals(phpClass.getName())) {
                return phpClass;
            }
        }
        throw new AssertionError("class " + name + " not found");
    }

    private static List<String> fqns(PhpClass phpClass) {
        return phpClass.getAttributes().stream().map(PhpAttribute::getFQN).toList();
    }

    @Test
    public void testAttributeFqnResolution(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App\\Controller;

            use Symfony\\Component\\Routing\\Attribute\\Route;
            use Doctrine\\ORM\\Mapping as ORM;
            use Foo\\{Bar, Baz as Qux};
            use function strlen;

            #[Route('/x', name: 'x')]
            #[ORM\\Entity, \\Fully\\Qualified]
            #[Local, namespace\\Relative, Qux, strlen]
            class A {}
            """);

        Assertions.assertEquals(List.of(
            "\\Symfony\\Component\\Routing\\Attribute\\Route",
            "\\Doctrine\\ORM\\Mapping\\Entity",
            "\\Fully\\Qualified",
            "\\App\\Controller\\Local",
            "\\App\\Controller\\Relative",
            "\\Foo\\Baz",
            // 'use function' does not import classes
            "\\App\\Controller\\strlen"
        ), fqns(findClass(file, "A")));
    }

    @Test
    public void testAttributesByFqn(Context context) {
        PsiFile file = parse(context, """
            <?php
            use Doctrine\\ORM\\Mapping as ORM;

            #[ORM\\Entity]
            class A {
                #[ORM\\Column(type: 'string', length: 10)]
                public string $field;

                #[ORM\\PrePersist]
                public function method(#[\\SensitiveParameter] string $secret) {}
            }
            """);
        PhpClass phpClass = findClass(file, "A");

        Assertions.assertEquals(1, phpClass.getAttributes("\\Doctrine\\ORM\\Mapping\\Entity").size());
        Assertions.assertEquals(1, phpClass.getAttributes("Doctrine\\ORM\\Mapping\\Entity").size());
        Assertions.assertTrue(phpClass.getAttributes("\\Doctrine\\ORM\\Mapping\\Table").isEmpty());

        Field field = PsiTreeUtil.findChildOfType(phpClass, Field.class);
        Assertions.assertNotNull(field);
        Assertions.assertEquals(1, field.getAttributes("\\Doctrine\\ORM\\Mapping\\Column").size());

        Method method = PsiTreeUtil.findChildOfType(phpClass, Method.class);
        Assertions.assertNotNull(method);
        Assertions.assertEquals(1, method.getAttributes("\\Doctrine\\ORM\\Mapping\\PrePersist").size());

        Parameter parameter = method.getParameters()[0];
        Assertions.assertEquals(1, parameter.getAttributes("\\SensitiveParameter").size());
    }

    @Test
    public void testNamedArguments(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App;

            #[Column(type: 'string', length: 10, enumType: Status::class)]
            class A {}
            """);
        PhpAttribute attribute = findClass(file, "A").getAttributes().iterator().next();
        List<PhpAttribute.PhpAttributeArgument> arguments = attribute.getArguments();

        Assertions.assertEquals(List.of("type", "length", "enumType"), arguments.stream().map(PhpAttribute.PhpAttributeArgument::getName).toList());
        Assertions.assertTrue(arguments.stream().allMatch(PhpAttribute.PhpAttributeArgument::isNamedArgument));

        PhpExpectedFunctionScalarArgument type = (PhpExpectedFunctionScalarArgument) arguments.get(0).getArgument();
        Assertions.assertEquals("string", type.getNormalizedValue());
        PhpExpectedFunctionScalarArgument length = (PhpExpectedFunctionScalarArgument) arguments.get(1).getArgument();
        Assertions.assertEquals("10", length.getNormalizedValue());

        PhpExpectedFunctionClassConstantArgument enumType = (PhpExpectedFunctionClassConstantArgument) arguments.get(2).getArgument();
        Assertions.assertEquals("\\App\\Status", enumType.getClassFqn());
        Assertions.assertEquals("class", enumType.getConstantName());

        Assertions.assertEquals(3, attribute.getParameters().length);
        Assertions.assertEquals("'string'", attribute.getParameters()[0].getText());
    }

    @Test
    public void testPositionalArguments(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App;

            #[\\Attribute]
            class Route {
                public function __construct(public string $path = '', public ?string $name = null) {}
            }

            #[Route('/x', name: 'x')]
            class A {}
            """);
        List<PhpAttribute.PhpAttributeArgument> arguments = findClass(file, "A").getAttributes().iterator().next().getArguments();

        // positional arguments are named after the attribute constructor parameters
        Assertions.assertEquals(List.of("path", "name"), arguments.stream().map(PhpAttribute.PhpAttributeArgument::getName).toList());
        Assertions.assertFalse(arguments.get(0).isNamedArgument());
        Assertions.assertEquals("/x", ((PhpExpectedFunctionScalarArgument) arguments.get(0).getArgument()).getNormalizedValue());
    }

    @Test
    public void testNamedArgumentIsFlat(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App;

            #[Route('/x', name: 'x')]
            class A {}
            """);
        PhpAttribute attribute = findClass(file, "A").getAttributes().iterator().next();
        ParameterList parameterList = PsiTreeUtil.findChildOfType(attribute, ParameterList.class);
        Assertions.assertNotNull(parameterList);

        // the name, the colon and the value are children of the parameter list
        PsiElement value = parameterList.getParameters()[1];
        Assertions.assertEquals("'x'", value.getText());
        Assertions.assertSame(parameterList, value.getParent());
        PsiElement colon = PhpPsiUtil.getPrevSiblingIgnoreWhitespace(value, true);
        Assertions.assertEquals(PhpTokenTypes.opCOLON, colon.getNode().getElementType());
        Assertions.assertEquals("name", PhpPsiUtil.getPrevSiblingIgnoreWhitespace(colon, true).getText());

        Assertions.assertEquals("name", PhpPsiUtil.getArgumentName(value));
        Assertions.assertNull(PhpPsiUtil.getArgumentName(parameterList.getParameters()[0]));
        Assertions.assertSame(value, parameterList.getParameter("name", 1));
        Assertions.assertEquals("'/x'", parameterList.getParameter("path", 0).getText());
    }

    @Test
    public void testEnum(Context context) {
        PsiFile file = parse(context, """
            <?php
            namespace App;

            enum Suit: string implements HasLabel {
                case Hearts = 'H';
                #[Deprecated]
                case Spades = 'S';

                const Wild = self::Spades;

                public function label(): string { return ucfirst($this->name); }
            }

            final readonly class Point {}
            """);
        PhpClass suit = findClass(file, "Suit");
        Assertions.assertTrue(suit.isEnum());
        Assertions.assertFalse(suit.isReadonly());

        List<PhpEnumCase> cases = List.copyOf(suit.getEnumCases());
        Assertions.assertEquals(List.of("Hearts", "Spades"), cases.stream().map(PhpEnumCase::getName).toList());
        Assertions.assertEquals("'H'", cases.get(0).getValue().getText());
        Assertions.assertEquals(suit, cases.get(0).getContainingClass());
        Assertions.assertEquals(1, cases.get(1).getAttributes("\\App\\Deprecated").size());

        PhpClass point = findClass(file, "Point");
        Assertions.assertFalse(point.isEnum());
        Assertions.assertTrue(point.isReadonly());
        Assertions.assertTrue(point.isFinal());
    }
}
