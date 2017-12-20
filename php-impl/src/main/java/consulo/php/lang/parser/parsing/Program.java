package consulo.php.lang.parser.parsing;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.util.PhpPsiBuilder;

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
		/*while (builder.compare(PhpTokenTypes.tsJUNKS)) {
			builder.advanceLexer();
		}*/
		StatementList.parse(builder, PhpTokenTypes.PHP_CLOSING_TAG);
		while(!builder.eof())
		{
			builder.advanceLexer();
		}
	/*while (builder.compare(PhpTokenTypes.PHP_CLOSING_TAG) || builder.compare(PhpTokenTypes.HTML)) {
			builder.advanceLexer();
		}*/
	}


}
