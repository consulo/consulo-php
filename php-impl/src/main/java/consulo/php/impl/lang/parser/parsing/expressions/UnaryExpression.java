package consulo.php.impl.lang.parser.parsing.expressions;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;

/**
 * @author jay
 * @time 16.12.2007 23:43:35
 */
public class UnaryExpression implements PhpTokenTypes
{
	private static TokenSet CAST_OPERATORS = TokenSet.create(opBOOLEAN_CAST, opINTEGER_CAST, opSTRING_CAST, opARRAY_CAST, opOBJECT_CAST, opUNSET_CAST, opFLOAT_CAST);

	private static TokenSet INC_DEC_OPERATORS = TokenSet.create(opINCREMENT, opDECREMENT);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(CAST_OPERATORS))
		{
			IElementType result = parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.TYPE_CAST_EXPRESSION);
			return PhpElementTypes.TYPE_CAST_EXPRESSION;
		}
		else if(builder.compareAndEat(opBIT_NOT))
		{
			IElementType result = parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.UNARY_EXPRESSION);
			return PhpElementTypes.UNARY_EXPRESSION;
		}
		else if(builder.compareAndEat(opSILENCE))
		{
			IElementType result = parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.SILENCE_EXPRESSION);
			return PhpElementTypes.SILENCE_EXPRESSION;
		}
		else if(builder.compareAndEat(INC_DEC_OPERATORS))
		{
			IElementType result = PrimaryExpression.parse(builder);
			if(!AssignmentExpression.ASSIGNABLE.contains(result))
			{
				builder.error("Expression is not assignable");
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.UNARY_EXPRESSION);
			return PhpElementTypes.UNARY_EXPRESSION;
		}
		else if(builder.compareAndEat(TokenSet.create(opPLUS, opMINUS)))
		{
			IElementType result = parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.UNARY_EXPRESSION);
			return PhpElementTypes.UNARY_EXPRESSION;
		}
		else
		{
			marker.drop();
			IElementType result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = PostfixExpression.parse(builder);
			}
			return result;
		}
	}
}
