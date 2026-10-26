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

import com.jetbrains.php.lang.psi.elements.impl.StringLiteralExpressionImpl;
import com.jetbrains.php.lang.psi.elements.impl.PhpDefineImpl;
import com.jetbrains.php.lang.psi.elements.impl.ConstantImpl;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionScalarArgument;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Set;

/**
 * References and member access in the shape of PhpStorm's API.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpReferenceApiTest {
    public record Context(PsiFileFactory psiFileFactory, Project project) implements InjectingRecord {
    }

    private static final String CODE = """
        <?php
        namespace App\\Controller;

        use App\\Service\\Mailer as AppMailer;

        class Child extends Base implements \\Countable {
            /**
             * @param AppMailer $mailer the mailer
             */
            public function run(AppMailer $mailer, $count) {
                $mailer->send($count, 'x');
                parent::run($mailer);
                Child::create();
                $this->field;
                $value = Child::VERSION;
                $sum = ($count + 1);
                $closure = function () {};
                /** @var \\DateTime $date */
                $date = null;
            }
        }
        """;

    private static PsiFile parse(Context context) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, CODE);
    }

    private static <T extends PsiElement> List<T> all(PsiFile file, Class<T> aClass) {
        return List.copyOf(PsiTreeUtil.findChildrenOfType(file, aClass));
    }

    private static MethodReference call(PsiFile file, String name) {
        return all(file, MethodReference.class).stream().filter(reference -> name.equals(reference.getName())).findFirst().orElseThrow();
    }

    @Test
    public void testClassReference(Context context) {
        PsiFile file = parse(context);
        PhpClass child = all(file, PhpClass.class).get(0);

        List<ClassReference> extendsList = child.getExtendsList().getReferenceElements();
        Assertions.assertEquals(1, extendsList.size());
        ClassReference base = extendsList.get(0);
        Assertions.assertEquals("Base", base.getName());
        Assertions.assertEquals("\\App\\Controller\\Base", base.getFQN());
        Assertions.assertEquals("#C\\App\\Controller\\Base", base.getSignature());
        Assertions.assertInstanceOf(PhpExpression.class, base);

        Assertions.assertEquals("\\Countable", child.getImplementsList().getReferenceElements().get(0).getFQN());
    }

    @Test
    public void testMemberReferences(Context context) {
        PsiFile file = parse(context);

        MethodReference send = call(file, "send");
        Assertions.assertInstanceOf(Variable.class, send.getClassReference());
        Assertions.assertEquals(PhpModifier.State.DYNAMIC, send.getReferenceType());
        Assertions.assertEquals("$count", send.getParameter(0).getText());
        Assertions.assertEquals("'x'", send.getParameter(1).getText());
        Assertions.assertNull(send.getParameter(2));

        Assertions.assertEquals(PhpModifier.State.PARENT, call(file, "run").getReferenceType());
        MethodReference create = call(file, "create");
        Assertions.assertEquals(PhpModifier.State.STATIC, create.getReferenceType());
        Assertions.assertEquals("\\App\\Controller\\Child", ((ClassReference) create.getClassReference()).getFQN());

        FieldReference field = all(file, FieldReference.class).get(0);
        Assertions.assertEquals("field", field.getName());
        Assertions.assertEquals("$this", field.getClassReference().getText());

        ClassConstantReference version = all(file, ClassConstantReference.class).get(0);
        Assertions.assertInstanceOf(ClassReference.class, version.getClassReference());
        Assertions.assertEquals(PhpModifier.State.STATIC, version.getReferenceType());
    }

    @Test
    public void testFunctions(Context context) {
        PsiFile file = parse(context);
        Function run = all(file, Function.class).stream().filter(function -> "run".equals(function.getName())).findFirst().orElseThrow();
        Assertions.assertFalse(run.isClosure());
        Assertions.assertEquals("mailer", run.getParameter(0).getName());
        Assertions.assertNull(run.getParameter(5));

        Function closure = all(file, Function.class).stream().filter(function -> function != run).findFirst().orElseThrow();
        Assertions.assertTrue(closure.isClosure());

        PhpDocParamTag docTag = run.getParameter(0).getDocTag();
        Assertions.assertNotNull(docTag);
        Assertions.assertEquals(Set.of("\\App\\Service\\Mailer"), docTag.getType().getTypes());
        Assertions.assertNull(run.getParameter(1).getDocTag());

        PhpDocComment varComment = all(file, PhpDocComment.class).stream().filter(comment -> comment.getText().contains("@var")).findFirst().orElseThrow();
        PhpDocParamTag varTag = varComment.getVarTag();
        Assertions.assertEquals(Set.of("\\DateTime"), varTag.getType().getTypes());
    }

    @Test
    public void testExpressions(Context context) {
        PsiFile file = parse(context);
        ParenthesizedExpression parenthesized = all(file, ParenthesizedExpression.class).get(0);
        Assertions.assertEquals("$count + 1", parenthesized.getArgument().getText());

        Assertions.assertEquals(Set.of("\\int", "\\string"), PhpType.from("\\int", "\\string").getTypes());

        PsiElement reference = new PhpExpectedFunctionScalarArgument(0, "'label'").createReference(context.project());
        Assertions.assertInstanceOf(StringLiteralExpression.class, reference);
    }

    @Test
    public void testImplementationClasses(Context context) {
        PsiFile file = context.psiFileFactory().createFileFromText("b.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            namespace App;

            const LIMIT = 10, NAME = 'app';
            define('LEGACY', 5);

            class Config { const VERSION = '1.0'; public $field = 1; }
            """);

        Assertions.assertInstanceOf(StringLiteralExpressionImpl.class, all(file, StringLiteralExpression.class).get(0));

        List<ConstantImpl> constants = all(file, ConstantImpl.class);
        Assertions.assertEquals(List.of("LIMIT", "NAME"), constants.stream().map(ConstantImpl::getName).toList());
        Assertions.assertEquals("10", constants.get(0).getValue().getText());
        Assertions.assertEquals("\\App\\LIMIT", constants.get(0).getFQN());
        Assertions.assertTrue(constants.get(0).isCaseSensitive());

        PhpDefineImpl define = all(file, PhpDefineImpl.class).get(0);
        Assertions.assertEquals("LEGACY", define.getName());
        Assertions.assertEquals("5", define.getValue().getText());

        PhpClass config = all(file, PhpClass.class).get(0);
        Assertions.assertInstanceOf(ClassConstImpl.class, config.findOwnFieldByName("VERSION", true));
        Assertions.assertEquals("'1.0'", ((ClassConstImpl) config.findOwnFieldByName("VERSION", true)).getDefaultValue().getText());
        Assertions.assertFalse(config.findOwnFieldByName("field", false) instanceof ClassConstImpl);
    }
}
