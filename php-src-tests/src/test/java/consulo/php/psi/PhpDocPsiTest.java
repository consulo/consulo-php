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
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocStubElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocToken;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

/**
 * PHPDoc trees in the shape of PhpStorm's: what the Doctrine annotation and Symfony plugins match on.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpDocPsiTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static List<PhpDocComment> docComments(Context context, String text) {
        PsiFile file = context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
        return List.copyOf(PsiTreeUtil.findChildrenOfType(file, PhpDocComment.class));
    }

    private static IElementType typeOf(PsiElement element) {
        return element.getNode().getElementType();
    }

    private static PsiElement findLeaf(PsiElement root, IElementType type, String text) {
        for (PsiElement leaf : PsiTreeUtil.findChildrenOfType(root, PhpDocToken.class)) {
            if (typeOf(leaf) == type && text.equals(leaf.getText())) {
                return leaf;
            }
        }
        throw new AssertionError(type + " '" + text + "' not found");
    }

    @Test
    public void testAnnotation(Context context) {
        PhpDocComment comment = docComments(context, """
            <?php
            /**
             * @ORM\\Entity(repositoryClass="App\\Repository\\FooRepository")
             * @ORM\\Table(name="foo", indexes={@ORM\\Index(name="idx", columns={"a", "b"})})
             */
            class Foo {}
            """).get(0);

        PhpDocTag[] entity = comment.getTagElementsByName("@ORM\\Entity");
        Assertions.assertEquals(1, entity.length);
        Assertions.assertEquals(PhpDocStubElementTypes.phpDocTag, typeOf(entity[0]));
        Assertions.assertEquals(PhpDocTokenTypes.DOC_TAG_NAME, typeOf(entity[0].getFirstChild()));

        PsiElement attributes = entity[0].getFirstChild().getNextSibling();
        Assertions.assertEquals(PhpDocElementTypes.phpDocAttributeList, typeOf(attributes));

        PsiElement property = findLeaf(attributes, PhpDocTokenTypes.DOC_IDENTIFIER, "repositoryClass");
        Assertions.assertEquals(attributes, property.getParent());
        Assertions.assertEquals(PhpDocTokenTypes.DOC_TEXT, typeOf(property.getNextSibling()));
        Assertions.assertEquals("=", property.getNextSibling().getText());

        PsiElement value = property.getNextSibling().getNextSibling();
        Assertions.assertEquals(PhpDocElementTypes.phpDocString, typeOf(value));
        Assertions.assertEquals(PhpDocTokenTypes.DOC_STRING, typeOf(value.getFirstChild()));
        Assertions.assertEquals("App\\Repository\\FooRepository", ((StringLiteralExpression) value).getContents());

        // nested annotations are tags too, but not top-level ones
        Assertions.assertEquals(0, comment.getTagElementsByName("@ORM\\Index").length);
        List<PhpDocTag> nested = new ArrayList<>();
        PhpDocUtil.consumeTagElementsByName(comment, "@ORM\\Index", nested::add);
        Assertions.assertEquals(1, nested.size());
        Assertions.assertEquals(PhpDocElementTypes.phpDocAttributeList, typeOf(nested.get(0).getParent()));

        List<PhpDocTag> all = new ArrayList<>();
        PhpDocUtil.processTagElementsByPredicate(comment, all::add, tag -> true);
        Assertions.assertEquals(3, all.size());
    }

    @Test
    public void testAnnotationScalars(Context context) {
        PhpDocComment comment = docComments(context, """
            <?php
            class Foo {
                /** @ORM\\Column(type="string", length=255, nullable=true, enumType=Status::class) */
                private $name;
            }
            """).get(0);
        PhpDocTag column = comment.getTagElementsByName("@ORM\\Column")[0];

        PsiElement length = findLeaf(column, PhpDocTokenTypes.DOC_IDENTIFIER, "length");
        Assertions.assertEquals("=", length.getNextSibling().getText());
        Assertions.assertEquals(PhpDocTokenTypes.DOC_INTEGER, typeOf(length.getNextSibling().getNextSibling()));

        PsiElement status = findLeaf(column, PhpDocTokenTypes.DOC_IDENTIFIER, "Status");
        Assertions.assertEquals(PhpDocTokenTypes.DOC_STATIC, typeOf(status.getNextSibling()));
    }

    @Test
    public void testTypedTags(Context context) {
        PhpDocComment comment = docComments(context, """
            <?php
            /**
             * Summary.
             *
             * @param string|null $a the a
             *        continued
             * @param int ...$rest
             * @param \\App\\Foo&\\App\\Bar $both
             * @return array<string, int>
             * @throws \\RuntimeException when broken
             */
            function f($a, ...$rest) {}
            """).get(0);

        PhpDocParamTag[] params = comment.getParamTags();
        Assertions.assertEquals(3, params.length);

        PhpDocParamTag a = comment.getParamTagByName("a");
        Assertions.assertNotNull(a);
        Assertions.assertEquals("@param", a.getName());
        Assertions.assertEquals("string|null", a.getTypeString());
        Assertions.assertEquals("string|null $a the a continued", a.getTagValue());

        PhpDocParamTag rest = comment.getParamTagByName("rest");
        Assertions.assertNotNull(rest);
        Assertions.assertEquals("int", rest.getTypeString());

        Assertions.assertEquals("\\App\\Foo&\\App\\Bar", comment.getParamTagByName("both").getTypeString());

        PhpDocReturnTag returnTag = comment.getReturnTag();
        Assertions.assertNotNull(returnTag);
        Assertions.assertEquals("array<string, int>", returnTag.getTypeString());

        Assertions.assertEquals("\\RuntimeException when broken", comment.getTagElementsByName("@throws")[0].getTagValue());
    }

    @Test
    public void testEmailIsNotATag(Context context) {
        PhpDocComment comment = docComments(context, """
            <?php
            /**
             * Contact me@example.com
             * @author Me <me@example.com>
             */
            function f() {}
            """).get(0);

        List<PhpDocTag> all = new ArrayList<>();
        PhpDocUtil.consumeTagElementsByName(comment, null, all::add);
        Assertions.assertEquals(List.of("@author"), all.stream().map(PhpDocTag::getName).toList());
    }

    @Test
    public void testStringContents(Context context) {
        PsiFile file = context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, """
            <?php
            $a = 'single';
            $b = "double";
            $c = b'binary';
            """);
        List<StringLiteralExpression> strings = List.copyOf(PsiTreeUtil.findChildrenOfType(file, StringLiteralExpression.class));

        Assertions.assertEquals(List.of("single", "double", "binary"), strings.stream().map(StringLiteralExpression::getContents).toList());
        Assertions.assertEquals(List.of(true, false, true), strings.stream().map(StringLiteralExpression::isSingleQuote).toList());
    }
}
