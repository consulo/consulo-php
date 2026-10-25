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

package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.parser.PhpElementTypes;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Name resolution by PHP's rules - current namespace and {@code use} imports - without the index,
 * so it is safe while indexing.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpNameResolver {
    private PhpNameResolver() {
    }

    /**
     * @return FQN with the leading backslash; {@code self}, {@code static} and {@code parent} resolve to the containing class
     */
    @RequiredReadAction
    public static @Nullable String resolveClassFqn(@Nullable ClassReference reference) {
        if (reference == null) {
            return null;
        }
        String name = nameText(reference);
        if (name.isEmpty()) {
            return null;
        }

        String lower = name.toLowerCase(Locale.ROOT);
        if (lower.equals("self") || lower.equals("static")) {
            PhpClass phpClass = PsiTreeUtil.getParentOfType(reference, PhpClass.class);
            return phpClass == null ? null : phpClass.getFQN();
        }
        if (lower.equals("parent")) {
            PhpClass phpClass = PsiTreeUtil.getParentOfType(reference, PhpClass.class);
            if (phpClass == null || phpClass.getExtendsList() == null || PsiTreeUtil.isAncestor(phpClass.getExtendsList(), reference, false)) {
                // 'class A extends parent' has no parent to refer to
                return null;
            }
            ClassReference superReference = PsiTreeUtil.getChildOfType(phpClass.getExtendsList(), ClassReference.class);
            return superReference == null ? null : resolveClassFqn(superReference);
        }
        return resolveName(reference, name);
    }

    /**
     * @param name as written: {@code \A\B}, {@code namespace\B}, {@code Alias\B} or {@code B}
     */
    @RequiredReadAction
    public static String resolveName(PsiElement context, String name) {
        if (name.startsWith("\\")) {
            return name;
        }
        String namespace = getNamespaceName(context);
        if (name.regionMatches(true, 0, "namespace\\", 0, 10)) {
            return qualify(namespace, name.substring(10));
        }

        int separator = name.indexOf('\\');
        String first = separator < 0 ? name : name.substring(0, separator);
        String imported = getClassImports(context).get(first.toLowerCase(Locale.ROOT));
        if (imported != null) {
            return separator < 0 ? imported : imported + name.substring(separator);
        }
        return qualify(namespace, name);
    }

    private static String qualify(String namespace, String name) {
        return namespace.isEmpty() ? "\\" + name : "\\" + namespace + "\\" + name;
    }

    /**
     * @return current namespace without leading backslash, empty for the global namespace
     */
    @RequiredReadAction
    public static String getNamespaceName(PsiElement context) {
        PhpNamespace namespace = PsiTreeUtil.getParentOfType(context, PhpNamespace.class);
        if (namespace == null) {
            return "";
        }
        String name = namespace.getName();
        if (name == null) {
            return "";
        }
        return name.startsWith("\\") ? name.substring(1) : name;
    }

    /**
     * @return lower-cased imported short name or alias to the imported FQN
     */
    @RequiredReadAction
    public static Map<String, String> getClassImports(PsiElement context) {
        PsiElement scope = PsiTreeUtil.getParentOfType(context, PhpNamespace.class);
        if (scope == null) {
            scope = context.getContainingFile();
        }
        Map<String, String> imports = new HashMap<>();
        if (scope == null) {
            return imports;
        }
        for (PsiElement group : scope.getChildren()) {
            if (group.getNode().getElementType() != PhpElementTypes.GROUP_STATEMENT) {
                continue;
            }
            for (PsiElement statement : group.getChildren()) {
                if (statement.getNode().getElementType() != PhpElementTypes.USE_LIST) {
                    continue;
                }
                for (PhpUse use : PsiTreeUtil.getChildrenOfTypeAsList(statement, PhpUse.class)) {
                    String fqn = use.getFQN();
                    if (fqn == null || !use.isClassImport()) {
                        continue;
                    }
                    String alias = use.getAliasName();
                    String key = alias != null ? alias : fqn.substring(fqn.lastIndexOf('\\') + 1);
                    imports.putIfAbsent(key.toLowerCase(Locale.ROOT), fqn);
                }
            }
        }
        return imports;
    }

    /**
     * Source text of a name without whitespace and comments
     */
    @RequiredReadAction
    public static String nameText(PsiElement name) {
        StringBuilder builder = new StringBuilder();
        appendNameText(name, builder);
        return builder.toString();
    }

    private static void appendNameText(PsiElement element, StringBuilder builder) {
        PsiElement child = element.getFirstChild();
        if (child == null) {
            if (!(element instanceof PsiWhiteSpace) && !(element instanceof PsiComment)) {
                builder.append(element.getText());
            }
            return;
        }
        for (; child != null; child = child.getNextSibling()) {
            appendNameText(child, builder);
        }
    }
}
