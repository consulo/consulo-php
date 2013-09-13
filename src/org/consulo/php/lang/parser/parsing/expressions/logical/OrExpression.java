package org.consulo.php.lang.parser.parsing.expressions.logical;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class OrExpression implements PHPTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = AndExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opOR))
			{
				result = AssignmentExpression.parseWithoutPriority(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					result = AndExpression.parse(builder);
				}
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PhpElementTypes.LOGICAL_EXPRESSION);
				result = PhpElementTypes.LOGICAL_EXPRESSION;
				if(builder.compareAndEat(opOR))
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
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = AndExpression.parse(builder);
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.LOGICAL_EXPRESSION);
		if(builder.compareAndEat(opOR))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.LOGICAL_EXPRESSION;
	}
}
