package org.consulo.php.lang.parser.parsing.expressions.bit;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.parsing.expressions.comparition.EqualityExpression;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class BitAndExpression implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = EqualityExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opBIT_AND))
			{
				result = AssignmentExpression.parseWithoutPriority(builder);
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					result = EqualityExpression.parse(builder);
				}
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PHPElementTypes.BIT_EXPRESSION);
				result = PHPElementTypes.BIT_EXPRESSION;
				if(builder.compareAndEat(opBIT_AND))
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

	private static IElementType subParse(PHPPsiBuilder builder, PsiBuilder.Marker marker)
	{
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			result = EqualityExpression.parse(builder);
		}
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.BIT_EXPRESSION);
		if(builder.compareAndEat(opBIT_AND))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PHPElementTypes.BIT_EXPRESSION;
	}
}