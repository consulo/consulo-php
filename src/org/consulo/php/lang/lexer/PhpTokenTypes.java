package org.consulo.php.lang.lexer;

import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.psi.PhpElementType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public interface PhpTokenTypes extends PhpDocElementTypes, TokenType
{

	IElementType PHP_OPENING_TAG = new PhpElementType("php opening tag"); // <?php or <?
	IElementType PHP_ECHO_OPENING_TAG = new PhpElementType("php echo opening tag"); // <?=
	IElementType PHP_CLOSING_TAG = new PhpElementType("php closing tag"); // ?>
	IElementType UNKNOWN_SYMBOL = new PhpElementType("dunno what's that");
	IElementType SYNTAX_ERROR = new PhpElementType("syntax error");

	IElementType WHITE_SPACE = new PhpElementType("some whitespace");
	IElementType HTML = new PhpElementType("html");

	IElementType kwIF = new PhpElementType("if");
	IElementType kwELSEIF = new PhpElementType("elseif");
	IElementType kwELSE = new PhpElementType("else");
	IElementType kwFOR = new PhpElementType("for");
	IElementType kwFOREACH = new PhpElementType("foreach keyword");
	IElementType kwWHILE = new PhpElementType("while");
	IElementType kwDO = new PhpElementType("do");
	IElementType kwSWITCH = new PhpElementType("switch");
	IElementType kwCASE = new PhpElementType("case");
	IElementType kwDEFAULT = new PhpElementType("default keyword");
	IElementType kwTRY = new PhpElementType("try");
	IElementType kwCATCH = new PhpElementType("catch");
	IElementType kwDECLARE = new PhpElementType("declare");
	IElementType kwBREAK = new PhpElementType("break");
	IElementType kwENDIF = new PhpElementType("endif");
	IElementType kwENDFOR = new PhpElementType("endfor");
	IElementType kwENDFOREACH = new PhpElementType("endforeach");
	IElementType kwENDWHILE = new PhpElementType("endwhile");
	IElementType kwENDSWITCH = new PhpElementType("endswitch");
	IElementType kwENDDECLARE = new PhpElementType("enddeclare");

	IElementType kwEXIT = new PhpElementType("exit");
	IElementType NAMESPACE_KEYWORD = new PhpElementType("NAMESPACE_KEYWORD");
	IElementType USE_KEYWORD = new PhpElementType("USE_KEYWORD");
	IElementType PRIVATE_KEYWORD = new PhpElementType("private");
	IElementType kwFUNCTION = new PhpElementType("function");
	IElementType kwNEW = new PhpElementType("new");
	IElementType kwINSTANCEOF = new PhpElementType("instanceof");
	IElementType kwCONST = new PhpElementType("const");
	IElementType kwLIST = new PhpElementType("list");
	IElementType kwIMPLEMENTS = new PhpElementType("implements");
	IElementType kwEVAL = new PhpElementType("eval");
	IElementType FINAL_KEYWORD = new PhpElementType("final");
	IElementType kwAS = new PhpElementType("as");
	IElementType kwTHROW = new PhpElementType("throw");
	IElementType kwEXCEPTION = new PhpElementType("exception");
	IElementType kwINCLUDE_ONCE = new PhpElementType("include once");
	IElementType kwCLASS = new PhpElementType("class");
	IElementType ABSTRACT_KEYWORD = new PhpElementType("ABSTRACT_KEYWORD");
	IElementType INTERFACE_KEYWORD = new PhpElementType("INTERFACE_KEYWORD");
	IElementType TRAIT_KEYWORD = new PhpElementType("TRAIT_KEYWORD");
	IElementType PUBLIC_KEYWORD = new PhpElementType("PUBLIC_KEYWORD");
	IElementType STATIC_KEYWORD = new PhpElementType("STATIC_KEYWORD");
	IElementType kwCLONE = new PhpElementType("clone keyword");
	IElementType kwISSET = new PhpElementType("isset keyword");
	IElementType kwEMPTY = new PhpElementType("empty keyword");
	IElementType kwRETURN = new PhpElementType("return");
	IElementType kwVAR = new PhpElementType("var");
	IElementType kwPHP_USER_FILTER = new PhpElementType("php user filter");
	IElementType kwCONTINUE = new PhpElementType("continue");
	IElementType kwDIE = new PhpElementType("die");
	IElementType PROTECTED_KEYWORD = new PhpElementType("PROTECTED_KEYWORD");
	IElementType kwPRINT = new PhpElementType("print");
	IElementType kwECHO = new PhpElementType("echo");
	IElementType kwINCLUDE = new PhpElementType("include");
	IElementType kwGLOBAL = new PhpElementType("global");
	IElementType kwEXTENDS = new PhpElementType("extends");
	IElementType kwUNSET = new PhpElementType("unset");
	IElementType kwREQUIRE_ONCE = new PhpElementType("require once");
	IElementType kwARRAY = new PhpElementType("array");
	IElementType kwREQUIRE = new PhpElementType("require");

	IElementType CONST_FUNCTION = new PhpElementType("__FUNCTION__ const");
	IElementType CONST_LINE = new PhpElementType("__LINE__ const");
	IElementType CONST_CLASS = new PhpElementType("__CLASS__ const");
	IElementType CONST_METHOD = new PhpElementType("__METHOD__ const");
	IElementType CONST_FILE = new PhpElementType("__FILE__ const");

	IElementType LINE_COMMENT = new PhpElementType("line comment");
	//	IElementType DOC_COMMENT = new PhpElementType("doc comment");
	IElementType C_STYLE_COMMENT = new PhpElementType("C style comment");
	IElementType VARIABLE = new PhpElementType("variable");
	IElementType VARIABLE_NAME = new PhpElementType("variable name"); // ???
	IElementType VARIABLE_OFFSET_NUMBER = new PhpElementType("array index"); // ???
	IElementType DOLLAR_LBRACE = new PhpElementType("${"); // ???
	IElementType IDENTIFIER = new PhpElementType("identifier");
	IElementType PREDEFINED_IDENTIFIER = new PhpElementType("identifier");
	IElementType ARROW = new PhpElementType("arrow");
	IElementType SCOPE_RESOLUTION = new PhpElementType("scope resolution");
	IElementType FLOAT_LITERAL = new PhpElementType("float");
	IElementType INTEGER_LITERAL = new PhpElementType("integer");
	IElementType STRING_LITERAL = new PhpElementType("string");
	IElementType STRING_LITERAL_SINGLE_QUOTE = new PhpElementType("single quoted string");
	IElementType EXEC_COMMAND = new PhpElementType("exec command");
	IElementType ESCAPE_SEQUENCE = new PhpElementType("escape sequence");
	IElementType HEREDOC_START = new PhpElementType("heredoc start");
	IElementType HEREDOC_CONTENTS = new PhpElementType("heredoc");
	IElementType HEREDOC_END = new PhpElementType("heredoc end");


	IElementType chDOUBLE_QUOTE = new PhpElementType("double quote");
	IElementType chSINGLE_QUOTE = new PhpElementType("single quote");
	IElementType chBACKTRICK = new PhpElementType("backtrick");
	IElementType chLBRACE = new PhpElementType("{");
	IElementType chRBRACE = new PhpElementType("}");
	IElementType chLPAREN = new PhpElementType("(");
	IElementType chRPAREN = new PhpElementType(")");
	IElementType chLBRACKET = new PhpElementType("[");
	IElementType chRBRACKET = new PhpElementType("]");


	IElementType opPLUS = new PhpElementType("plus"); //+
	IElementType opUNARY_PLUS = new PhpElementType("unary plus"); //+
	IElementType opMINUS = new PhpElementType("minus"); //-
	IElementType opNEGATE = new PhpElementType("negate"); //-
	IElementType opINCREMENT = new PhpElementType("increment"); //++
	IElementType opDECREMENT = new PhpElementType("decrement"); //--
	IElementType opASGN = new PhpElementType("assign"); //=
	IElementType opNOT = new PhpElementType("not"); //!
	IElementType opQUEST = new PhpElementType("ternary"); //?
	IElementType opCOMMA = new PhpElementType("comma"); //,
	IElementType opCONCAT = new PhpElementType("dot"); //.
	IElementType opCOLON = new PhpElementType("colon"); //:
	IElementType opSEMICOLON = new PhpElementType("semicolon"); //;
	IElementType opBIT_AND = new PhpElementType("bit and"); //&
	IElementType opBIT_OR = new PhpElementType("bit or"); //|
	IElementType opBIT_XOR = new PhpElementType("bit xor"); //^
	IElementType opBIT_NOT = new PhpElementType("bit not"); //~
	IElementType opLIT_AND = new PhpElementType("literal and"); //and
	IElementType opLIT_OR = new PhpElementType("literal or"); //or
	IElementType opLIT_XOR = new PhpElementType("literal xor"); //xor
	IElementType opEQUAL = new PhpElementType("equals"); //==
	IElementType opNOT_EQUAL = new PhpElementType("not equals"); //!=
	IElementType opIDENTICAL = new PhpElementType("identical"); //===
	IElementType opNOT_IDENTICAL = new PhpElementType("not identical"); //!==
	IElementType opPLUS_ASGN = new PhpElementType("plus assign"); //+=
	IElementType opMINUS_ASGN = new PhpElementType("minus assign"); //-=
	IElementType opMUL_ASGN = new PhpElementType("multiply assign"); //*=
	IElementType opDIV_ASGN = new PhpElementType("division assign"); ///=
	IElementType opREM_ASGN = new PhpElementType("division remainder assign"); //%=
	IElementType opSHIFT_RIGHT = new PhpElementType("shift right"); //>>
	IElementType opSHIFT_RIGHT_ASGN = new PhpElementType("shift right assign"); //>>=
	IElementType opSHIFT_LEFT = new PhpElementType("shift left"); //<<
	IElementType opSHIFT_LEFT_ASGN = new PhpElementType("shift left assign"); //<<=
	IElementType opAND_ASGN = new PhpElementType("and assign"); //&&=
	IElementType opOR_ASGN = new PhpElementType("or assign"); //||=
	IElementType opBIT_AND_ASGN = new PhpElementType("bit and assign"); //&=
	IElementType opBIT_OR_ASGN = new PhpElementType("bit or assign"); //|=
	IElementType opBIT_XOR_ASGN = new PhpElementType("bit xor assign"); //^=
	IElementType opAND = new PhpElementType("and"); //&&
	IElementType opOR = new PhpElementType("or"); //||
	IElementType opLESS = new PhpElementType("less than"); //<
	IElementType opLESS_OR_EQUAL = new PhpElementType("less than or equal"); //<=
	IElementType opGREATER = new PhpElementType("greater than"); //>
	IElementType opGREATER_OR_EQUAL = new PhpElementType("greater than or equal"); //>=
	IElementType opCONCAT_ASGN = new PhpElementType("concatenation assign"); //.=
	IElementType opSILENCE = new PhpElementType("error silence"); //@
	IElementType opDIV = new PhpElementType("division"); ///
	IElementType SLASH = new PhpElementType("SLASH"); // \
	IElementType opMUL = new PhpElementType("multiply"); //*
	IElementType opREM = new PhpElementType("remainder"); //%
	IElementType opHASH_ARRAY = new PhpElementType("hash"); //=>

	//casting
	IElementType opINTEGER_CAST = new PhpElementType("integer cast");
	IElementType opFLOAT_CAST = new PhpElementType("float cast");
	IElementType opBOOLEAN_CAST = new PhpElementType("boolean cast");
	IElementType opSTRING_CAST = new PhpElementType("string cast");
	IElementType opARRAY_CAST = new PhpElementType("array cast");
	IElementType opOBJECT_CAST = new PhpElementType("object cast");
	IElementType opUNSET_CAST = new PhpElementType("unset cast");

	IElementType DOLLAR = new PhpElementType("dollar");

	IElementType EXPR_SUBST_BEGIN = new PhpElementType("expression substitution begin");
	IElementType EXPR_SUBST_END = new PhpElementType("expression substitution end");


	TokenSet tsPHP_OPENING_TAGS = TokenSet.create(PHP_OPENING_TAG, PHP_ECHO_OPENING_TAG);

	TokenSet tsSTATEMENT_PRIMARY = TokenSet.create(kwIF, kwFOR, kwFOREACH, kwWHILE, kwDO, kwBREAK, kwCONTINUE, kwECHO, kwGLOBAL, kwFUNCTION, kwUNSET, kwSWITCH, kwTRY

	);

	TokenSet KEYWORDS = TokenSet.orSet(tsSTATEMENT_PRIMARY, TokenSet.create(ABSTRACT_KEYWORD, kwARRAY, kwAS, kwBREAK, kwCASE, kwCATCH, kwCLASS, TRAIT_KEYWORD, kwCLONE, kwCONST, kwCONTINUE, kwDEFAULT, kwDIE, kwECHO, kwELSE, kwELSEIF, kwEMPTY, kwENDDECLARE, kwENDFOR, kwENDFOREACH, kwENDIF, kwENDSWITCH, kwENDWHILE, kwEVAL, kwEXCEPTION, kwEXIT, kwEXTENDS, FINAL_KEYWORD, kwFUNCTION, kwGLOBAL, kwIMPLEMENTS, kwINCLUDE, kwINCLUDE_ONCE, INTERFACE_KEYWORD, kwISSET, kwLIST, kwPHP_USER_FILTER, kwPRINT, PRIVATE_KEYWORD, PROTECTED_KEYWORD, PUBLIC_KEYWORD, kwREQUIRE, kwREQUIRE_ONCE, kwRETURN, STATIC_KEYWORD, kwTHROW, kwUNSET, kwVAR, kwNEW, kwINSTANCEOF, NAMESPACE_KEYWORD, USE_KEYWORD));

	TokenSet tsMATH_OPS = TokenSet.create(opPLUS, opMINUS, opMUL, opDIV, opNEGATE, opREM);

	TokenSet tsBIT_OPS = TokenSet.create(opBIT_AND, opBIT_NOT, opBIT_OR, opBIT_XOR, opSHIFT_LEFT, opSHIFT_RIGHT);

	TokenSet tsASGN_OPS = TokenSet.create(opAND_ASGN, opBIT_AND_ASGN, opBIT_OR_ASGN, opBIT_XOR_ASGN, opCONCAT_ASGN, opMINUS_ASGN, opMUL_ASGN, opOR_ASGN, opPLUS_ASGN, opSHIFT_RIGHT_ASGN, opSHIFT_LEFT_ASGN, opREM_ASGN, opASGN);

	TokenSet tsCAST_OPS = TokenSet.create(opINTEGER_CAST, opFLOAT_CAST, opBOOLEAN_CAST, opSTRING_CAST, opARRAY_CAST, opOBJECT_CAST, opUNSET_CAST);

	TokenSet tsUNARY_PREFIX_OPS = TokenSet.orSet(TokenSet.create(opNOT, opDECREMENT, opINCREMENT, opNEGATE, opBIT_NOT, opSILENCE, opUNARY_PLUS, kwNEW, kwPRINT), tsCAST_OPS);

	TokenSet tsUNARY_POSTFIX_OPS = TokenSet.create(opDECREMENT, opINCREMENT);

	TokenSet tsUNARY_OPS = TokenSet.orSet(tsUNARY_PREFIX_OPS, tsUNARY_POSTFIX_OPS);


	TokenSet tsCOMPARE_OPS = TokenSet.create(opEQUAL, opNOT_EQUAL, opIDENTICAL, opNOT_IDENTICAL, opGREATER, opLESS, opGREATER_OR_EQUAL, opLESS_OR_EQUAL);

	TokenSet tsLOGICAL_OPS = TokenSet.create(opAND, opOR);

	TokenSet tsTERNARY_OPS = TokenSet.create(opQUEST/*, opCOLON*/);

	TokenSet tsBINARY_OPS = TokenSet.orSet(TokenSet.create(opLIT_AND, opLIT_OR, opLIT_XOR, opCONCAT, kwINSTANCEOF), tsASGN_OPS, tsBIT_OPS, tsCOMPARE_OPS, tsMATH_OPS, tsLOGICAL_OPS, tsTERNARY_OPS);

	TokenSet tsOPERATORS = TokenSet.orSet(tsBINARY_OPS, tsUNARY_OPS);


	TokenSet tsCOMMENTS = TokenSet.create(LINE_COMMENT, DOC_COMMENT, C_STYLE_COMMENT);

	TokenSet tsCONSTANTS = TokenSet.create(CONST_CLASS, CONST_FILE, CONST_FUNCTION, CONST_LINE, CONST_METHOD);

	TokenSet tsNUMBERS = TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL);

	TokenSet tsSTRINGS = TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE);

	TokenSet tsSTRING_EDGE = TokenSet.create(chDOUBLE_QUOTE, chSINGLE_QUOTE, chBACKTRICK);

	TokenSet tsEXPR_SUBST_MARKS = TokenSet.create(EXPR_SUBST_BEGIN, EXPR_SUBST_END);

	TokenSet tsOPENING_BRACKETS = TokenSet.create(chLBRACE, chLBRACKET, chLPAREN);

	TokenSet tsCLOSING_BRACKETS = TokenSet.create(chRBRACE, chRBRACKET, chRPAREN);

	TokenSet tsBRACKETS = TokenSet.orSet(tsOPENING_BRACKETS, tsCLOSING_BRACKETS);

	TokenSet tsWHITE_SPACE_OR_COMMENT = TokenSet.create(DOC_COMMENT, C_STYLE_COMMENT, LINE_COMMENT, WHITE_SPACE);

	TokenSet tsREFERENCE_FIRST_TOKENS = TokenSet.create(VARIABLE, IDENTIFIER, DOLLAR);

	TokenSet tsOPERAND_FIRST_TOKENS = TokenSet.orSet(tsREFERENCE_FIRST_TOKENS, tsCONSTANTS, tsNUMBERS, tsSTRING_EDGE, TokenSet.create(kwARRAY, kwEMPTY, kwEXIT, kwISSET));

	TokenSet tsPRIMARY_TOKENS = TokenSet.orSet(tsOPERAND_FIRST_TOKENS, tsUNARY_OPS, TokenSet.create(chLPAREN));

	TokenSet tsTERMINATOR = TokenSet.create(opSEMICOLON, PHP_CLOSING_TAG);

	TokenSet tsHEREDOC_IDS = TokenSet.create(HEREDOC_START, HEREDOC_END);

	TokenSet tsCOMMON_SCALARS = TokenSet.orSet(tsCONSTANTS, tsNUMBERS, TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE));

	TokenSet tsJUNKS = TokenSet.create(HTML, PHP_OPENING_TAG, PHP_ECHO_OPENING_TAG);

	TokenSet tsMODIFIERS = TokenSet.create(PRIVATE_KEYWORD, PROTECTED_KEYWORD, PUBLIC_KEYWORD, ABSTRACT_KEYWORD, FINAL_KEYWORD, STATIC_KEYWORD);

	TokenSet tsVARIABLE_MODIFIERS = TokenSet.orSet(tsMODIFIERS, TokenSet.create(kwVAR));

	TokenSet tsEXPRESSION_FIRST_TOKENS = TokenSet.orSet(tsCOMMON_SCALARS, tsCAST_OPS, TokenSet.create(kwPRINT, kwARRAY, kwEXIT, kwREQUIRE, kwREQUIRE_ONCE, kwINCLUDE, kwINCLUDE_ONCE, kwEVAL, kwEMPTY, kwISSET, kwNEW, kwCLONE, kwLIST), TokenSet.create(VARIABLE, VARIABLE_NAME, DOLLAR, IDENTIFIER, opINCREMENT, opDECREMENT, opPLUS, opMINUS, opNOT, opBIT_NOT, opSILENCE, chLPAREN, chDOUBLE_QUOTE, chBACKTRICK, HEREDOC_START));

	TokenSet tsSTATEMENT_FIRST_TOKENS = TokenSet.create(kwIF, kwWHILE, kwDO, kwFOR, kwSWITCH, kwBREAK, kwCONTINUE, kwRETURN, kwGLOBAL, STATIC_KEYWORD, kwECHO, kwUNSET, kwFOREACH, kwDECLARE, kwTRY, kwTHROW);


	TokenSet ASSIGNABLE = TokenSet.create(PhpElementTypes.VARIABLE_REFERENCE, PhpElementTypes.ARRAY, PhpElementTypes.FIELD_REFERENCE);
}
