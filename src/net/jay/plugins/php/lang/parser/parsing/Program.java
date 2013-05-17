package net.jay.plugins.php.lang.parser.parsing;

import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 9:20:31
 */
public class Program {

	//	start:
	//		statement_list
	//	;
	public static void parse(PHPPsiBuilder builder) {
		/*while (builder.compare(PHPTokenTypes.tsJUNKS)) {
			builder.advanceLexer();
		}*/
		StatementList.parse(builder, PHPTokenTypes.PHP_CLOSING_TAG);
    while (!builder.eof()) {
      builder.advanceLexer();
    }
    /*while (builder.compare(PHPTokenTypes.PHP_CLOSING_TAG) || builder.compare(PHPTokenTypes.HTML)) {
			builder.advanceLexer();
		}*/
	}


}
