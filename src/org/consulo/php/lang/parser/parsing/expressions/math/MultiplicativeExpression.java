package org.consulo.php.lang.parser.parsing.expressions.math;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.parsing.expressions.logical.LogicalNotExpression;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 16.12.2007 20:50:23
 */
public class MultiplicativeExpression implements PHPTokenTypes
{

	private static TokenSet MULTIPLICATIVE_OPERATORS = TokenSet.create(opDIV, opMUL, opREM);

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = LogicalNotExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				result = LogicalNotExpression.parse(builder);
			}
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PHPElementTypes.MULTIPLICATIVE_EXPRESSION);
			result = PHPElementTypes.MULTIPLICATIVE_EXPRESSION;
			if(builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
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
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			result = LogicalNotExpression.parse(builder);
		}
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.MULTIPLICATIVE_EXPRESSION);
		if(builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PHPElementTypes.MULTIPLICATIVE_EXPRESSION;
	}
}
