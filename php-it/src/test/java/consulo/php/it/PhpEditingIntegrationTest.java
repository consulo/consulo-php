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

import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil;
import com.jetbrains.php.completion.PhpClassLookupElement;
import com.jetbrains.php.lang.psi.PhpCodeEditUtil;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.refactoring.PhpAliasImporter;
import consulo.application.Application;
import consulo.application.ReadAction;
import consulo.application.WriteAction;
import consulo.application.internal.TransferredWriteActionService;
import consulo.it.AllowLogError;
import consulo.it.CodeInsightTestFixture;
import consulo.it.HeadlessProjectExtension;
import consulo.language.editor.completion.lookup.LookupElementPresentation;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.undoRedo.CommandProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Changes of PHP code in a project: imports, class members and lookup presentation.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(HeadlessProjectExtension.class)
// the XML plugin looks up its standard schemas with a leading '/' through a class loader, which fails on a plain classpath
@AllowLogError("consulo.xml.impl.internal.ExternalResource")
public class PhpEditingIntegrationTest {
    private static PhpClass findClass(PsiFile file) {
        return ReadAction.compute(() -> PsiTreeUtil.findChildOfType(file, PhpClass.class));
    }

    @Test
    public void importsIntoANamespace(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("HomeController.php", """
            <?php
            namespace App\\Controller;

            use App\\Service\\Mailer;

            class HomeController {}
            """);
        PhpPsiElement scope = ReadAction.compute(() -> PhpCodeInsightUtil.findScopeForUseOperator(findClass(file)));

        runCommand(fixture, () -> {
            PhpAliasImporter.insertUseStatement("\\Doctrine\\ORM\\Mapping", "ORM", scope);
            PhpAliasImporter.insertUseStatement("\\Symfony\\Component\\Routing\\Attribute\\Route", scope);
        });

        assertThat(ReadAction.compute(file::getText)).isEqualTo("""
            <?php
            namespace App\\Controller;

            use App\\Service\\Mailer;
            use Doctrine\\ORM\\Mapping as ORM;
            use Symfony\\Component\\Routing\\Attribute\\Route;

            class HomeController {}
            """);
        assertThat(ReadAction.compute(() -> PhpCodeInsightUtil.getAliasesInScope(scope))).containsEntry("ORM", "\\Doctrine\\ORM\\Mapping");
    }

    @Test
    public void importsIntoAFileWithoutNamespace(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("script.php", "<?php\nclass Script {}\n");
        PhpPsiElement scope = ReadAction.compute(() -> PhpCodeInsightUtil.findScopeForUseOperator(findClass(file)));

        runCommand(fixture, () -> PhpAliasImporter.insertUseStatement("Foo\\Bar", scope));

        assertThat(ReadAction.compute(file::getText)).isEqualTo("<?php\n\nuse Foo\\Bar;\nclass Script {}\n");
    }

    @Test
    public void insertsAClassMember(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("Foo.php", "<?php\nclass Foo {\n    public function a() {}\n}\n");
        PhpClass phpClass = findClass(file);

        runCommand(fixture, () -> {
            Method method = PhpPsiElementFactory.createMethod(fixture.getProject(), "public function b(): void {}");
            PhpCodeEditUtil.insertClassMember(phpClass, method);
        });

        assertThat(ReadAction.compute(() -> phpClass.findOwnMethodByName("b"))).isNotNull();
        assertThat(ReadAction.compute(file::getText)).contains("public function a() {}", "public function b(): void {}");
    }

    @Test
    public void classLookupPresentation(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("Mailer.php", "<?php\nnamespace App\\Service;\n\nclass Mailer {}\n");

        LookupElementPresentation presentation = ReadAction.compute(() -> {
            PhpClassLookupElement element = new PhpClassLookupElement(findClass(file), true, null);
            // the element icon comes from the icon deferrer, which the headless application does not have
            element.icon = PhpIcons.CLASS;
            LookupElementPresentation result = new LookupElementPresentation();
            element.renderElement(result);
            return result;
        });
        assertThat(presentation.getItemText()).isEqualTo("Mailer");
        assertThat(presentation.getTailText()).isEqualTo(" (App\\Service)");
        assertThat(presentation.getIcon()).isSameAs(PhpIcons.CLASS);
    }

    private static void runCommand(CodeInsightTestFixture fixture, Runnable change) throws Exception {
        Project project = fixture.getProject();
        WriteAction.run(() -> Application.get().getInstance(TransferredWriteActionService.class).runOnEdtWithTransferredWriteActionAndWait(
            () -> CommandProcessor.getInstance().executeCommand(project, () -> {
                change.run();
                PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(fixture.getDocument());
            }, "test", null)));
    }
}
