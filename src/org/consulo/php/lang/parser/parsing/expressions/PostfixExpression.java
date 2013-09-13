package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

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

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = PrimaryExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(POSTFIX_OPERATORS))
		{
			if(!ASSIGNABLE.contains(result))
			{
				builder.error("Expression is not assignable");
			}
			marker.done(PHPElementTypes.POSTFIX_EXPRESSION);
			result = PHPElementTypes.POSTFIX_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}

}
