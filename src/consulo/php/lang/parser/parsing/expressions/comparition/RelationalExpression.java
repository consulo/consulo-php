package consulo.php.lang.parser.parsing.expressions.comparition;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import consulo.php.lang.parser.parsing.expressions.bit.ShiftExpression;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class RelationalExpression implements PhpTokenTypes
{

	private static TokenSet RELATIONAL_OPERATORS = TokenSet.create(opGREATER, opGREATER_OR_EQUAL, opLESS, opLESS_OR_EQUAL);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = ShiftExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(RELATIONAL_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = ShiftExpression.parse(builder);
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			marker.done(PhpElementTypes.RELATIONAL_EXPRESSION);
			return PhpElementTypes.RELATIONAL_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
