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

package consulo.php.phpsrc;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiErrorElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import com.jetbrains.php.config.PhpLanguageLevel;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.sdk.PhpRuntimeStubs;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Parses the PHP runtime stubs bundled in php-api ({@code phpstorm-stubs}): every file must parse cleanly and build stubs.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpRuntimeStubsParsingTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    /**
     * Stubs that are not valid user code: {@code function exit()} is a syntax error in a PHP file
     * (php-src {@code Zend/tests/exit/define_exit_function.phpt}), but the stubs declare it. The parser reports it and keeps the function.
     */
    private static final Set<String> NOT_USER_CODE = Set.of("Core/Core.php");

    @TestFactory
    public Stream<DynamicTest> runtimeStubs(Context context) throws IOException, URISyntaxException {
        URL license = PhpRuntimeStubs.class.getResource(PhpRuntimeStubs.RESOURCE_ROOT + "/LICENSE");
        Assertions.assertNotNull(license, "phpstorm-stubs not bundled in php-api");

        Path root = toPath(license.toURI()).getParent();
        List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk.filter(it -> it.toString().endsWith(".php")).sorted().toList();
        }
        Assertions.assertFalse(files.isEmpty());

        return files.stream().map(file -> DynamicTest.dynamicTest(root.relativize(file).toString(), () -> {
            String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8).replace("\r\n", "\n");
            PsiFile psiFile = context.psiFileFactory().createFileFromText(file.getFileName().toString(), PhpLanguage.INSTANCE,
                PhpLanguageLevel.HIGHEST, text);
            Assertions.assertEquals(text, psiFile.getText(), "PSI text differs from source");

            PsiErrorElement error = findFirstError(psiFile);
            if (error != null && !NOT_USER_CODE.contains(root.relativize(file).toString())) {
                Assertions.fail("offset " + error.getTextRange().getStartOffset() + ": " + error.getErrorDescription()
                    + " near '" + text.substring(error.getTextRange().getStartOffset(), Math.min(text.length(), error.getTextRange().getStartOffset() + 60)) + "'");
            }
            PhpStubElements.FILE.getBuilder().buildStubTree(psiFile);
        }));
    }

    /**
     * The stubs are on the classpath as a directory or inside the php-impl jar
     */
    private static Path toPath(URI uri) throws IOException {
        if (!"jar".equals(uri.getScheme())) {
            return Paths.get(uri);
        }
        FileSystem fileSystem;
        try {
            fileSystem = FileSystems.getFileSystem(uri);
        }
        catch (FileSystemNotFoundException e) {
            fileSystem = FileSystems.newFileSystem(uri, Map.of());
        }
        String entry = uri.getSchemeSpecificPart().substring(uri.getSchemeSpecificPart().indexOf("!/") + 1);
        return fileSystem.getPath(entry);
    }

    private static PsiErrorElement findFirstError(PsiFile file) {
        PsiErrorElement[] result = new PsiErrorElement[1];
        file.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiErrorElement error) {
                    result[0] = error;
                    stopWalking();
                    return;
                }
                super.visitElement(element);
            }
        });
        return result[0];
    }
}
