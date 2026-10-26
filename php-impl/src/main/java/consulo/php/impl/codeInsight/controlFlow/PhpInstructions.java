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
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpAccessVariableInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpConstructorCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpReturnInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpThrowInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpYieldInstruction;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.controlFlow.ControlFlowBuilder;
import consulo.language.psi.PsiElement;
import org.jspecify.annotations.Nullable;

/**
 * The instruction kinds of a PHP control flow
 *
 * @author VISTALL
 * @since 2026-10-26
 */
final class PhpInstructions {
    private PhpInstructions() {
    }

    static final class Return extends PhpInstructionImpl implements PhpReturnInstruction {
        private final @Nullable PsiElement myArgument;

        Return(ControlFlowBuilder builder, PsiElement statement, @Nullable PsiElement argument) {
            super(builder, statement);
            myArgument = argument;
        }

        @Override
        public @Nullable PsiElement getArgument() {
            return myArgument;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processReturnInstruction(this);
        }
    }

    static final class Yield extends PhpInstructionImpl implements PhpYieldInstruction {
        private final @Nullable PsiElement myArgument;
        private final boolean myYieldFrom;

        Yield(ControlFlowBuilder builder, PsiElement expression, @Nullable PsiElement argument, boolean yieldFrom) {
            super(builder, expression);
            myArgument = argument;
            myYieldFrom = yieldFrom;
        }

        @Override
        public @Nullable PsiElement getArgument() {
            return myArgument;
        }

        @Override
        public boolean isYieldFrom() {
            return myYieldFrom;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processYieldInstruction(this);
        }
    }

    static final class Throw extends PhpInstructionImpl implements PhpThrowInstruction {
        private final @Nullable PsiElement myArgument;

        Throw(ControlFlowBuilder builder, PsiElement element, @Nullable PsiElement argument) {
            super(builder, element);
            myArgument = argument;
        }

        @Override
        public @Nullable PsiElement getArgument() {
            return myArgument;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processThrowInstruction(this);
        }
    }

    static final class Call extends PhpInstructionImpl implements PhpCallInstruction {
        Call(ControlFlowBuilder builder, FunctionReference reference) {
            super(builder, reference);
        }

        @Override
        public FunctionReference getFunctionReference() {
            return (FunctionReference) myElement;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processPhpCallInstruction(this);
        }
    }

    static final class ConstructorCall extends PhpInstructionImpl implements PhpConstructorCallInstruction {
        ConstructorCall(ControlFlowBuilder builder, NewExpression expression) {
            super(builder, expression);
        }

        @Override
        public NewExpression getNewExpression() {
            return (NewExpression) myElement;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processConstructorCallInstruction(this);
        }
    }

    static final class AccessVariable extends PhpInstructionImpl implements PhpAccessVariableInstruction {
        private final CharSequence myName;
        private final Access myAccess;

        AccessVariable(ControlFlowBuilder builder, PhpPsiElement variable, CharSequence name, Access access) {
            super(builder, variable);
            myName = name;
            myAccess = access;
        }

        @Override
        public CharSequence getVariableName() {
            return myName;
        }

        @Override
        public PhpPsiElement getAnchor() {
            return (PhpPsiElement) myElement;
        }

        @Override
        public Access getAccess() {
            return myAccess;
        }

        @Override
        public boolean process(PhpInstructionProcessor processor) {
            return processor.processAccessVariableInstruction(this);
        }

        @Override
        public String getElementPresentation() {
            return myAccess + " $" + myName;
        }
    }
}
