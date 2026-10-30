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

package consulo.php.it;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.roots.PhpNamespaceCompositeProvider;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpAttributesFQNsIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpClassFqnIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpInterfaceFqnIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpNamespaceIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpTraitFqnIndex;
import consulo.application.ReadAction;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.php.sdk.PhpRuntimeStubs;
import consulo.it.HeadlessProjectExtension;
import consulo.it.HeadlessProjects;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.StubIndex;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The PHP indexes over real files: FQN file indexes, namespaces, the bundled runtime stubs and types across files.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(HeadlessProjectExtension.class)
public class PhpIndexIntegrationTest {
    private static final String HOME_CONTROLLER = """
        <?php
        namespace App\\Controller;

        use App\\Service\\Mailer;
        use Symfony\\Component\\Routing\\Attribute\\Route;

        #[Route('/', name: 'home')]
        class HomeController {
            public function __construct(private Mailer $mailer) {}

            public function index() {
                $sent = $this->mailer->send();
            }
        }
        """;

    private static final String SERVICES = """
        <?php
        namespace App\\Service;

        interface MailerInterface {}
        trait Loggable {}
        enum Status: string { case On = 'on'; }

        const DEFAULT_SENDER = 'noreply@example.com';

        class Mailer implements MailerInterface {
            use Loggable;

            public function send(): bool { return true; }
        }
        """;

    private static Project open(HeadlessProjects projects) throws Exception {
        return PhpProjects.open(projects, Map.of(
            "src/Controller/HomeController.php", HOME_CONTROLLER,
            "src/Service/Mailer.php", SERVICES
        ));
    }

    @Test
    public void fqnFileIndexes(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        FileBasedIndex index = FileBasedIndex.getInstance();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        Collection<String> classes = PhpProjects.waitForIndex(() -> index.getAllKeys(PhpClassFqnIndex.KEY, project),
            keys -> keys.contains("\\App\\Controller\\HomeController"));
        assertThat(classes).contains("\\App\\Service\\Mailer", "\\App\\Service\\Status")
            .doesNotContain("\\App\\Service\\MailerInterface", "\\App\\Service\\Loggable");

        assertThat(ReadAction.compute(() -> index.getAllKeys(PhpInterfaceFqnIndex.KEY, project))).contains("\\App\\Service\\MailerInterface");
        assertThat(ReadAction.compute(() -> index.getAllKeys(PhpTraitFqnIndex.KEY, project))).contains("\\App\\Service\\Loggable");
        assertThat(ReadAction.compute(() -> index.getAllKeys(PhpAttributesFQNsIndex.KEY, project)))
            .contains("\\Symfony\\Component\\Routing\\Attribute\\Route");

        // lookups are exact: the case of the declaration
        assertThat(ReadAction.compute(() -> index.getValues(PhpClassFqnIndex.KEY, "\\App\\Service\\Mailer", scope))).hasSize(1);
        assertThat(ReadAction.compute(() -> index.getValues(PhpClassFqnIndex.KEY, "\\app\\service\\mailer", scope))).isEmpty();
        List<String> files = ReadAction.compute(() -> index.getContainingFiles(PhpClassFqnIndex.KEY, "\\App\\Service\\Mailer", scope)
            .stream().map(VirtualFile::getName).toList());
        assertThat(files).containsExactly("Mailer.php");
    }

    @Test
    public void namespaces(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        PhpProjects.waitForIndex(() -> phpIndex.getNamespacesByName("\\App\\Controller"), namespaces -> !namespaces.isEmpty());

        // keys are lowercased like PhpStorm's, queries are case-insensitive
        Collection<PhpNamespace> namespaces = ReadAction.compute(() -> StubIndex.getElements(PhpNamespaceIndex.KEY, "\\app\\controller",
            project, GlobalSearchScope.allScope(project), PhpNamespace.class));
        assertThat(namespaces).hasSize(1);
        assertThat(ReadAction.compute(() -> phpIndex.getNamespacesByName("\\app\\service"))).hasSize(1);

        // a parent namespace without a declaration, children with the declared case
        assertThat(ReadAction.compute(() -> phpIndex.getNamespacesByName("\\App"))).hasSize(1);
        assertThat(ReadAction.compute(() -> phpIndex.getChildNamespacesByParentName("\\App\\"))).containsExactlyInAnyOrder("Controller", "Service");
        assertThat(ReadAction.compute(() -> phpIndex.getChildNamespacesByParentName("\\app"))).containsExactlyInAnyOrder("Controller", "Service");
    }

    private static Project openWithPhpSdk(HeadlessProjects projects) throws Exception {
        return PhpProjects.open(projects, Map.of("src/Service/Mailer.php", SERVICES), PhpProjects.createPhpSdk());
    }

    @Test
    public void phpSdkRootsAreTheRuntimeStubs(HeadlessProjects projects) throws Exception {
        Sdk sdk = PhpProjects.createPhpSdk();

        VirtualFile root = PhpRuntimeStubs.getRoot();
        assertThat(root).as("stubs at " + PhpRuntimeStubs.class.getResource(PhpRuntimeStubs.RESOURCE_ROOT)).isNotNull();
        assertThat(sdk.getRootProvider().getFiles(BinariesOrderRootType.ID)).containsExactly(root);
        assertThat(sdk.getRootProvider().getFiles(SourcesOrderRootType.ID)).containsExactly(root);
        assertThat(ReadAction.compute(() -> root.findFileByRelativePath("date/date.php"))).isNotNull();
    }

    @Test
    public void runtimeStubsResolveThroughThePhpSdk(HeadlessProjects projects) throws Exception {
        Project project = openWithPhpSdk(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        Collection<PhpClass> dateTime = PhpProjects.waitForIndex(() -> phpIndex.getClassesByFQN("\\DateTimeImmutable"), classes -> !classes.isEmpty());
        assertThat(ReadAction.compute(() -> dateTime.iterator().next().getContainingFile().getVirtualFile().getPath())).contains("phpstorm-stubs");
        assertThat(ReadAction.compute(() -> phpIndex.getInterfacesByFQN("\\Stringable"))).isNotEmpty();
        Collection<Function> strlen = ReadAction.compute(() -> phpIndex.getFunctionsByFQN("\\strlen"));
        assertThat(strlen).hasSize(1);
        assertThat(ReadAction.compute(() -> strlen.iterator().next().getType().getTypes())).contains("\\int");

        // SDK roots are libraries: plain index queries over all scope see the stubs, as the Symfony plugin queries them
        FileBasedIndex index = FileBasedIndex.getInstance();
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);
        assertThat(ReadAction.compute(() -> index.getValues(PhpClassFqnIndex.KEY, "\\Exception", allScope))).hasSize(1);
        assertThat(ReadAction.compute(() -> index.getValues(PhpInterfaceFqnIndex.KEY, "\\Traversable", allScope))).hasSize(1);
        assertThat(ReadAction.compute(() -> index.getValues(PhpClassFqnIndex.KEY, "\\App\\Service\\Mailer", allScope))).hasSize(1);
    }

    @Test
    public void runtimeStubsNeedThePhpSdk(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        PhpProjects.waitForIndex(() -> phpIndex.getClassesByFQN("\\App\\Service\\Mailer"), classes -> !classes.isEmpty());
        assertThat(ReadAction.compute(() -> phpIndex.getClassesByFQN("\\DateTimeImmutable"))).isEmpty();
    }

    @Test
    public void typesAcrossFiles(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);
        PhpProjects.waitForIndex(() -> phpIndex.getClassesByFQN("\\App\\Service\\Mailer"), classes -> !classes.isEmpty());

        Set<String> types = ReadAction.compute(() -> {
            VirtualFile file = project.getBaseDir().findFileByRelativePath("src/Controller/HomeController.php");
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            MethodReference send = PsiTreeUtil.findChildrenOfType(psiFile, MethodReference.class).stream()
                .filter(reference -> "send".equals(reference.getName())).findFirst().orElseThrow();
            PhpType type = phpIndex.completeType(project, send.getType(), null);
            return type.getTypes();
        });
        assertThat(types).containsExactly("\\bool");
    }

    /**
     * Promoted constructor parameters are fields of the class, also when the class comes from stubs
     */
    @Test
    public void promotedFieldsFromStubs(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);
        Collection<PhpClass> classes = PhpProjects.waitForIndex(() -> phpIndex.getClassesByFQN("\\App\\Controller\\HomeController"),
            found -> !found.isEmpty());

        assertThat(ReadAction.compute(() -> {
            Field mailer = classes.iterator().next().findOwnFieldByName("mailer", false);
            return mailer == null ? null : mailer.getModifier().getAccess() + " " + mailer.getType().toString();
        })).isEqualTo("private \\App\\Service\\Mailer");
    }

    /**
     * {@code const} outside of classes is indexed with the {@code define()} constants
     */
    @Test
    public void namespaceConstants(HeadlessProjects projects) throws Exception {
        Project project = open(projects);
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        Collection<Constant> constants = PhpProjects.waitForIndex(() -> phpIndex.getConstantsByName("DEFAULT_SENDER"), found -> !found.isEmpty());
        assertThat(ReadAction.compute(() -> constants.iterator().next().getFQN())).isEqualTo("\\App\\Service\\DEFAULT_SENDER");
        assertThat(ReadAction.compute(() -> constants.iterator().next().getValuePresentation())).isEqualTo("'noreply@example.com'");
    }

    @Test
    public void phpUnitTestClasses(HeadlessProjects projects) throws Exception {
        Project project = PhpProjects.open(projects, Map.of(
            "vendor/phpunit/TestCase.php", "<?php\nnamespace PHPUnit\\Framework;\nabstract class TestCase {}\n",
            "tests/MailerTest.php", "<?php\nnamespace App\\Tests;\nuse PHPUnit\\Framework\\TestCase;\nclass Base extends TestCase {}\nclass MailerTest extends Base {}\n",
            "src/Service/Mailer.php", SERVICES
        ));
        PhpIndex phpIndex = PhpIndex.getInstance(project);
        Collection<PhpClass> tests = PhpProjects.waitForIndex(() -> phpIndex.getClassesByFQN("\\App\\Tests\\MailerTest"), found -> !found.isEmpty());
        Collection<PhpClass> mailer = ReadAction.compute(() -> phpIndex.getClassesByFQN("\\App\\Service\\Mailer"));

        assertThat(ReadAction.compute(() -> PhpUnitUtil.isTestClass(tests.iterator().next()))).isTrue();
        assertThat(ReadAction.compute(() -> PhpUnitUtil.isPhpUnitTestFile(tests.iterator().next().getContainingFile()))).isTrue();
        assertThat(ReadAction.compute(() -> PhpUnitUtil.isTestClass(mailer.iterator().next()))).isFalse();
    }

    @Test
    public void psr4Namespaces(HeadlessProjects projects) throws Exception {
        Project project = PhpProjects.open(projects, Map.of(
            "composer.json", "{\"autoload\": {\"psr-4\": {\"App\\\\\": \"src/\"}}, \"autoload-dev\": {\"psr-4\": {\"App\\\\Tests\\\\\": [\"tests/\"]}}}",
            "src/Controller/Admin/Dashboard.php", "<?php\n",
            "tests/Unit/FooTest.php", "<?php\n"
        ));
        List<String> controller = ReadAction.compute(() -> {
            VirtualFile directory = project.getBaseDir().findFileByRelativePath("src/Controller/Admin");
            return PhpNamespaceCompositeProvider.INSTANCE.suggestNamespaces(PsiManager.getInstance(project).findDirectory(directory));
        });
        assertThat(controller).containsExactly("App\\Controller\\Admin");
        List<String> tests = ReadAction.compute(() -> {
            VirtualFile directory = project.getBaseDir().findFileByRelativePath("tests/Unit");
            return PhpNamespaceCompositeProvider.INSTANCE.suggestNamespaces(PsiManager.getInstance(project).findDirectory(directory));
        });
        assertThat(tests).containsExactly("App\\Tests\\Unit");
    }
}
