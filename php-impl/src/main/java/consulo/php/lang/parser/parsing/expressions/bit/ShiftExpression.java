package consulo.php.lang.parser.parsing.expressions.bit;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import consulo.php.lang.parser.parsing.expressions.math.AdditiveExpression;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 16.12.2007
 */
public class ShiftExpression implements PhpTokenTypes
{

	private static TokenSet SHIFT_OPERATORS = TokenSet.create(opSHIFT_LEFT, opSHIFT_RIGHT);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = AdditiveExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(SHIFT_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = AdditiveExpression.parse(builder);
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PhpElementTypes.SHIFT_EXPRESSION);
			result = PhpElementTypes.SHIFT_EXPRESSION;
			if(builder.compareAndEat(SHIFT_OPERATORS))
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
		return result;
	}

	private static IElementType subParse(PhpPsiBuilder builder, PsiBuilder.Marker marker)
	{
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = AdditiveExpression.parse(builder);
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.SHIFT_EXPRESSION);
		if(builder.compareAndEat(SHIFT_OPERATORS))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.SHIFT_EXPRESSION;
	}
}
