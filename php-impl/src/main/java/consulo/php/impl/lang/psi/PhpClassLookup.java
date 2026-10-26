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
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.util.CachedValueProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.LanguageCachedValueUtil;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import consulo.php.lang.lexer.PhpTokenTypes;
import org.jspecify.annotations.Nullable;

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
    public static @Nullable Collection<PhpClass> findQualifierClasses(PsiElement reference) {
        PsiElement qualifier = reference.getFirstChild();
        if (qualifier == null || qualifier.getNode().getElementType() == PhpTokenTypes.IDENTIFIER) {
            return null;
        }
        PhpType type;
        if (qualifier instanceof ClassReference classReference) {
            String fqn = PhpNameResolver.resolveClassFqn(classReference);
            type = fqn == null ? PhpType.EMPTY
                : PhpType.builder().add(fqn).build();
        }
        else {
            type = PhpTypeInference.getType(qualifier);
        }
        PhpType global = PhpIndex.getInstance(reference.getProject()).completeType(reference.getProject(), type, null);
        List<PhpClass> result = new ArrayList<>();
        for (String className : global.getTypes()) {
            if (!className.startsWith("#") && !PhpType.isPrimitiveType(className)) {
                result.addAll(findClasses(reference, className));
            }
        }
        return result;
    }
}
