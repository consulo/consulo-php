package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.logical.LiteralOrExpression;
import org.consulo.php.lang.parser.parsing.expressions.logical.OrExpression;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class TernaryExpression implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = OrExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(opQUEST))
		{
			IElementType expr = LiteralOrExpression.parse(builder);
			if(expr == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			builder.match(opCOLON);
			expr = AssignmentExpression.parseWithoutPriority(builder);
			if(expr == PHPElementTypes.EMPTY_INPUT)
			{
				expr = OrExpression.parse(builder);
			}
			if(expr == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PHPElementTypes.TERNARY_EXPRESSION);
			result = PHPElementTypes.TERNARY_EXPRESSION;
			if(builder.compareAndEat(opQUEST))
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

	private static IElementType subParse(PHPPsiBuilder builder, PsiBuilder.Marker marker)
	{
		IElementType result = LiteralOrExpression.parse(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("expression"));
		}
		builder.match(opCOLON);
		result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			result = OrExpression.parse(builder);
		}
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.TERNARY_EXPRESSION);
		if(builder.compareAndEat(opQUEST))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PHPElementTypes.TERNARY_EXPRESSION;
	}
}
