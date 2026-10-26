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

package com.jetbrains.php.lang.psi.elements.impl;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.impl.PhpFieldImpl;
import jakarta.annotation.Nonnull;

/**
 * Class constant: {@code const A = 1;} in a class, interface, trait or enum. A {@link Field}
 * with {@link #isConstant()}, like in PhpStorm.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public class ClassConstImpl extends PhpFieldImpl
{
	public ClassConstImpl(ASTNode node)
	{
		super(node);
	}

	public ClassConstImpl(@Nonnull PhpFieldStub stub)
	{
		super(stub);
	}
}
