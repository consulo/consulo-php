package consulo.php.impl.lang.parser.parsing;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 9:20:31
 */
public class Program
{
	//	start:
	//		statement_list
	//	;
	public static void parse(PhpPsiBuilder builder)
	{
		StatementList.parse(builder, PhpTokenTypes.PHP_CLOSING_TAG);
		while(!builder.eof())
		{
			builder.advanceLexer();
		}
	}
}
