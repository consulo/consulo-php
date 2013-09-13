package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.logical.LiteralOrExpression;
import org.consulo.php.lang.parser.parsing.expressions.logical.OrExpression;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class TernaryExpression implements PHPTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = OrExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(opQUEST))
		{
			IElementType expr = LiteralOrExpression.parse(builder);
			if(expr == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			builder.match(opCOLON);
			expr = AssignmentExpression.parseWithoutPriority(builder);
			if(expr == PhpElementTypes.EMPTY_INPUT)
			{
				expr = OrExpression.parse(builder);
			}
			if(expr == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PhpElementTypes.TERNARY_EXPRESSION);
			result = PhpElementTypes.TERNARY_EXPRESSION;
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

	private static IElementType subParse(PhpPsiBuilder builder, PsiBuilder.Marker marker)
	{
		IElementType result = LiteralOrExpression.parse(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		builder.match(opCOLON);
		result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = OrExpression.parse(builder);
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.TERNARY_EXPRESSION);
		if(builder.compareAndEat(opQUEST))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.TERNARY_EXPRESSION;
	}
}
