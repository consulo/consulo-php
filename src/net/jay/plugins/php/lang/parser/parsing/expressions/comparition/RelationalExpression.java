package net.jay.plugins.php.lang.parser.parsing.expressions.comparition;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.AssignmentExpression;
import net.jay.plugins.php.lang.parser.parsing.expressions.bit.ShiftExpression;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class RelationalExpression implements PHPTokenTypes
{

	private static TokenSet RELATIONAL_OPERATORS = TokenSet.create(opGREATER, opGREATER_OR_EQUAL, opLESS, opLESS_OR_EQUAL);

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = ShiftExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(RELATIONAL_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				result = ShiftExpression.parse(builder);
			}
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("expression"));
			}
			marker.done(PHPElementTypes.RELATIONAL_EXPRESSION);
			return PHPElementTypes.RELATIONAL_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
