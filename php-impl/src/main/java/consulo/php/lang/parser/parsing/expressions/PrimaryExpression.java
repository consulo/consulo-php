package consulo.php.lang.parser.parsing.expressions;

import javax.annotation.Nullable;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.parsing.expressions.primary.Array;
import consulo.php.lang.parser.parsing.expressions.primary.NewExpression;
import consulo.php.lang.parser.parsing.expressions.primary.Scalar;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @time 17.12.2007 13:16:25
 */
public class PrimaryExpression implements PhpTokenTypes
{

	//	scalar
	//	| kwARRAY '(' array_pair_list ')'
	//	| '`' encaps_list '`'
	//	| internal_functions_in_yacc
	//	| '(' expr ')'
	//	| kwNEW class_name_reference ctor_arguments
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			variable.done(result);
			return result;
		}
		else
		{
			variable.drop();
		}
		result = Scalar.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		if(builder.compare(chBACKTRICK))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			Scalar.parseEncapsList(builder);
			builder.match(chBACKTRICK);
			marker.done(PhpElementTypes.SHELL_COMMAND);
			return PhpElementTypes.SHELL_COMMAND;
		}
		if(builder.compare(kwARRAY))
		{
			return Array.parse(builder);
		}
		if(builder.compare(LBRACKET))
		{
			return parseArrayExpression(builder, null);
		}
		if(builder.compareAndEat(chLPAREN))
		{
			result = Expression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			builder.match(chRPAREN);
			return PhpElementTypes.EXPRESSION;
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
			if(expr == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.CLONE_EXPRESSION);
			return PhpElementTypes.CLONE_EXPRESSION;
		}
		if(builder.compare(kwEXIT))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseExitExpression(builder);
			marker.done(PhpElementTypes.EXIT_EXPRESSION);
			return PhpElementTypes.EXIT_EXPRESSION;
		}
		result = parseInternalFunctions(builder);
		return result;
	}

	public static IElementType parseArrayExpression(PhpPsiBuilder builder, @Nullable PsiBuilder.Marker otherMarker)
	{
		PsiBuilder.Marker mark = otherMarker == null ? builder.mark() : otherMarker;
		builder.advanceLexer();

		if(builder.getTokenType() != RBRACKET)
		{
			while(!builder.eof())
			{
				IElementType r = UnaryExpression.parse(builder);
				if(r == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error("Expression expected");
					break;
				}

				if(builder.getTokenType() == opCOMMA)
				{
					builder.advanceLexer();
				}
				else if(builder.getTokenType() == RBRACKET)
				{
					break;
				}
				else
				{
					builder.error("Comma expected");
					break;
				}
			}
		}

		builder.compareAndEat(RBRACKET);
		mark.done(PhpElementTypes.ARRAY_EXPRESSION);
		return PhpElementTypes.ARRAY_EXPRESSION;
	}

	//	exit_expr:
	//		/* empty */
	//		| '(' ')'
	//		| '(' expr ')'
	//	;
	private static void parseExitExpression(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(chLPAREN))
		{
			if(!builder.compareAndEat(chRPAREN))
			{
				IElementType result = Expression.parse(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
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
	private static IElementType parseInternalFunctions(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker function = builder.mark();
		if(builder.compareAndEat(TokenSet.create(kwREQUIRE, kwREQUIRE_ONCE, kwINCLUDE, kwINCLUDE_ONCE)))
		{
			IElementType result = Expression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			function.done(PhpElementTypes.INCLUDE_EXPRESSION);
			return PhpElementTypes.INCLUDE_EXPRESSION;
		}
		if(builder.compareAndEat(kwEMPTY))
		{
			builder.match(chLPAREN);
			IElementType result = Variable.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("variable"));
			}
			builder.match(chRPAREN);
			function.done(PhpElementTypes.EMPTY_EXPRESSION);
			return PhpElementTypes.EMPTY_EXPRESSION;
		}
		if(builder.compareAndEat(kwEVAL))
		{
			builder.match(chLPAREN);
			IElementType result = Expression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			builder.match(chRPAREN);
			function.done(PhpElementTypes.EVAL_EXPRESSION);
			return PhpElementTypes.EVAL_EXPRESSION;
		}
		if(builder.compareAndEat(kwISSET))
		{
			builder.match(chLPAREN);
			ParserPart issetVariable = new ParserPart()
			{
				@Override
				public IElementType parse(PhpPsiBuilder builder)
				{
					return Variable.parse(builder);
				}
			};
			IElementType result = issetVariable.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("variable"));
			}
			else
			{
				ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, issetVariable, false);
			}
			builder.match(chRPAREN);
			function.done(PhpElementTypes.ISSET_EXPRESSION);
			return PhpElementTypes.ISSET_EXPRESSION;
		}
		function.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}

}
