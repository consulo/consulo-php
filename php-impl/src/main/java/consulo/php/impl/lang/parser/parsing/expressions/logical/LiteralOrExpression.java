package consulo.php.impl.lang.parser.parsing.expressions.logical;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 14.12.2007
 */
public class LiteralOrExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = LiteralXorExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opLIT_OR))
			{
				result = LiteralXorExpression.parse(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PhpElementTypes.LITERAL_LOGICAL_EXPRESSION);
				result = PhpElementTypes.LITERAL_LOGICAL_EXPRESSION;
				if(builder.compareAndEat(opLIT_OR))
				{
					subParse(builder, newMarker);
				}
				else
				{
					newMarker.drop();
				}
			}
			else
			{
				marker.drop();
			}
		}
		else
		{
			marker.drop();
		}
		return result;
	}

	private static IElementType subParse(PhpPsiBuilder builder, PsiBuilder.Marker marker)
	{
		if(LiteralXorExpression.parse(builder) == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.LITERAL_LOGICAL_EXPRESSION);
		if(builder.compareAndEat(opLIT_OR))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.LITERAL_LOGICAL_EXPRESSION;
	}

}
