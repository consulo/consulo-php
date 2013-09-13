package org.consulo.php.lang.parser.parsing.expressions.comparition;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.AssignmentExpression;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class EqualityExpression implements PHPTokenTypes
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
