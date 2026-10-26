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

package consulo.php.impl.codeInsight.controlFlow;

import com.jetbrains.php.codeInsight.controlFlow.PhpInstructionProcessor;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import consulo.language.controlFlow.ControlFlowBuilder;
import consulo.language.controlFlow.base.InstructionImpl;
import consulo.language.psi.PsiElement;
import org.jspecify.annotations.Nullable;

/**
 * Instruction without a kind of its own: entry and exit points, loop heads, case entries
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public class PhpInstructionImpl extends InstructionImpl implements PhpInstruction {
    public PhpInstructionImpl(ControlFlowBuilder builder, @Nullable PsiElement element) {
        super(builder, element);
    }

    @Override
    public boolean process(PhpInstructionProcessor processor) {
        return processor.processInstruction(this);
    }
}
