package org.consulo.php.lang.parser.parsing.expressions.logical;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.parsing.expressions.InstanceofExpression;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 16.12.2007 21:00:36
 */
public class LogicalNotExpression implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(opNOT))
		{
			parse(builder);
			marker.done(PHPElementTypes.UNARY_EXPRESSION);
			return PHPElementTypes.UNARY_EXPRESSION;
		}
		else
		{
			marker.drop();
			IElementType result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				result = InstanceofExpression.parse(builder);
			}
			return result;
		}
	}
}
