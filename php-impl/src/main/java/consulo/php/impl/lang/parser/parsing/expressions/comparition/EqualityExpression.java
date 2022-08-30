package consulo.php.impl.lang.parser.parsing.expressions.comparition;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.expressions.AssignmentExpression;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.ast.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class EqualityExpression implements PhpTokenTypes
{

	private static TokenSet EQUALITY_OPERATORS = TokenSet.create(opEQUAL, opNOT_EQUAL, opIDENTICAL, opNOT_IDENTICAL);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = RelationalExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(EQUALITY_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = RelationalExpression.parse(builder);
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			marker.done(PhpElementTypes.EQUALITY_EXPRESSION);
			return PhpElementTypes.EQUALITY_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
