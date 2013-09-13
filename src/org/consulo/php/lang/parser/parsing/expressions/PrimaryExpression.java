package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.parsing.expressions.primary.Array;
import org.consulo.php.lang.parser.parsing.expressions.primary.NewExpression;
import org.consulo.php.lang.parser.parsing.expressions.primary.Scalar;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 17.12.2007 13:16:25
 */
public class PrimaryExpression implements PHPTokenTypes
{

	//	scalar
	//	| kwARRAY '(' array_pair_list ')'
	//	| '`' encaps_list '`'
	//	| internal_functions_in_yacc
	//	| '(' expr ')'
	//	| kwNEW class_name_reference ctor_arguments
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			variable.done(result);
			return result;
		}
		else
		{
			variable.drop();
		}
		result = Scalar.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		if(builder.compare(chBACKTRICK))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			Scalar.parseEncapsList(builder);
			builder.match(chBACKTRICK);
			marker.done(PHPElementTypes.SHELL_COMMAND);
			return PHPElementTypes.SHELL_COMMAND;
		}
		if(builder.compare(kwARRAY))
		{
			return Array.parse(builder);
		}
		if(builder.compareAndEat(chLPAREN))
		{
			result = Expression.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			builder.match(chRPAREN);
			return PHPElementTypes.EXPRESSION;
		}
		if(builder.compare(kwNEW))
		{
			return NewExpression.parse(builder);
		}
		if(builder.compare(kwCLONE))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			IElementType expr = parse(builder);
			if(expr == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PHPElementTypes.CLONE_EXPRESSION);
			return PHPElementTypes.CLONE_EXPRESSION;
		}
		if(builder.compare(kwEXIT))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseExitExpression(builder);
			marker.done(PHPElementTypes.EXIT_EXPRESSION);
			return PHPElementTypes.EXIT_EXPRESSION;
		}
		result = parseInternalFunctions(builder);
		return result;
	}

	//	exit_expr:
	//		/* empty */
	//		| '(' ')'
	//		| '(' expr ')'
	//	;
	private static void parseExitExpression(PHPPsiBuilder builder)
	{
		if(builder.compareAndEat(chLPAREN))
		{
			if(!builder.compareAndEat(chRPAREN))
			{
				IElementType result = Expression.parse(builder);
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.EXPRESSION_EXPECTED_MESSAGE);
				}
				builder.match(chRPAREN);
			}
		}
	}

	//	internal_functions_in_yacc:
	//		kwISSET '(' isset_variables ')'
	//		| kwEMPTY '(' variable ')'
	//		| kwINCLUDE expr
	//		| kwINCLUDE_ONCE expr
	//		| kwEVAL '(' expr ')'
	//		| kwREQUIRE expr
	//		| kwREQUIRE_ONCE expr
	//	;
	//
	//	isset_variables:
	//		variable
	//		| isset_variables ',' variable
	//	;
	private static IElementType parseInternalFunctions(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker function = builder.mark();
		if(builder.compareAndEat(TokenSet.create(kwREQUIRE, kwREQUIRE_ONCE, kwINCLUDE, kwINCLUDE_ONCE)))
		{
			IElementType result = Expression.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			function.done(PHPElementTypes.INCLUDE_EXPRESSION);
			return PHPElementTypes.INCLUDE_EXPRESSION;
		}
		if(builder.compareAndEat(kwEMPTY))
		{
			builder.match(chLPAREN);
			IElementType result = Variable.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("variable"));
			}
			builder.match(chRPAREN);
			function.done(PHPElementTypes.EMPTY_EXPRESSION);
			return PHPElementTypes.EMPTY_EXPRESSION;
		}
		if(builder.compareAndEat(kwEVAL))
		{
			builder.match(chLPAREN);
			IElementType result = Expression.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			builder.match(chRPAREN);
			function.done(PHPElementTypes.EVAL_EXPRESSION);
			return PHPElementTypes.EVAL_EXPRESSION;
		}
		if(builder.compareAndEat(kwISSET))
		{
			builder.match(chLPAREN);
			ParserPart issetVariable = new ParserPart()
			{
				public IElementType parse(PHPPsiBuilder builder)
				{
					return Variable.parse(builder);
				}
			};
			IElementType result = issetVariable.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("variable"));
			}
			else
			{
				ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, issetVariable, false);
			}
			builder.match(chRPAREN);
			function.done(PHPElementTypes.ISSET_EXPRESSION);
			return PHPElementTypes.ISSET_EXPRESSION;
		}
		function.drop();
		return PHPElementTypes.EMPTY_INPUT;
	}

}
