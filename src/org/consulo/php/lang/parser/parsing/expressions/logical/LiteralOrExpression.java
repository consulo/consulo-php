package org.consulo.php.lang.parser.parsing.expressions.logical;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 14.12.2007
 */
public class LiteralOrExpression implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = LiteralXorExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opLIT_OR))
			{
				result = LiteralXorExpression.parse(builder);
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PHPElementTypes.LITERAL_LOGICAL_EXPRESSION);
				result = PHPElementTypes.LITERAL_LOGICAL_EXPRESSION;
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

	private static IElementType subParse(PHPPsiBuilder builder, PsiBuilder.Marker marker)
	{
		if(LiteralXorExpression.parse(builder) == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.LITERAL_LOGICAL_EXPRESSION);
		if(builder.compareAndEat(opLIT_OR))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PHPElementTypes.LITERAL_LOGICAL_EXPRESSION;
	}

}
