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
package consulo.php.impl.lang.spellchecker;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiUtilCore;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.spellcheker.text.TextContent;
import consulo.language.spellcheker.text.TextContentBuilder;
import consulo.language.spellcheker.text.TextExtractor;
import consulo.language.spellcheker.text.util.EscapeUtils;
import consulo.language.spellcheker.text.util.HtmlUtils;
import consulo.language.spellcheker.text.util.PsiUtils;
import consulo.language.spellcheker.text.util.Text;
import consulo.util.collection.ContainerUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consulo.language.spellcheker.text.TextContent.TextDomain.COMMENTS;
import static consulo.language.spellcheker.text.TextContent.TextDomain.DOCUMENTATION;
import static consulo.language.spellcheker.text.TextContent.TextDomain.LITERALS;

/**
 * Natural language text in PHP:
 * <ul>
 *   <li>PHPDoc comments: the description and the descriptions of block tags; tag names, types, variable names
 *   and non-prose tags are excluded, inline tags and code spans are unknown fragments,</li>
 *   <li>{@code //} and {@code #} (adjacent ones joined) and {@code /* *}{@code /} comments, except tool directives,</li>
 *   <li>single and double quoted strings and heredoc/nowdoc bodies; interpolations are unknown fragments, escapes resolved;
 *   array keys, compared values and included paths are skipped.</li>
 * </ul>
 */
@ExtensionImpl
public class PhpTextExtractor extends TextExtractor {
    private static final TokenSet STRING_CONTENTS = TokenSet.create(
        PhpTokenTypes.STRING_LITERAL,
        PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE,
        PhpTokenTypes.HEREDOC_CONTENTS,
        PhpTokenTypes.ESCAPE_SEQUENCE
    );
    private static final TokenSet STRING_BOUNDARIES = TokenSet.create(
        PhpTokenTypes.chDOUBLE_QUOTE,
        PhpTokenTypes.HEREDOC_START,
        PhpTokenTypes.HEREDOC_END
    );

    private static final TextContentBuilder COMMENT_BUILDER =
        TextContentBuilder.FromPsi.removingIndents(" \t*").removingLineSuffixes(" \t");

    private static final Pattern BLOCK_TAG = Pattern.compile("(?m)^[ \t]*@([A-Za-z][\\w-]*)");
    private static final Pattern INLINE_UNKNOWN = Pattern.compile("\\{@[^}]*}|`[^`\n]*`");
    private static final Pattern SINGLE_QUOTE_ESCAPE = Pattern.compile("\\\\[\\\\']");
    private static final Pattern DIRECTIVE = Pattern.compile(
        "^\\s*(?:@phpstan-|@psalm-|@phan-|@codeCoverageIgnore|@formatter:|@noinspection|@var\\b|@lang\\b|phpcs:|phpcs\\b|"
            + "noinspection|php-cs-fixer|@infection-ignore|language=)"
    );

    private static final Set<String> PROSE_TAGS = Set.of(
        "param", "var", "property", "property-read", "property-write",
        "return", "returns", "throws", "deprecated", "todo", "internal", "description", "summary"
    );
    private static final Set<String> TYPED_TAGS = Set.of(
        "param", "var", "property", "property-read", "property-write", "return", "returns", "throws"
    );
    private static final Set<String> SKIPPED_HEREDOC_LABELS = Set.of("SQL", "JS", "JAVASCRIPT", "JSON", "CSS", "XML", "REGEXP");

    private static final Set<String> SKIPPED_BEFORE_LITERAL = Set.of(
        "case", "==", "===", "!=", "!==", "<>", "require", "require_once", "include", "include_once"
    );
    private static final Set<String> SKIPPED_AFTER_LITERAL = Set.of("=>", "==", "===", "!=", "!==", "<>");

    @Override
    @RequiredReadAction
    protected List<TextContent> buildTextContents(PsiElement element, Set<TextContent.TextDomain> allowedDomains) {
        if (element instanceof PhpDocComment) {
            if (!allowedDomains.contains(DOCUMENTATION)) {
                return List.of();
            }
            return buildDocumentation(element);
        }

        IElementType type = PsiUtilCore.getElementType(element);
        if (type == null) {
            return List.of();
        }

        if (element instanceof PsiComment) {
            if (!allowedDomains.contains(COMMENTS) || (type != PhpTokenTypes.LINE_COMMENT && type != PhpTokenTypes.C_STYLE_COMMENT)) {
                return List.of();
            }
            return buildComments(element, type);
        }

        if (type == PhpElementTypes.STRING) {
            if (!allowedDomains.contains(LITERALS) || isNotProseLiteral(element)) {
                return List.of();
            }
            return buildString(element);
        }

        if (type == PhpElementTypes.HEREDOC) {
            if (!allowedDomains.contains(LITERALS)) {
                return List.of();
            }
            return buildHeredoc(element);
        }
        return List.of();
    }

    @RequiredReadAction
    private static List<TextContent> buildComments(PsiElement element, IElementType type) {
        List<PsiElement> roots = PsiUtils.getNotSoDistantSimilarSiblings(
            element,
            it -> it instanceof PsiComment && !(it instanceof PhpDocComment) && PsiUtilCore.getElementType(it) == type
        );
        List<TextContent> fragments = new ArrayList<>();
        for (PsiElement root : roots) {
            TextContent fragment = buildCommentBody(root, COMMENTS);
            if (fragment != null && !DIRECTIVE.matcher(fragment).find()) {
                fragments.add(fragment);
            }
        }
        return ContainerUtil.createMaybeSingletonList(TextContent.joinWithWhitespace('\n', fragments));
    }

    /**
     * The comment text without its {@code //}, {@code #}, {@code /*}, {@code /**} and {@code *}{@code /} delimiters
     * and leading asterisks.
     */
    @RequiredReadAction
    private static @Nullable TextContent buildCommentBody(PsiElement comment, TextContent.TextDomain domain) {
        String text = comment.getText();
        int start = 0;
        int end = text.length();
        if (text.startsWith("//")) {
            start = 2;
        }
        else if (text.startsWith("#")) {
            start = 1;
        }
        else if (text.startsWith("/*")) {
            start = text.startsWith("/**") && end >= 5 ? 3 : 2;
            if (end >= 4 && text.endsWith("*/")) {
                end -= 2;
            }
        }
        if (start >= end) {
            return null;
        }
        return COMMENT_BUILDER.build(comment, domain, new TextRange(start, end));
    }

    @RequiredReadAction
    private static List<TextContent> buildDocumentation(PsiElement comment) {
        TextContent content = buildCommentBody(comment, DOCUMENTATION);
        if (content == null) {
            return List.of();
        }

        String text = content.toString();
        Matcher matcher = BLOCK_TAG.matcher(text);
        int descriptionEnd = text.length();
        List<int[]> tags = new ArrayList<>();
        while (matcher.find()) {
            if (tags.isEmpty()) {
                descriptionEnd = matcher.start();
            }
            tags.add(new int[]{matcher.start(1), matcher.end(1)});
        }

        List<TextRange> proseRanges = new ArrayList<>();
        proseRanges.add(new TextRange(0, descriptionEnd));
        for (int i = 0; i < tags.size(); i++) {
            int nameStart = tags.get(i)[0];
            int nameEnd = tags.get(i)[1];
            int bodyEnd = i + 1 < tags.size() ? tags.get(i + 1)[0] - 1 : text.length();
            String name = text.substring(nameStart, nameEnd).toLowerCase(Locale.ROOT);
            if (!PROSE_TAGS.contains(name)) {
                continue;
            }
            int start = skipWhitespace(text, nameEnd, bodyEnd);
            if (TYPED_TAGS.contains(name) && start < bodyEnd && text.charAt(start) != '$') {
                start = skipType(text, start, bodyEnd);
                start = skipWhitespace(text, start, bodyEnd);
            }
            if (isVariableStart(text, start, bodyEnd)) {
                start = skipWord(text, start, bodyEnd);
                start = skipWhitespace(text, start, bodyEnd);
            }
            if (start < bodyEnd) {
                proseRanges.add(new TextRange(start, bodyEnd));
            }
        }

        List<TextContent> result = new ArrayList<>();
        for (TextRange range : proseRanges) {
            TextContent part = content.subText(range);
            if (part == null) {
                continue;
            }
            part = part.excludeRanges(ContainerUtil.map(Text.allOccurrences(INLINE_UNKNOWN, part), TextContent.Exclusion::markUnknown));
            TextContent trimmed = part.trimWhitespace();
            if (trimmed != null) {
                result.addAll(HtmlUtils.excludeHtml(trimmed));
            }
        }
        return result;
    }

    @RequiredReadAction
    private static List<TextContent> buildString(PsiElement string) {
        PsiElement first = string.getFirstChild();
        if (first == null) {
            return List.of();
        }
        if (first == string.getLastChild()) {
            return buildSimpleString(string, PsiUtilCore.getElementType(first) == PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE);
        }
        TextContent content = interpolatedBuilder(string).build(string, LITERALS, new TextRange(0, string.getTextLength()));
        return content == null ? List.of() : finish(EscapeUtils.replaceBackslashEscapes(content));
    }

    @RequiredReadAction
    private static List<TextContent> buildSimpleString(PsiElement string, boolean singleQuoted) {
        String text = string.getText();
        int start = text.startsWith("b") || text.startsWith("B") ? 2 : 1;
        int end = text.length() > start && text.charAt(text.length() - 1) == text.charAt(start - 1) ? text.length() - 1 : text.length();
        if (start >= end) {
            return List.of();
        }
        TextContent content = TextContentBuilder.FromPsi.build(string, LITERALS, new TextRange(start, end));
        if (content == null) {
            return List.of();
        }
        if (singleQuoted) {
            return finish(content.excludeRanges(
                ContainerUtil.map(Text.allOccurrences(SINGLE_QUOTE_ESCAPE, content), TextContent.Exclusion::markUnknown)
            ));
        }
        return finish(EscapeUtils.replaceBackslashEscapes(content));
    }

    @RequiredReadAction
    private static List<TextContent> buildHeredoc(PsiElement heredoc) {
        PsiElement start = heredoc.getFirstChild();
        String opening = start == null ? "" : start.getText();
        String label = opening.replaceAll("[^A-Za-z0-9_]", "").replaceFirst("^[bB](?=[A-Za-z_])", "");
        if (SKIPPED_HEREDOC_LABELS.contains(label.toUpperCase(Locale.ROOT))) {
            return List.of();
        }
        TextContent content = interpolatedBuilder(heredoc)
            .removingIndents(" \t")
            .build(heredoc, LITERALS, new TextRange(0, heredoc.getTextLength()));
        if (content == null) {
            return List.of();
        }
        boolean nowdoc = opening.indexOf('\'') >= 0;
        return finish(nowdoc ? content : EscapeUtils.replaceBackslashEscapes(content));
    }

    /**
     * Text parts of a string or heredoc; the quotes and heredoc labels are excluded,
     * each interpolation ({@code $var}, {@code {$expr}}, {@code ${name}}) is a single unknown fragment.
     */
    private static TextContentBuilder interpolatedBuilder(PsiElement root) {
        return TextContentBuilder.FromPsi
            .excluding(e -> e.getParent() == root && STRING_BOUNDARIES.contains(PsiUtilCore.getElementType(e)))
            .withUnknown(e -> e.getParent() == root && !isStringPart(e) && isStringPart(e.getPrevSibling()))
            .excluding(e -> e.getParent() == root && !isStringPart(e));
    }

    private static boolean isStringPart(@Nullable PsiElement element) {
        IElementType type = PsiUtilCore.getElementType(element);
        return STRING_CONTENTS.contains(type) || STRING_BOUNDARIES.contains(type);
    }

    private static List<TextContent> finish(TextContent content) {
        return ContainerUtil.createMaybeSingletonList(content.trimWhitespace());
    }

    /**
     * Array keys ({@code ['key' => 1]}, {@code $a['key']}), values compared with something
     * and included file paths are identifiers rather than prose.
     */
    @RequiredReadAction
    private static boolean isNotProseLiteral(PsiElement literal) {
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(literal);
        PsiElement next = PsiTreeUtil.nextVisibleLeaf(literal);
        String prevText = prev == null ? "" : prev.getText().toLowerCase(Locale.ROOT);
        String nextText = next == null ? "" : next.getText();
        if (SKIPPED_BEFORE_LITERAL.contains(prevText) || SKIPPED_AFTER_LITERAL.contains(nextText)) {
            return true;
        }
        if (prevText.equals("[") && nextText.equals("]")) {
            return true;
        }
        if (prevText.equals("(") && prev != null) {
            PsiElement callee = PsiTreeUtil.prevVisibleLeaf(prev);
            String calleeText = callee == null ? "" : callee.getText().toLowerCase(Locale.ROOT);
            return SKIPPED_BEFORE_LITERAL.contains(calleeText);
        }
        return false;
    }

    private static boolean isVariableStart(String text, int start, int end) {
        int i = start;
        while (i < end && (text.charAt(i) == '&' || text.charAt(i) == '.')) {
            i++;
        }
        return i < end && text.charAt(i) == '$';
    }

    private static int skipWhitespace(String text, int start, int end) {
        int i = start;
        while (i < end && Character.isWhitespace(text.charAt(i))) {
            i++;
        }
        return i;
    }

    private static int skipWord(String text, int start, int end) {
        int i = start;
        while (i < end && !Character.isWhitespace(text.charAt(i))) {
            i++;
        }
        return i;
    }

    /**
     * Skips a PHPDoc type like {@code array<int, string>|null} or {@code array{key: int}}, brackets may contain spaces.
     */
    private static int skipType(String text, int start, int end) {
        int depth = 0;
        int i = start;
        while (i < end) {
            char c = text.charAt(i);
            if (c == '<' || c == '{' || c == '(' || c == '[') {
                depth++;
            }
            else if (c == '>' || c == '}' || c == ')' || c == ']') {
                depth = Math.max(0, depth - 1);
            }
            else if (Character.isWhitespace(c) && depth == 0) {
                break;
            }
            i++;
        }
        return i;
    }

    @Override
    public Language getLanguage() {
        return PhpLanguage.INSTANCE;
    }
}
