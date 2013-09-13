package org.consulo.php.lang.parser.parsing.expressions.logical;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 14.12.2007
 */
public class LiteralOrExpression implements PHPTokenTypes
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
