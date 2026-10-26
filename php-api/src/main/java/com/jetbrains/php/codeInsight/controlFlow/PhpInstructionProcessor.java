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

import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpAccessVariableInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpConstructorCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpReturnInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpThrowInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpYieldInstruction;

/**
 * Visitor of control flow instructions, see {@link PhpControlFlowUtil#processFlow(PhpControlFlow, PhpInstructionProcessor)}.
 * Every method returns whether to continue; the specific ones fall back to {@link #processInstruction(PhpInstruction)}.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public class PhpInstructionProcessor
{
	public boolean processInstruction(PhpInstruction instruction)
	{
		return true;
	}

	public boolean processAccessVariableInstruction(PhpAccessVariableInstruction instruction)
	{
		return processInstruction(instruction);
	}

	public boolean processPhpCallInstruction(PhpCallInstruction instruction)
	{
		return processInstruction(instruction);
	}

	public boolean processConstructorCallInstruction(PhpConstructorCallInstruction instruction)
	{
		return processInstruction(instruction);
	}

	public boolean processReturnInstruction(PhpReturnInstruction instruction)
	{
		return processInstruction(instruction);
	}

	public boolean processYieldInstruction(PhpYieldInstruction instruction)
	{
		return processInstruction(instruction);
	}

	public boolean processThrowInstruction(PhpThrowInstruction instruction)
	{
		return processInstruction(instruction);
	}
}
