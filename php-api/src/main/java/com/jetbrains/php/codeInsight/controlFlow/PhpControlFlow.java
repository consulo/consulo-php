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

package com.jetbrains.php.codeInsight.controlFlow;

import com.jetbrains.php.codeInsight.PhpScopeHolder;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import consulo.language.controlFlow.ControlFlow;

/**
 * Control flow of a {@link PhpScopeHolder}: instructions in evaluation order, the first one is the entry
 * point, the last one the exit point. Nested functions, closures and classes have their own control flow.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public interface PhpControlFlow extends ControlFlow
{
	@Override
	PhpInstruction[] getInstructions();

	default PhpInstruction getEntryPoint()
	{
		return getInstructions()[0];
	}
}
