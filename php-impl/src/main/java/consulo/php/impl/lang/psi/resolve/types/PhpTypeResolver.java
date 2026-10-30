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

package consulo.php.impl.lang.psi.resolve.types;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpEnumCase;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import consulo.annotation.access.RequiredReadAction;
import consulo.project.Project;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Turns the signatures of local types into class names, with the help of the index and type providers.
 *
 * @author VISTALL
 * @since 2026-10-25
 * @see PhpTypeInference
 */
public final class PhpTypeResolver {
    private static final int MAX_DEPTH = 15;

    private static final Set<String> THIS_TYPES = Set.of("static", "\\static", "$this", "\\$this", "self", "\\self");

    private final PhpIndex myIndex;
    private final Project myProject;

    public PhpTypeResolver(PhpIndex index, Project project) {
        myIndex = index;
        myProject = project;
    }

    @RequiredReadAction
    public PhpType complete(PhpType type, @Nullable Set<String> visited) {
        return complete(type, visited == null ? new HashSet<>() : visited, 0);
    }

    @RequiredReadAction
    private PhpType complete(PhpType type, Set<String> visited, int depth) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (String part : type.getTypes()) {
            if (!part.startsWith("#")) {
                builder.add(part);
                continue;
            }
            if (depth > MAX_DEPTH || !visited.add(part)) {
                continue;
            }
            try {
                builder.add(completeSignature(part, visited, depth + 1));
            }
            finally {
                visited.remove(part);
            }
        }
        return builder.build();
    }

    /**
     * Replaces {@code static}, {@code self} and {@code $this} with the class the member was accessed through
     */
    @RequiredReadAction
    public PhpType completeThis(PhpType type, @Nullable String thisClass, Set<String> visited) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (String part : type.getTypes()) {
            if (thisClass != null && THIS_TYPES.contains(part.toLowerCase(Locale.ROOT))) {
                builder.add(thisClass);
            }
            else {
                builder.add(part);
            }
        }
        return complete(builder.build(), visited);
    }

    @RequiredReadAction
    private PhpType completeSignature(String signature, Set<String> visited, int depth) {
        if (signature.length() < 2) {
            return PhpType.EMPTY;
        }
        char key = signature.charAt(1);
        String body = signature.substring(2);
        switch (key) {
            case 'C':
                return PhpType.builder().add(body).build();
            case 'E': {
                // element of an array: 'Foo[]' -> 'Foo'
                PhpType.PhpTypeBuilder builder = PhpType.builder();
                for (String arrayType : complete(PhpType.builder().add(body).build(), visited, depth).getTypes()) {
                    if (arrayType.endsWith("[]")) {
                        builder.add(arrayType.substring(0, arrayType.length() - 2));
                    }
                }
                return builder.build();
            }
            case 'M':
            case 'P':
            case 'K': {
                PhpType.PhpTypeBuilder builder = PhpType.builder();
                int dot = body.lastIndexOf('.');
                if (dot < 0) {
                    return PhpType.EMPTY;
                }
                String memberName = body.substring(dot + 1);
                for (PhpClass phpClass : getClasses(body.substring(0, dot), visited, depth)) {
                    PhpNamedElement member = findMember(phpClass, key, memberName);
                    if (member != null) {
                        // late static binding: 'static' is the class the member was accessed through
                        builder.add(completeThis(typeOf(member), phpClass.getFQN(), visited));
                    }
                }
                return builder.build();
            }
            case 'F': {
                PhpType.PhpTypeBuilder builder = PhpType.builder();
                for (Function function : myIndex.getFunctionsByFQN(body)) {
                    builder.add(complete(typeOf(function), visited, depth));
                }
                return builder.build();
            }
            case 'D': {
                PhpType.PhpTypeBuilder builder = PhpType.builder();
                for (Constant constant : myIndex.getConstantsByFQN(body)) {
                    builder.add(complete(typeOf(constant), visited, depth));
                }
                return builder.build();
            }
            default:
                return completeProviderSignature(key, signature, visited, depth);
        }
    }

    @RequiredReadAction
    private PhpType completeProviderSignature(char key, String signature, Set<String> visited, int depth) {
        PhpTypeProvider4 provider = findProvider(key);
        if (provider == null) {
            return PhpType.EMPTY;
        }
        // like PhpStorm, the provider gets the signature without its "#" + key prefix
        String expression = signature.substring(2);
        PhpType completed = provider.complete(expression, myProject);
        if (completed != null) {
            return complete(completed, visited, depth);
        }
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        Collection<? extends PhpNamedElement> elements = provider.getBySignature(expression, visited, depth, myProject);
        if (elements != null) {
            for (PhpNamedElement element : elements) {
                builder.add(complete(typeOf(element), visited, depth));
            }
        }
        return builder.build();
    }

    private static @Nullable PhpTypeProvider4 findProvider(char key) {
        for (PhpTypeProvider4 provider : PhpTypeProvider4.EP_NAME.getExtensionList()) {
            if (provider.getKey() == key) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Classes of a qualifier: a class name, or a signature to complete first
     */
    @RequiredReadAction
    private Collection<PhpClass> getClasses(String qualifier, Set<String> visited, int depth) {
        Set<String> classNames = new LinkedHashSet<>();
        if (qualifier.startsWith("#")) {
            for (String type : complete(PhpType.builder().add(qualifier).build(), visited, depth).getTypes()) {
                classNames.add(type);
            }
        }
        else {
            classNames.add(qualifier);
        }
        List<PhpClass> result = new ArrayList<>();
        for (String className : classNames) {
            if (!className.startsWith("#") && !PhpType.isPrimitiveType(className)) {
                result.addAll(myIndex.getAnyByFQN(className));
            }
        }
        return result;
    }

    private static @Nullable PhpNamedElement findMember(PhpClass phpClass, char key, String name) {
        if (key == 'M') {
            return phpClass.findMethodByName(name);
        }
        if (key == 'K') {
            if (phpClass.isEnum()) {
                for (PhpEnumCase enumCase : phpClass.getEnumCases()) {
                    if (name.equals(enumCase.getName())) {
                        return phpClass;
                    }
                }
            }
            return phpClass.findFieldByName(name, true);
        }
        return phpClass.findFieldByName(name, false);
    }

    private static PhpType typeOf(PhpNamedElement element) {
        if (element instanceof PhpClass phpClass) {
            String fqn = phpClass.getFQN();
            return fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }
        return element instanceof PhpTypedElement typed ? typed.getType() : PhpType.EMPTY;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Elements a signature stands for; several signatures can be joined with {@code |}
     */
    @RequiredReadAction
    public Collection<? extends PhpNamedElement> getBySignature(String signatures, @Nullable Set<String> visited, int depth) {
        Set<String> guard = visited == null ? new HashSet<>() : visited;
        List<PhpNamedElement> result = new ArrayList<>();
        for (String signature : signatures.split("\\|")) {
            if (signature.isEmpty() || depth > MAX_DEPTH || !guard.add(signature)) {
                continue;
            }
            try {
                result.addAll(getBySingleSignature(signature, guard, depth));
            }
            finally {
                guard.remove(signature);
            }
        }
        return result;
    }

    @RequiredReadAction
    private Collection<? extends PhpNamedElement> getBySingleSignature(String signature, Set<String> visited, int depth) {
        if (!signature.startsWith("#") || signature.length() < 2) {
            return myIndex.getAnyByFQN(signature);
        }
        char key = signature.charAt(1);
        String body = signature.substring(2);
        switch (key) {
            case 'C':
                return myIndex.getAnyByFQN(body);
            case 'M':
            case 'P':
            case 'K': {
                int dot = body.lastIndexOf('.');
                if (dot < 0) {
                    return List.of();
                }
                List<PhpNamedElement> result = new ArrayList<>();
                for (PhpClass phpClass : getClasses(body.substring(0, dot), visited, depth + 1)) {
                    PhpNamedElement member = findMember(phpClass, key, body.substring(dot + 1));
                    if (member != null) {
                        result.add(member);
                    }
                }
                return result;
            }
            case 'F':
                return myIndex.getFunctionsByFQN(body);
            case 'D':
                return myIndex.getConstantsByFQN(body);
            default: {
                PhpTypeProvider4 provider = findProvider(key);
                if (provider == null) {
                    return List.of();
                }
                Collection<? extends PhpNamedElement> elements = provider.getBySignature(body, visited, depth + 1, myProject);
                return elements == null ? List.of() : elements;
            }
        }
    }
}
