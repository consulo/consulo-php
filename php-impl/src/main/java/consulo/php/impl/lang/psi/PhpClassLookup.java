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

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.util.CachedValueProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.LanguageCachedValueUtil;
import consulo.language.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classes by FQN: the file of the context first, then the index.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpClassLookup {
    private PhpClassLookup() {
    }

    @RequiredReadAction
    public static Collection<PhpClass> findClasses(PsiElement context, String fqn) {
        String normalized = (fqn.startsWith("\\") ? fqn : "\\" + fqn).toLowerCase(Locale.ROOT);
        PsiFile file = context.getContainingFile();
        if (file != null) {
            List<PhpClass> local = getFileClasses(file).get(normalized);
            if (local != null) {
                return local;
            }
        }
        return PhpIndex.getInstance(context.getProject()).getAnyByFQN(fqn);
    }

    private static Map<String, List<PhpClass>> getFileClasses(PsiFile file) {
        return LanguageCachedValueUtil.getCachedValue(file, () -> {
            Map<String, List<PhpClass>> classes = new HashMap<>();
            for (PhpClass phpClass : PsiTreeUtil.findChildrenOfType(file, PhpClass.class)) {
                String classFqn = phpClass.getFQN();
                if (classFqn != null) {
                    classes.computeIfAbsent(classFqn.toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(phpClass);
                }
            }
            return CachedValueProvider.Result.create(classes, file);
        });
    }

    /**
     * Classes the qualifier of {@code $a->x}, {@code Foo::x} evaluates to, or {@code null} if there is no qualifier
     */
    @RequiredReadAction
    public static @org.jspecify.annotations.Nullable Collection<PhpClass> findQualifierClasses(PsiElement reference) {
        PsiElement qualifier = reference.getFirstChild();
        if (qualifier == null || qualifier.getNode().getElementType() == consulo.php.lang.lexer.PhpTokenTypes.IDENTIFIER) {
            return null;
        }
        com.jetbrains.php.lang.psi.resolve.types.PhpType type;
        if (qualifier instanceof com.jetbrains.php.lang.psi.elements.ClassReference classReference) {
            String fqn = PhpNameResolver.resolveClassFqn(classReference);
            type = fqn == null ? com.jetbrains.php.lang.psi.resolve.types.PhpType.EMPTY
                : com.jetbrains.php.lang.psi.resolve.types.PhpType.builder().add(fqn).build();
        }
        else {
            type = consulo.php.impl.lang.psi.resolve.types.PhpTypeInference.getType(qualifier);
        }
        com.jetbrains.php.lang.psi.resolve.types.PhpType global = PhpIndex.getInstance(reference.getProject()).completeType(reference.getProject(), type, null);
        List<PhpClass> result = new ArrayList<>();
        for (String className : global.getTypes()) {
            if (!className.startsWith("#") && !com.jetbrains.php.lang.psi.resolve.types.PhpType.isPrimitiveType(className)) {
                result.addAll(findClasses(reference, className));
            }
        }
        return result;
    }
}
