package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 17.12.2007 13:40:42
 */
public class PostfixExpression implements PHPTokenTypes
{

	private static TokenSet POSTFIX_OPERATORS = TokenSet.create(opINCREMENT, opDECREMENT);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = PrimaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(POSTFIX_OPERATORS))
		{
			if(!ASSIGNABLE.contains(result))
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
