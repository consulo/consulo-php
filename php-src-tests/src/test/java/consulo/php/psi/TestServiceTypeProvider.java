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

import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiElement;
import consulo.project.Project;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Container-like provider in the style of the Symfony plugin: {@code $container->get('app.mailer')} is an {@code \App\Mailer}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
@ExtensionImpl
public class TestServiceTypeProvider implements PhpTypeProvider4 {
    static final char KEY = 'Ж';
    static final char SEPARATOR = 'ő';

    @Override
    public char getKey() {
        return KEY;
    }

    @Override
    public PhpType getType(PsiElement element) {
        if (!(element instanceof MethodReference reference) || !"get".equals(reference.getName())) {
            return null;
        }
        PsiElement[] parameters = reference.getParameters();
        if (parameters.length != 1 || !(parameters[0] instanceof StringLiteralExpression id)) {
            return null;
        }
        return PhpType.builder().add("#" + KEY + reference.getSignature() + SEPARATOR + id.getContents()).build();
    }

    @Override
    public PhpType complete(String expression, Project project) {
        String id = expression.substring(expression.lastIndexOf(SEPARATOR) + 1);
        return "app.mailer".equals(id) ? PhpType.builder().add("\\App\\Mailer").build() : null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
        return List.of();
    }
}
