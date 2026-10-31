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
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.config.PhpLanguageLevel;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.psi.impl.PhpIndexImpl;
import consulo.project.Project;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Local types with signatures, completed through the index - here an index over one file.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpTypePsiTest {
    public record Context(PsiFileFactory psiFileFactory, Project project) implements InjectingRecord {
    }

    /**
     * The light test application has no stub index: classes and functions come from the test file
     */
    private static class FileIndex extends PhpIndexImpl {
        private final PsiFile myFile;

        FileIndex(Project project, PsiFile file) {
            super(project);
            myFile = file;
        }

        @Override
        public Collection<PhpClass> getAnyByFQN(String fqn) {
            List<PhpClass> result = new ArrayList<>();
            for (PhpClass phpClass : PsiTreeUtil.findChildrenOfType(myFile, PhpClass.class)) {
                if (fqn.equalsIgnoreCase(phpClass.getFQN())) {
                    result.add(phpClass);
                }
            }
            return result;
        }

        @Override
        public Collection<Function> getFunctionsByFQN(String fqn) {
            List<Function> result = new ArrayList<>();
            for (Function function : PsiTreeUtil.findChildrenOfType(myFile, Function.class)) {
                if (!(function instanceof Method) && fqn.equalsIgnoreCase(function.getFQN())) {
                    result.add(function);
                }
            }
            return result;
        }
    }

    private static final String CODE = """
        <?php
        namespace App;

        interface HasName { public function name(): string; }

        class Foo implements HasName {
            public Bar $bar;
            /** @var Baz */
            public $baz;
            public const VERSION = '1.0';

            public static function create(): static { return new static(); }
            public function self(): self { return $this; }
            public function bar(): Bar { return $this->bar; }
            /** @return Baz[] */
            public function bazList() { return []; }
            public function name(): string { return 'foo'; }
        }

        class Child extends Foo {}
        class Bar { public function count(): int { return 1; } }
        class Baz {}

        function helper(): ?Bar { return new Bar(); }

        /**
         * @param Baz $doc
         */
        function test(HasName $param, $doc) {
            $foo = new Foo();
            $child = Child::create();
            $self = $foo->self();
            $count = $foo->bar()->count();
            $list = $foo->bazList();
            foreach ($list as $item) {
                $element = $item;
            }
            $helper = helper();
            $field = $foo->bar;
            $docField = $foo->baz;
            $name = $param->name();
            $version = Foo::VERSION;
            $className = Foo::class;
            $documented = $doc;
            $mixed = $unknown ?? 1;
            $sum = 1 + 2;
            $flag = !$foo;
        }
        """;

    private static PsiFile parse(Context context) {
        return context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, CODE);
    }

    /**
     * Type of the variable assigned in {@code $name = ...}
     */
    private static PhpType localType(PsiFile file, String name) {
        for (PsiElement assignment : PsiTreeUtil.findChildrenOfType(file, PsiElement.class)) {
            if (assignment.getNode().getElementType() == PhpElementTypes.ASSIGNMENT_EXPRESSION && assignment.getFirstChild().getText().equals(name)) {
                return ((PhpTypedElement) assignment.getFirstChild()).getType();
            }
        }
        throw new AssertionError(name + " not assigned");
    }

    private static Set<String> global(Context context, PsiFile file, String name) {
        PhpType type = localType(file, name);
        return new TreeSet<>(new FileIndex(context.project(), file).completeType(context.project(), type, null).getTypes());
    }

    @Test
    public void testLocalTypes(Context context) {
        PsiFile file = parse(context);
        Assertions.assertEquals(Set.of("\\App\\Foo"), localType(file, "$foo").getTypes());
        Assertions.assertEquals(Set.of("#M#C\\App\\Child.create"), localType(file, "$child").getTypes());
        Assertions.assertEquals(Set.of("#M#M#C\\App\\Foo.bar.count"), localType(file, "$count").getTypes());
        Assertions.assertEquals(Set.of("#P#C\\App\\Foo.bar"), localType(file, "$field").getTypes());
        Assertions.assertEquals(Set.of("#K#C\\App\\Foo.VERSION"), localType(file, "$version").getTypes());
        Assertions.assertEquals(Set.of("\\string"), localType(file, "$className").getTypes());
        Assertions.assertEquals(Set.of("\\int", "\\float"), localType(file, "$sum").getTypes());
        Assertions.assertEquals(Set.of("\\bool"), localType(file, "$flag").getTypes());

        MethodReference count = (MethodReference) PsiTreeUtil.findChildrenOfType(file, MethodReference.class).stream()
            .filter(reference -> "count".equals(reference.getName())).findFirst().orElseThrow();
        Assertions.assertEquals("#M#M#C\\App\\Foo.bar.count", count.getSignature());
    }

    @Test
    public void testInlineVarTypes(Context context) {
        PsiFile file = context.psiFileFactory().createFileFromText("b.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            /** @var \\App\\Foo $typeFirst */
            $typeFirst->a();
            /** @var $nameFirst \\App\\Bar */
            $nameFirst->b();
            """);
        Assertions.assertEquals(Set.of("\\App\\Foo"), receiverType(file, "a"));
        // the legacy order of the inline @var
        Assertions.assertEquals(Set.of("\\App\\Bar"), receiverType(file, "b"));
    }

    private static Set<String> receiverType(PsiFile file, String method) {
        MethodReference reference = PsiTreeUtil.findChildrenOfType(file, MethodReference.class).stream()
            .filter(r -> method.equals(r.getName())).findFirst().orElseThrow();
        return ((PhpTypedElement) reference.getClassReference()).getType().getTypes();
    }

    @Test
    public void testDeclaredTypes(Context context) {
        PsiFile file = parse(context);
        PhpClass foo = PsiTreeUtil.findChildrenOfType(file, PhpClass.class).stream().filter(c -> "Foo".equals(c.getName())).findFirst().orElseThrow();

        Assertions.assertEquals(Set.of("\\App\\Foo"), foo.findOwnMethodByName("self").getType().getTypes());
        Assertions.assertEquals(Set.of("\\App\\Baz[]"), foo.findOwnMethodByName("bazList").getType().getTypes());
        Assertions.assertEquals(Set.of("\\App\\Bar"), foo.findOwnFieldByName("bar", false).getType().getTypes());
        Assertions.assertEquals(Set.of("\\App\\Baz"), foo.findOwnFieldByName("baz", false).getType().getTypes());
        Assertions.assertEquals(Set.of("\\string"), foo.findOwnFieldByName("VERSION", true).getType().getTypes());
    }

    @Test
    public void testCompletedTypes(Context context) {
        PsiFile file = parse(context);
        // late static binding: 'static' is the class the method was called on
        Assertions.assertEquals(Set.of("\\App\\Child"), global(context, file, "$child"));
        Assertions.assertEquals(Set.of("\\App\\Foo"), global(context, file, "$self"));
        Assertions.assertEquals(Set.of("\\int"), global(context, file, "$count"));
        Assertions.assertEquals(Set.of("\\App\\Baz[]"), global(context, file, "$list"));
        Assertions.assertEquals(Set.of("\\App\\Baz"), global(context, file, "$element"));
        Assertions.assertEquals(Set.of("\\App\\Bar", "\\null"), global(context, file, "$helper"));
        Assertions.assertEquals(Set.of("\\App\\Bar"), global(context, file, "$field"));
        Assertions.assertEquals(Set.of("\\App\\Baz"), global(context, file, "$docField"));
        Assertions.assertEquals(Set.of("\\string"), global(context, file, "$name"));
        Assertions.assertEquals(Set.of("\\string"), global(context, file, "$version"));
        Assertions.assertEquals(Set.of("\\App\\Baz"), global(context, file, "$documented"));
        Assertions.assertEquals(Set.of("\\int"), global(context, file, "$mixed"));
    }

    @Test
    public void testGetBySignature(Context context) {
        PsiFile file = parse(context);
        FileIndex index = new FileIndex(context.project(), file);

        Collection<? extends PhpNamedElement> methods = index.getBySignature("#M#M#C\\App\\Foo.bar.count");
        Assertions.assertEquals(1, methods.size());
        Assertions.assertEquals("count", methods.iterator().next().getName());

        // inherited through the parent class
        Assertions.assertEquals(1, index.getBySignature("#M#C\\App\\Child.bar").size());
        Assertions.assertEquals(2, index.getBySignature("#C\\App\\Foo|#C\\App\\Bar").size());
        Assertions.assertEquals(1, index.getBySignature("#F\\App\\helper").size());
    }

    @Test
    public void testTypeProvider(Context context) {
        PsiFile file = context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            namespace App;

            class Container { public function get(string $id): object {} }
            class Mailer { public function send(): bool {} }

            function test(Container $container) {
                $mailer = $container->get('app.mailer');
                $sent = $container->get('app.mailer')->send();
                $other = $container->get('unknown');
            }
            """);

        PhpType local = localType(file, "$mailer");
        Assertions.assertTrue(local.getTypes().contains("#M#C\\App\\Container.get"), local.toString());
        Assertions.assertTrue(local.getTypes().stream().anyMatch(type -> type.startsWith("#" + TestServiceTypeProvider.KEY)), local.toString());

        Assertions.assertEquals(Set.of("\\App\\Mailer", "\\object"), global(context, file, "$mailer"));
        // the provider type is completed before members are looked up on it
        Assertions.assertEquals(Set.of("\\bool"), global(context, file, "$sent"));
        Assertions.assertEquals(Set.of("\\object"), global(context, file, "$other"));
    }
}
