package org.consulo.php.lang.parser.parsing.expressions.math;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 16.12.2007 20:43:48
 */
public class AdditiveExpression implements PHPTokenTypes
{

	private static TokenSet ADDITIVE_OPERATORS = TokenSet.create(opPLUS, opMINUS, opCONCAT);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = MultiplicativeExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(ADDITIVE_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = MultiplicativeExpression.parse(builder);
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PhpElementTypes.ADDITIVE_EXPRESSION);
			result = PhpElementTypes.ADDITIVE_EXPRESSION;
			if(builder.compareAndEat(ADDITIVE_OPERATORS))
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
			result = MultiplicativeExpression.parse(builder);
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.ADDITIVE_EXPRESSION);
		if(builder.compareAndEat(ADDITIVE_OPERATORS))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.ADDITIVE_EXPRESSION;
	}

}
