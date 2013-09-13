package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 03.11.2007
 */
public class ContinueStatement implements PHPTokenTypes
{

	//	kwCONTINUE ';'
	//	| kwCONTINUE expr ';'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwCONTINUE))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();
		if(!builder.compareAndEat(opSEMICOLON))
		{
			Expression.parse(builder);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		statement.done(PHPElementTypes.CONTINUE);
		return PHPElementTypes.CONTINUE;
	}
}