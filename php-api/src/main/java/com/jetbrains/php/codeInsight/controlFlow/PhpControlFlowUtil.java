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

import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;

/**
 * @author VISTALL
 * @since 2026-10-26
 */
public final class PhpControlFlowUtil
{
	private PhpControlFlowUtil()
	{
	}

	/**
	 * Visits every instruction of the flow in order, until the processor returns false
	 *
	 * @return false when the processor stopped the walk
	 */
	public static boolean processFlow(PhpControlFlow controlFlow, PhpInstructionProcessor processor)
	{
		for(PhpInstruction instruction : controlFlow.getInstructions())
		{
			if(!instruction.process(processor))
			{
				return false;
			}
		}
		return true;
	}
}
