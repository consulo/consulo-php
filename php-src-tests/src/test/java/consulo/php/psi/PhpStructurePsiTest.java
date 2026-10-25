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
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.config.PhpLanguageLevel;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

/**
 * PhpStorm tree shapes that plugins match on.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpStructurePsiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    @Test
    public void testFieldAndConstantLists(Context context) {
        PsiFile file = context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            class Foo {
                /** shared doc */
                #[Column]
                protected static ?int $a = 1, $b;
                public const int X = 1, Y = 2;
                private $c;
            }
            $foo->bar(1, name: 2);
            """);
        PhpClass foo = PsiTreeUtil.findChildOfType(file, PhpClass.class);
        Field[] fields = foo.getOwnFields();
        Assertions.assertEquals(List.of("$a", "$b", "X", "Y", "$c"), Arrays.stream(fields).map(Field::getName).toList());

        Field a = fields[0];
        Field b = fields[1];
        Assertions.assertEquals(PhpElementTypes.CLASS_FIELDS, a.getParent().getNode().getElementType());
        Assertions.assertSame(a.getParent(), b.getParent());
        Assertions.assertTrue(b.isStatic());
        Assertions.assertFalse(b.isConstant());
        Assertions.assertEquals(1, b.getAttributes().size());
        Assertions.assertNotNull(b.getDocComment());
        Assertions.assertEquals("1", a.getDefaultValue().getText());
        Assertions.assertTrue(a.getType().getTypes().contains("\\int"));

        Field x = fields[2];
        Assertions.assertEquals(PhpElementTypes.CLASS_CONSTANTS, x.getParent().getNode().getElementType());
        Assertions.assertTrue(x.isConstant());
        Assertions.assertTrue(fields[3].getType().getTypes().contains("\\int"));

        Assertions.assertFalse(fields[4].isStatic());
        Assertions.assertNull(fields[4].getDocComment());

        MethodReference bar = PsiTreeUtil.findChildOfType(file, MethodReference.class);
        Assertions.assertEquals(PhpElementTypes.PARAMETER_LIST, bar.getParameterList().getNode().getElementType());
        Assertions.assertEquals(List.of("1", "2"), Arrays.stream(bar.getParameters()).map(p -> p.getText()).toList());
    }
}
