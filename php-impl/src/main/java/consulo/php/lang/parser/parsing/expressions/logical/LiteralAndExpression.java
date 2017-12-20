package consulo.php.lang.parser.parsing.expressions.logical;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 14.12.2007
 */
public class LiteralAndExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = AssignmentExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opLIT_AND))
			{
				result = LiteralAndExpression.parse(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PhpElementTypes.LITERAL_LOGICAL_EXPRESSION);
				result = PhpElementTypes.LITERAL_LOGICAL_EXPRESSION;
				if(builder.compareAndEat(opLIT_AND))
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
		if(LiteralAndExpression.parse(builder) == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.LITERAL_LOGICAL_EXPRESSION);
		if(builder.compareAndEat(opLIT_AND))
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
