package consulo.php.impl.lang.parser.parsing.statements;

import consulo.language.ast.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.Statement;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class DoWhileStatement implements PhpTokenTypes
{

	//		kwDO statement kwWHILE '(' expr ')' ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwDO))
		{
			statement.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		Statement.parse(builder);
		builder.match(kwWHILE);
		builder.match(LPAREN);
		Expression.parse(builder);
		builder.match(RPAREN);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PhpElementTypes.DO_WHILE);
		return PhpElementTypes.DO_WHILE;
	}
}
