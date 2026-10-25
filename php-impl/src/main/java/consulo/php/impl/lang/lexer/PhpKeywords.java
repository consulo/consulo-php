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

package consulo.php.impl.lang.lexer;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import com.jetbrains.php.config.PhpLanguageLevel;
import consulo.php.lang.lexer.PhpTokenTypes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Reserved words of PHP. Keywords are case-insensitive, so the lexer matches a label and looks it up here.
 * Contextual words ({@code enum}, {@code readonly}, {@code from}, ...) are not listed - they stay identifiers and the parser remaps them.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpKeywords {
    private record Keyword(IElementType type, PhpLanguageLevel since) {
    }

    private static final Map<String, Keyword> KEYWORDS = new HashMap<>();

    static {
        PhpLanguageLevel any = PhpLanguageLevel.PHP_5_0;

        // oop
        add("class", PhpTokenTypes.kwCLASS, any);
        add("interface", PhpTokenTypes.INTERFACE_KEYWORD, any);
        add("extends", PhpTokenTypes.kwEXTENDS, any);
        add("implements", PhpTokenTypes.kwIMPLEMENTS, any);
        add("var", PhpTokenTypes.kwVAR, any);
        add("const", PhpTokenTypes.kwCONST, any);
        add("public", PhpTokenTypes.PUBLIC_KEYWORD, any);
        add("protected", PhpTokenTypes.PROTECTED_KEYWORD, any);
        add("private", PhpTokenTypes.PRIVATE_KEYWORD, any);
        add("static", PhpTokenTypes.STATIC_KEYWORD, any);
        add("final", PhpTokenTypes.FINAL_KEYWORD, any);
        add("abstract", PhpTokenTypes.ABSTRACT_KEYWORD, any);
        add("clone", PhpTokenTypes.kwCLONE, any);
        add("new", PhpTokenTypes.kwNEW, any);
        add("instanceof", PhpTokenTypes.kwINSTANCEOF, any);
        add("trait", PhpTokenTypes.TRAIT_KEYWORD, PhpLanguageLevel.PHP_5_4);
        add("insteadof", PhpTokenTypes.INSTEADOF_KEYWORD, PhpLanguageLevel.PHP_5_4);
        add("namespace", PhpTokenTypes.NAMESPACE_KEYWORD, PhpLanguageLevel.PHP_5_3);
        add("use", PhpTokenTypes.USE_KEYWORD, PhpLanguageLevel.PHP_5_3);

        // flow control
        add("if", PhpTokenTypes.kwIF, any);
        add("elseif", PhpTokenTypes.kwELSEIF, any);
        add("else", PhpTokenTypes.kwELSE, any);
        add("do", PhpTokenTypes.kwDO, any);
        add("while", PhpTokenTypes.kwWHILE, any);
        add("break", PhpTokenTypes.kwBREAK, any);
        add("switch", PhpTokenTypes.kwSWITCH, any);
        add("case", PhpTokenTypes.kwCASE, any);
        add("default", PhpTokenTypes.kwDEFAULT, any);
        add("for", PhpTokenTypes.kwFOR, any);
        add("foreach", PhpTokenTypes.kwFOREACH, any);
        add("as", PhpTokenTypes.kwAS, any);
        add("continue", PhpTokenTypes.kwCONTINUE, any);
        add("declare", PhpTokenTypes.kwDECLARE, any);
        add("goto", PhpTokenTypes.kwGOTO, PhpLanguageLevel.PHP_5_3);
        add("match", PhpTokenTypes.MATCH_KEYWORD, PhpLanguageLevel.PHP_8_0);

        add("endif", PhpTokenTypes.kwENDIF, any);
        add("endwhile", PhpTokenTypes.kwENDWHILE, any);
        add("endfor", PhpTokenTypes.kwENDFOR, any);
        add("endforeach", PhpTokenTypes.kwENDFOREACH, any);
        add("endswitch", PhpTokenTypes.kwENDSWITCH, any);
        add("enddeclare", PhpTokenTypes.kwENDDECLARE, any);

        // functions
        add("function", PhpTokenTypes.kwFUNCTION, any);
        add("fn", PhpTokenTypes.FN_KEYWORD, PhpLanguageLevel.PHP_7_4);
        add("global", PhpTokenTypes.kwGLOBAL, any);
        add("return", PhpTokenTypes.kwRETURN, any);
        add("yield", PhpTokenTypes.YIELD_KEYWORD, PhpLanguageLevel.PHP_5_5);
        add("array", PhpTokenTypes.kwARRAY, any);
        add("list", PhpTokenTypes.kwLIST, any);
        add("print", PhpTokenTypes.kwPRINT, any);
        add("echo", PhpTokenTypes.kwECHO, any);
        add("empty", PhpTokenTypes.kwEMPTY, any);
        add("eval", PhpTokenTypes.kwEVAL, any);
        add("die", PhpTokenTypes.kwEXIT, any);
        add("exit", PhpTokenTypes.kwEXIT, any);
        add("isset", PhpTokenTypes.kwISSET, any);
        add("unset", PhpTokenTypes.kwUNSET, any);
        add("__halt_compiler", PhpTokenTypes.HALT_COMPILER_KEYWORD, any);

        // includes
        add("include", PhpTokenTypes.kwINCLUDE, any);
        add("include_once", PhpTokenTypes.kwINCLUDE_ONCE, any);
        add("require", PhpTokenTypes.kwREQUIRE, any);
        add("require_once", PhpTokenTypes.kwREQUIRE_ONCE, any);

        // exceptions
        add("try", PhpTokenTypes.kwTRY, any);
        add("catch", PhpTokenTypes.kwCATCH, any);
        add("throw", PhpTokenTypes.kwTHROW, any);
        add("finally", PhpTokenTypes.FINALLY_KEYWORD, PhpLanguageLevel.PHP_5_5);

        // word operators
        add("and", PhpTokenTypes.opLIT_AND, any);
        add("or", PhpTokenTypes.opLIT_OR, any);
        add("xor", PhpTokenTypes.opLIT_XOR, any);
    }

    /**
     * @return all keyword token types
     */
    public static TokenSet types() {
        return TokenSet.create(KEYWORDS.values().stream().map(Keyword::type).distinct().toArray(IElementType[]::new));
    }

    private static void add(String text, IElementType type, PhpLanguageLevel since) {
        KEYWORDS.put(text, new Keyword(type, since));
    }

    private PhpKeywords() {
    }

    /**
     * @return keyword token for the label, or {@link PhpTokenTypes#IDENTIFIER}
     */
    public static IElementType lookup(CharSequence label, PhpLanguageLevel languageLevel) {
        Keyword keyword = KEYWORDS.get(label.toString().toLowerCase(Locale.ROOT));
        if (keyword == null || !languageLevel.isAtLeast(keyword.since())) {
            return PhpTokenTypes.IDENTIFIER;
        }
        return keyword.type();
    }
}
