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

package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.PhpAttribute;
import com.jetbrains.php.lang.psi.elements.PhpAttributesList;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpAttributesListImpl extends PhpElementImpl implements PhpAttributesList {
    public PhpAttributesListImpl(ASTNode node) {
        super(node);
    }

    @Override
    public void accept(PhpElementVisitor visitor) {
        visitor.visitPhpElement(this);
    }

    @Override
    public Collection<PhpAttribute> getAttributes() {
        return Arrays.asList(findChildrenByClass(PhpAttribute.class));
    }
}
