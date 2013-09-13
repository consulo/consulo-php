package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.parsing.expressions.logical.LiteralOrExpression;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 29.10.2007
 */
public class Expression implements PHPTokenTypes
{

	//	expr:
	//		variable //read
	//		| expr_without_variable
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		return parseExpressionWithoutVariable(builder);
	}

	//	expr_without_variable:
	//		kwLIST '(' assignment_list ')' '=' expr
	//		| variable '=' expr
	//		| variable '=' '&' variable
	//		| variable '=' '&' kwNEW class_name_reference ctor_arguments
	//		| kwNEW class_name_reference ctor_arguments
	//		| kwCLONE expr
	//		| variable opPLUS_ASGN expr
	//		| variable opMINUS_ASGN expr
	//		| variable opMUL_ASGN expr
	//		| variable opDIV_ASGN expr
	//		| variable opCONCAT_ASGN expr
	//		| variable opREM_ASGN expr
	//		| variable opBIT_AND_ASGN expr
	//		| variable opBIT_OR_ASGN expr
	//		| variable opBIT_XOR_ASGN expr
	//		| variable opSHIFT_LEFT_ASGN expr
	//		| variable opSHIFT_RIGHT_ASGN expr
	//		| variable opINCREMENT //read+write
	//		| opINCREMENT variable //read+write
	//		| variable opDECREMENT //read+write
	//		| opDECREMENT variable //read+write
	//		| expr opOR expr
	//		| expr opAND expr
	//		| expr opLIT_OR expr
	//		| expr opLIT_AND expr
	//		| expr opLIT_XOR expr
	//		| expr opBIT_OR expr
	//		| expr opBIT_AND expr
	//		| expr opBIT_XOR expr
	//		| expr opCONCAT expr
	//		| expr opPLUS expr
	//		| expr opMINUS expr
	//		| expr opMUL expr
	//		| expr opDIV expr
	//		| expr opREM expr
	//		| expr opSHIFT_LEFT expr
	//		| expr opSHIFT_RIGHT expr
	//		| opPLUS expr //with precedence of opINCREMENT
	//		| opMINUS expr //with precedence of opINCREMENT
	//		| opNOT expr
	//		| opBIT_NOT expr
	//		| expr opIDENTICAL expr
	//		| expr opNOT_IDENTICAL expr
	//		| expr opEQUAL expr
	//		| expr opNOT_EQUAL expr
	//		| expr opLESS expr
	//		| expr opLESS_OR_EQUAL expr
	//		| expr opGREATER expr
	//		| expr opGREATER_OR_EQUAL expr
	//		| expr kwINSTANCEOF class_name_reference
	//		| '(' expr ')'
	//		| expr '?'
	//			expr ':'
	//			expr
	//		| internal_functions_in_yacc
	//		| opINTEGER_CAST expr
	//		| opFLOAT_CAST expr
	//		| opSTRING_CAST expr
	//		| opARRAY_CAST expr
	//		| opOBJECT_CAST expr
	//		| opBOOLEAN_CAST expr
	//		| opUNSET_CAST expr
	//		| kwEXIT exit_expr
	//		| opSILENCE expr
	//		| scalar
	//		| kwARRAY '(' array_pair_list ')'
	//		| '`' encaps_list '`'
	//		| kwPRINT expr
	//	;
	/*
	 * This expression starts with
	 *   — scalar,
	 *   — array,
	 *   — print,
	 *   — exit,
	 *   — cast,
	 *   — internal functions,
	 *   — parenthesis,
	 *   — prefix unary operators,
	 *   — list,
	 *   — variable,
	 *   — new or
	 *   — clone
	 */
	private static IElementType parseExpressionWithoutVariable(PhpPsiBuilder builder)
	{
		return LiteralOrExpression.parse(builder);
	}

}
