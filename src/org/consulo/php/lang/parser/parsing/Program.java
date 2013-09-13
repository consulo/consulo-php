package org.consulo.php.lang.parser.parsing;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

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
		/*while (builder.compare(PHPTokenTypes.tsJUNKS)) {
			builder.advanceLexer();
		}*/
		StatementList.parse(builder, PHPTokenTypes.PHP_CLOSING_TAG);
		while(!builder.eof())
		{
			builder.advanceLexer();
		}
	/*while (builder.compare(PHPTokenTypes.PHP_CLOSING_TAG) || builder.compare(PHPTokenTypes.HTML)) {
			builder.advanceLexer();
		}*/
	}


}
