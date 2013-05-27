package net.jay.plugins.php.lang.lexer;

import net.jay.plugins.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.psi.PHPElementType;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public interface PHPTokenTypes extends PhpDocElementTypes
{

	IElementType PHP_OPENING_TAG = new PHPElementType("php opening tag"); // <?php or <?
	IElementType PHP_ECHO_OPENING_TAG = new PHPElementType("php echo opening tag"); // <?=
	IElementType PHP_CLOSING_TAG = new PHPElementType("php closing tag"); // ?>
	IElementType UNKNOWN_SYMBOL = new PHPElementType("dunno what's that");
	IElementType SYNTAX_ERROR = new PHPElementType("syntax error");

	IElementType WHITE_SPACE = new PHPElementType("some whitespace");
	IElementType HTML = new PHPElementType("html");

	IElementType kwIF = new PHPElementType("if");
	IElementType kwELSEIF = new PHPElementType("elseif");
	IElementType kwELSE = new PHPElementType("else");
	IElementType kwFOR = new PHPElementType("for");
	IElementType kwFOREACH = new PHPElementType("foreach keyword");
	IElementType kwWHILE = new PHPElementType("while");
	IElementType kwDO = new PHPElementType("do");
	IElementType kwSWITCH = new PHPElementType("switch");
	IElementType kwCASE = new PHPElementType("case");
	IElementType kwDEFAULT = new PHPElementType("default keyword");
	IElementType kwTRY = new PHPElementType("try");
	IElementType kwCATCH = new PHPElementType("catch");
	IElementType kwDECLARE = new PHPElementType("declare");
	IElementType kwBREAK = new PHPElementType("break");
	IElementType kwENDIF = new PHPElementType("endif");
	IElementType kwENDFOR = new PHPElementType("endfor");
	IElementType kwENDFOREACH = new PHPElementType("endforeach");
	IElementType kwENDWHILE = new PHPElementType("endwhile");
	IElementType kwENDSWITCH = new PHPElementType("endswitch");
	IElementType kwENDDECLARE = new PHPElementType("enddeclare");

	IElementType kwEXIT = new PHPElementType("exit");
	IElementType kwPRIVATE = new PHPElementType("private");
	IElementType kwFUNCTION = new PHPElementType("function");
	IElementType kwNEW = new PHPElementType("new");
	IElementType kwINSTANCEOF = new PHPElementType("instanceof");
	IElementType kwCONST = new PHPElementType("const");
	IElementType kwLIST = new PHPElementType("list");
	IElementType kwIMPLEMENTS = new PHPElementType("implements");
	IElementType kwEVAL = new PHPElementType("eval");
	IElementType kwFINAL = new PHPElementType("final");
	IElementType kwAS = new PHPElementType("as");
	IElementType kwTHROW = new PHPElementType("throw");
	IElementType kwEXCEPTION = new PHPElementType("exception");
	IElementType kwINCLUDE_ONCE = new PHPElementType("include once");
	IElementType kwCLASS = new PHPElementType("class");
	IElementType kwABSTACT = new PHPElementType("abstract");
	IElementType kwINTERFACE = new PHPElementType("interface keyword");
	IElementType kwPUBLIC = new PHPElementType("public keyword");
	IElementType kwSTATIC = new PHPElementType("static keyword");
	IElementType kwCLONE = new PHPElementType("clone keyword");
	IElementType kwISSET = new PHPElementType("isset keyword");
	IElementType kwEMPTY = new PHPElementType("empty keyword");
	IElementType kwRETURN = new PHPElementType("return");
	IElementType kwVAR = new PHPElementType("var");
	IElementType kwPHP_USER_FILTER = new PHPElementType("php user filter");
	IElementType kwCONTINUE = new PHPElementType("continue");
	IElementType kwDIE = new PHPElementType("die");
	IElementType kwPROTECTED = new PHPElementType("protected");
	IElementType kwPRINT = new PHPElementType("print");
	IElementType kwECHO = new PHPElementType("echo");
	IElementType kwINCLUDE = new PHPElementType("include");
	IElementType kwGLOBAL = new PHPElementType("global");
	IElementType kwEXTENDS = new PHPElementType("extends");
	IElementType kwUNSET = new PHPElementType("unset");
	IElementType kwREQUIRE_ONCE = new PHPElementType("require once");
	IElementType kwARRAY = new PHPElementType("array");
	IElementType kwREQUIRE = new PHPElementType("require");

	IElementType CONST_FUNCTION = new PHPElementType("__FUNCTION__ const");
	IElementType CONST_LINE = new PHPElementType("__LINE__ const");
	IElementType CONST_CLASS = new PHPElementType("__CLASS__ const");
	IElementType CONST_METHOD = new PHPElementType("__METHOD__ const");
	IElementType CONST_FILE = new PHPElementType("__FILE__ const");

	IElementType LINE_COMMENT = new PHPElementType("line comment");
	//	IElementType DOC_COMMENT = new PHPElementType("doc comment");
	IElementType C_STYLE_COMMENT = new PHPElementType("C style comment");
	IElementType VARIABLE = new PHPElementType("variable");
	IElementType VARIABLE_NAME = new PHPElementType("variable name"); // ???
	IElementType VARIABLE_OFFSET_NUMBER = new PHPElementType("array index"); // ???
	IElementType DOLLAR_LBRACE = new PHPElementType("${"); // ???
	IElementType IDENTIFIER = new PHPElementType("identifier");
	IElementType PREDEFINED_IDENTIFIER = new PHPElementType("identifier");
	IElementType ARROW = new PHPElementType("arrow");
	IElementType SCOPE_RESOLUTION = new PHPElementType("scope resolution");
	IElementType FLOAT_LITERAL = new PHPElementType("float");
	IElementType INTEGER_LITERAL = new PHPElementType("integer");
	IElementType STRING_LITERAL = new PHPElementType("string");
	IElementType STRING_LITERAL_SINGLE_QUOTE = new PHPElementType("single quoted string");
	IElementType EXEC_COMMAND = new PHPElementType("exec command");
	IElementType ESCAPE_SEQUENCE = new PHPElementType("escape sequence");
	IElementType HEREDOC_START = new PHPElementType("heredoc start");
	IElementType HEREDOC_CONTENTS = new PHPElementType("heredoc");
	IElementType HEREDOC_END = new PHPElementType("heredoc end");


	IElementType chDOUBLE_QUOTE = new PHPElementType("double quote");
	IElementType chSINGLE_QUOTE = new PHPElementType("single quote");
	IElementType chBACKTRICK = new PHPElementType("backtrick");
	IElementType chLBRACE = new PHPElementType("{");
	IElementType chRBRACE = new PHPElementType("}");
	IElementType chLPAREN = new PHPElementType("(");
	IElementType chRPAREN = new PHPElementType(")");
	IElementType chLBRACKET = new PHPElementType("[");
	IElementType chRBRACKET = new PHPElementType("]");


	IElementType opPLUS = new PHPElementType("plus"); //+
	IElementType opUNARY_PLUS = new PHPElementType("unary plus"); //+
	IElementType opMINUS = new PHPElementType("minus"); //-
	IElementType opNEGATE = new PHPElementType("negate"); //-
	IElementType opINCREMENT = new PHPElementType("increment"); //++
	IElementType opDECREMENT = new PHPElementType("decrement"); //--
	IElementType opASGN = new PHPElementType("assign"); //=
	IElementType opNOT = new PHPElementType("not"); //!
	IElementType opQUEST = new PHPElementType("ternary"); //?
	IElementType opCOMMA = new PHPElementType("comma"); //,
	IElementType opCONCAT = new PHPElementType("dot"); //.
	IElementType opCOLON = new PHPElementType("colon"); //:
	IElementType opSEMICOLON = new PHPElementType("semicolon"); //;
	IElementType opBIT_AND = new PHPElementType("bit and"); //&
	IElementType opBIT_OR = new PHPElementType("bit or"); //|
	IElementType opBIT_XOR = new PHPElementType("bit xor"); //^
	IElementType opBIT_NOT = new PHPElementType("bit not"); //~
	IElementType opLIT_AND = new PHPElementType("literal and"); //and
	IElementType opLIT_OR = new PHPElementType("literal or"); //or
	IElementType opLIT_XOR = new PHPElementType("literal xor"); //xor
	IElementType opEQUAL = new PHPElementType("equals"); //==
	IElementType opNOT_EQUAL = new PHPElementType("not equals"); //!=
	IElementType opIDENTICAL = new PHPElementType("identical"); //===
	IElementType opNOT_IDENTICAL = new PHPElementType("not identical"); //!==
	IElementType opPLUS_ASGN = new PHPElementType("plus assign"); //+=
	IElementType opMINUS_ASGN = new PHPElementType("minus assign"); //-=
	IElementType opMUL_ASGN = new PHPElementType("multiply assign"); //*=
	IElementType opDIV_ASGN = new PHPElementType("division assign"); ///=
	IElementType opREM_ASGN = new PHPElementType("division remainder assign"); //%=
	IElementType opSHIFT_RIGHT = new PHPElementType("shift right"); //>>
	IElementType opSHIFT_RIGHT_ASGN = new PHPElementType("shift right assign"); //>>=
	IElementType opSHIFT_LEFT = new PHPElementType("shift left"); //<<
	IElementType opSHIFT_LEFT_ASGN = new PHPElementType("shift left assign"); //<<=
	IElementType opAND_ASGN = new PHPElementType("and assign"); //&&=
	IElementType opOR_ASGN = new PHPElementType("or assign"); //||=
	IElementType opBIT_AND_ASGN = new PHPElementType("bit and assign"); //&=
	IElementType opBIT_OR_ASGN = new PHPElementType("bit or assign"); //|=
	IElementType opBIT_XOR_ASGN = new PHPElementType("bit xor assign"); //^=
	IElementType opAND = new PHPElementType("and"); //&&
	IElementType opOR = new PHPElementType("or"); //||
	IElementType opLESS = new PHPElementType("less than"); //<
	IElementType opLESS_OR_EQUAL = new PHPElementType("less than or equal"); //<=
	IElementType opGREATER = new PHPElementType("greater than"); //>
	IElementType opGREATER_OR_EQUAL = new PHPElementType("greater than or equal"); //>=
	IElementType opCONCAT_ASGN = new PHPElementType("concatenation assign"); //.=
	IElementType opSILENCE = new PHPElementType("error silence"); //@
	IElementType opDIV = new PHPElementType("division"); ///
	IElementType opMUL = new PHPElementType("multiply"); //*
	IElementType opREM = new PHPElementType("remainder"); //%
	IElementType opHASH_ARRAY = new PHPElementType("hash"); //=>

	//casting
	IElementType opINTEGER_CAST = new PHPElementType("integer cast");
	IElementType opFLOAT_CAST = new PHPElementType("float cast");
	IElementType opBOOLEAN_CAST = new PHPElementType("boolean cast");
	IElementType opSTRING_CAST = new PHPElementType("string cast");
	IElementType opARRAY_CAST = new PHPElementType("array cast");
	IElementType opOBJECT_CAST = new PHPElementType("object cast");
	IElementType opUNSET_CAST = new PHPElementType("unset cast");

	IElementType DOLLAR = new PHPElementType("dollar");

	IElementType EXPR_SUBST_BEGIN = new PHPElementType("expression substitution begin");
	IElementType EXPR_SUBST_END = new PHPElementType("expression substitution end");


	TokenSet tsPHP_OPENING_TAGS = TokenSet.create(PHP_OPENING_TAG, PHP_ECHO_OPENING_TAG);

	TokenSet tsSTATEMENT_PRIMARY = TokenSet.create(kwIF, kwFOR, kwFOREACH, kwWHILE, kwDO, kwBREAK, kwCONTINUE, kwECHO, kwGLOBAL, kwFUNCTION, kwUNSET, kwSWITCH, kwTRY

	);

	TokenSet tsKEYWORDS = TokenSet.orSet(tsSTATEMENT_PRIMARY, TokenSet.create(kwABSTACT, kwARRAY, kwAS, kwBREAK, kwCASE, kwCATCH, kwCLASS, kwCLONE, kwCONST, kwCONTINUE, kwDEFAULT, kwDIE, kwECHO, kwELSE, kwELSEIF, kwEMPTY, kwENDDECLARE, kwENDFOR, kwENDFOREACH, kwENDIF, kwENDSWITCH, kwENDWHILE, kwEVAL, kwEXCEPTION, kwEXIT, kwEXTENDS, kwFINAL, kwFUNCTION, kwGLOBAL, kwIMPLEMENTS, kwINCLUDE, kwINCLUDE_ONCE, kwINTERFACE, kwISSET, kwLIST, kwPHP_USER_FILTER, kwPRINT, kwPRIVATE, kwPROTECTED, kwPUBLIC, kwREQUIRE, kwREQUIRE_ONCE, kwRETURN, kwSTATIC, kwTHROW, kwUNSET, kwVAR, kwNEW, kwINSTANCEOF));

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

	TokenSet tsMODIFIERS = TokenSet.create(kwPRIVATE, kwPROTECTED, kwPUBLIC, kwABSTACT, kwFINAL, kwSTATIC);

	TokenSet tsVARIABLE_MODIFIERS = TokenSet.orSet(tsMODIFIERS, TokenSet.create(kwVAR));

	TokenSet tsEXPRESSION_FIRST_TOKENS = TokenSet.orSet(tsCOMMON_SCALARS, tsCAST_OPS, TokenSet.create(kwPRINT, kwARRAY, kwEXIT, kwREQUIRE, kwREQUIRE_ONCE, kwINCLUDE, kwINCLUDE_ONCE, kwEVAL, kwEMPTY, kwISSET, kwNEW, kwCLONE, kwLIST), TokenSet.create(VARIABLE, VARIABLE_NAME, DOLLAR, IDENTIFIER, opINCREMENT, opDECREMENT, opPLUS, opMINUS, opNOT, opBIT_NOT, opSILENCE, chLPAREN, chDOUBLE_QUOTE, chBACKTRICK, HEREDOC_START));

	TokenSet tsSTATEMENT_FIRST_TOKENS = TokenSet.create(kwIF, kwWHILE, kwDO, kwFOR, kwSWITCH, kwBREAK, kwCONTINUE, kwRETURN, kwGLOBAL, kwSTATIC, kwECHO, kwUNSET, kwFOREACH, kwDECLARE, kwTRY, kwTHROW);


	TokenSet ASSIGNABLE = TokenSet.create(PHPElementTypes.VARIABLE, PHPElementTypes.ARRAY, PHPElementTypes.FIELD_REFERENCE);
}
