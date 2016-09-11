package consulo.php.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 03.11.2007
 */
public class BreakStatement implements PhpTokenTypes
{

	//	kwBREAK ';'
	//	| kwBREAK expr ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwBREAK))
		{
			return PhpElementTypes.EMPTY_INPUT;
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
		statement.done(PhpElementTypes.BREAK);
		return PhpElementTypes.BREAK;
	}
}
