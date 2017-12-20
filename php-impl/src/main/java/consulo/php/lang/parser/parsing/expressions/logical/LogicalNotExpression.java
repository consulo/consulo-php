package consulo.php.lang.parser.parsing.expressions.logical;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import consulo.php.lang.parser.parsing.expressions.InstanceofExpression;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 16.12.2007 21:00:36
 */
public class LogicalNotExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(opNOT))
		{
			parse(builder);
			marker.done(PhpElementTypes.UNARY_EXPRESSION);
			return PhpElementTypes.UNARY_EXPRESSION;
		}
		else
		{
			marker.drop();
			IElementType result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = InstanceofExpression.parse(builder);
			}
			return result;
		}
	}
}
