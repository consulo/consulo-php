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
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.parser.PhpElementTypes;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Supertypes of a class read from its declaration - index-free, used to build stubs.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpClassSupertypes {
    private PhpClassSupertypes() {
    }

    /**
     * @return FQN of the {@code extends} class, {@code null} for interfaces, traits, enums and root classes
     */
    @RequiredReadAction
    public static @Nullable String superClassFqn(PhpClass phpClass) {
        if (phpClass.isInterface()) {
            return null;
        }
        List<String> extended = listFqns(phpClass, PhpElementTypes.EXTENDS_LIST);
        return extended.isEmpty() ? null : extended.get(0);
    }

    /**
     * @return implemented interfaces, or the extended interfaces of an interface
     */
    @RequiredReadAction
    public static List<String> interfaceFqns(PhpClass phpClass) {
        return listFqns(phpClass, phpClass.isInterface() ? PhpElementTypes.EXTENDS_LIST : PhpElementTypes.IMPLEMENTS_LIST);
    }

    @RequiredReadAction
    public static List<String> traitFqns(PhpClass phpClass) {
        List<String> result = new ArrayList<>();
        for (PsiElement child : phpClass.getChildren()) {
            if (child.getNode().getElementType() != PhpElementTypes.TRAIT_USE) {
                continue;
            }
            for (ClassReference reference : PsiTreeUtil.getChildrenOfTypeAsList(child, ClassReference.class)) {
                String fqn = PhpNameResolver.resolveClassFqn(reference);
                if (fqn != null) {
                    result.add(fqn);
                }
            }
        }
        return result;
    }

    @RequiredReadAction
    private static List<String> listFqns(PhpClass phpClass, IElementType listType) {
        List<String> result = new ArrayList<>();
        for (PsiElement child : phpClass.getChildren()) {
            if (child.getNode().getElementType() != listType) {
                continue;
            }
            for (ClassReference reference : PsiTreeUtil.getChildrenOfTypeAsList(child, ClassReference.class)) {
                String fqn = PhpNameResolver.resolveClassFqn(reference);
                if (fqn != null) {
                    result.add(fqn);
                }
            }
        }
        return result;
    }
}
