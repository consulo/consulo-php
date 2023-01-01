package consulo.php.impl.lang.parser.parsing.expressions;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;

/**
 * @author jay
 * @time 17.12.2007 13:40:42
 */
public class PostfixExpression implements PhpTokenTypes
{

	private static TokenSet POSTFIX_OPERATORS = TokenSet.create(opINCREMENT, opDECREMENT);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = PrimaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(POSTFIX_OPERATORS))
		{
			if(!AssignmentExpression.ASSIGNABLE.contains(result))
			{
				builder.error("Expression is not assignable");
			}
			marker.done(PhpElementTypes.POSTFIX_EXPRESSION);
			result = PhpElementTypes.POSTFIX_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}

}
