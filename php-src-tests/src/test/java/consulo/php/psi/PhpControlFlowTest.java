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

import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlowUtil;
import com.jetbrains.php.codeInsight.controlFlow.PhpInstructionProcessor;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpAccessVariableInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpConstructorCallInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpReturnInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpThrowInstruction;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpYieldInstruction;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.language.controlFlow.Instruction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.test.junit.impl.extension.ConsuloProjectLoader;
import consulo.test.junit.impl.extension.InjectingRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Control flow of PHP scopes: instruction kinds, evaluation order, variable access, scopes and edges.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
@ExtendWith(ConsuloProjectLoader.class)
public class PhpControlFlowTest {
    public record Context(PsiFileFactory psiFileFactory) implements InjectingRecord {
    }

    private static PhpFile parse(Context context, String text) {
        return (PhpFile) context.psiFileFactory().createFileFromText("a.php", PhpLanguage.INSTANCE, PhpLanguageLevel.HIGHEST, text);
    }

    private static Function function(PhpFile file, String name) {
        return PsiTreeUtil.findChildrenOfType(file, Function.class).stream()
            .filter(function -> name.equals(function.getName())).findFirst().orElseThrow();
    }

    /**
     * Readable form of the typed instructions: "read $a", "write $a", "call foo", "new Bar", "return $a", "yield 1", "throw ..."
     */
    private static List<String> describe(PhpControlFlow controlFlow) {
        List<String> result = new ArrayList<>();
        PhpControlFlowUtil.processFlow(controlFlow, new PhpInstructionProcessor() {
            @Override
            public boolean processAccessVariableInstruction(PhpAccessVariableInstruction instruction) {
                result.add(instruction.getAccess().name().toLowerCase() + " $" + instruction.getVariableName());
                return true;
            }

            @Override
            public boolean processPhpCallInstruction(PhpCallInstruction instruction) {
                result.add("call " + instruction.getFunctionReference().getName());
                return true;
            }

            @Override
            public boolean processConstructorCallInstruction(PhpConstructorCallInstruction instruction) {
                result.add("new " + instruction.getNewExpression().getText());
                return true;
            }

            @Override
            public boolean processReturnInstruction(PhpReturnInstruction instruction) {
                result.add("return " + (instruction.getArgument() == null ? "" : instruction.getArgument().getText()));
                return true;
            }

            @Override
            public boolean processYieldInstruction(PhpYieldInstruction instruction) {
                result.add((instruction.isYieldFrom() ? "yield from " : "yield ") + instruction.getArgument().getText());
                return true;
            }

            @Override
            public boolean processThrowInstruction(PhpThrowInstruction instruction) {
                result.add("throw " + instruction.getArgument().getText());
                return true;
            }
        });
        return result;
    }

    private static PhpInstruction find(PhpControlFlow controlFlow, String text) {
        for (PhpInstruction instruction : controlFlow.getInstructions()) {
            PsiElement anchor = instruction.getAnchor();
            if (anchor != null && anchor.getText().equals(text)) {
                return instruction;
            }
        }
        throw new AssertionError("no instruction for " + text + "\n" + controlFlow);
    }

    private static PhpInstruction exit(PhpControlFlow controlFlow) {
        PhpInstruction[] instructions = controlFlow.getInstructions();
        return instructions[instructions.length - 1];
    }

    private static boolean reaches(Instruction from, Instruction to) {
        List<Instruction> queue = new ArrayList<>(List.of(from));
        List<Instruction> seen = new ArrayList<>();
        while (!queue.isEmpty()) {
            Instruction next = queue.remove(queue.size() - 1);
            if (next == to) {
                return true;
            }
            if (!seen.contains(next)) {
                seen.add(next);
                queue.addAll(next.allSucc());
            }
        }
        return false;
    }

    @Test
    public void testInstructionsInEvaluationOrder(Context context) {
        PhpFile file = parse(context, """
            <?php
            function f($container) {
                $a = foo($b, new Bar());
                $container->get('x');
                if ($a) {
                    return $a;
                }
                yield 1;
                yield from $list;
                throw new Exception();
            }
            """);
        PhpControlFlow controlFlow = function(file, "f").getControlFlow();

        Assertions.assertEquals(List.of(
            "read $b", "new new Bar()", "call foo", "write $a",
            "read $container", "call get",
            "read $a", "read $a", "return $a",
            "yield 1", "read $list", "yield from $list",
            "new new Exception()", "throw new Exception()"
        ), describe(controlFlow));

        // entry and exit points have no anchor
        Assertions.assertNull(controlFlow.getEntryPoint().getAnchor());
        Assertions.assertNull(exit(controlFlow).getAnchor());
        // method calls are function references, like in PhpStorm
        Assertions.assertInstanceOf(MethodReference.class, ((PhpCallInstruction) find(controlFlow, "$container->get('x')")).getFunctionReference());
    }

    @Test
    public void testAccessKinds(Context context) {
        PhpFile file = parse(context, """
            <?php
            function f($list, $arr) {
                $x = &$y;
                foreach ($list as $k => &$v) {}
                [$p, 'key' => $q] = $arr;
                list($l1, $l2) = $arr;
                $s .= 'x';
                $arr['k'] = $i;
                global $g;
            }
            """);

        Assertions.assertEquals(List.of(
            "read_ref $y", "write_ref $x",
            "read $list", "write $k", "write_ref $v",
            "read $arr", "write $p", "write $q",
            "read $arr", "write $l1", "write $l2",
            "read $s", "write $s",
            "read $i", "write $arr",
            "write $g"
        ), describe(function(file, "f").getControlFlow()));
    }

    @Test
    public void testScopes(Context context) {
        PhpFile file = parse(context, """
            <?php
            namespace App;

            $top = 1;

            function outer($a) {
                $closure = function () use ($a, &$b) {
                    return inner($c);
                };
                $arrow = fn($x) => $x + $a;
                function nested() { $hidden = 1; }
                return $closure;
            }
            """);
        Function outer = function(file, "outer");
        Assertions.assertEquals(List.of("read $a", "read_ref $b", "write $closure", "write $arrow", "read $closure", "return $closure"),
            describe(outer.getControlFlow()));

        Function closure = PsiTreeUtil.findChildrenOfType(outer, Function.class).stream()
            .filter(function -> function.getText().startsWith("function ()")).findFirst().orElseThrow();
        Assertions.assertEquals(List.of("read $c", "call inner", "return inner($c)"), describe(closure.getControlFlow()));

        Function arrow = PsiTreeUtil.findChildrenOfType(outer, Function.class).stream()
            .filter(function -> function.getText().startsWith("fn")).findFirst().orElseThrow();
        Assertions.assertEquals(List.of("read $x", "read $a"), describe(arrow.getControlFlow()));

        // the namespace has the top level statements, the file only what is outside of namespaces
        PhpNamespace namespace = PsiTreeUtil.findChildOfType(file, PhpNamespace.class);
        Assertions.assertEquals(List.of("write $top"), describe(namespace.getControlFlow()));
        Assertions.assertEquals(List.of(), describe(file.getControlFlow()));
    }

    @Test
    public void testEdges(Context context) {
        PhpFile file = parse(context, """
            <?php
            function f($c, $list) {
                if ($c) {
                    return early();
                }
                while ($c) {
                    if ($c) {
                        break;
                    }
                    if ($list) {
                        continue;
                    }
                    loop();
                }
                after();
            }
            """);
        PhpControlFlow controlFlow = function(file, "f").getControlFlow();
        PhpInstruction returnInstruction = find(controlFlow, "return early();");
        PhpInstruction loopCall = find(controlFlow, "loop()");
        PhpInstruction afterCall = find(controlFlow, "after()");
        PhpInstruction breakInstruction = find(controlFlow, "break;");
        PhpInstruction continueInstruction = find(controlFlow, "continue;");

        // return goes to the exit point only
        Assertions.assertEquals(List.of(exit(controlFlow)), List.copyOf(returnInstruction.allSucc()));
        Assertions.assertFalse(reaches(returnInstruction, afterCall));
        // the code after the if is reached from the entry
        Assertions.assertTrue(reaches(controlFlow.getEntryPoint(), afterCall));
        // the loop body goes back to the condition, and leaving the loop reaches after()
        Assertions.assertTrue(reaches(loopCall, loopCall));
        Assertions.assertTrue(reaches(loopCall, afterCall));
        // break leaves the loop, continue goes back to its head
        Assertions.assertTrue(reaches(breakInstruction, afterCall));
        Assertions.assertFalse(reaches(breakInstruction, loopCall));
        Assertions.assertTrue(reaches(continueInstruction, loopCall));
        Assertions.assertTrue(reaches(afterCall, exit(controlFlow)));
    }

    @Test
    public void testProcessFlowStops(Context context) {
        PhpFile file = parse(context, "<?php\nfunction f() { a(); b(); c(); }\n");
        List<String> calls = new ArrayList<>();
        boolean completed = PhpControlFlowUtil.processFlow(function(file, "f").getControlFlow(), new PhpInstructionProcessor() {
            @Override
            public boolean processPhpCallInstruction(PhpCallInstruction instruction) {
                calls.add(instruction.getFunctionReference().getName());
                return calls.size() < 2;
            }
        });
        Assertions.assertFalse(completed);
        Assertions.assertEquals(List.of("a", "b"), calls);
    }
}
