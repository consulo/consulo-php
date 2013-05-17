package net.jay.plugins.php.lang.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.Statement;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class DoWhileStatement implements PHPTokenTypes {

	//		kwDO statement kwWHILE '(' expr ')' ';'
	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(kwDO)) {
			statement.drop();
			return PHPElementTypes.EMPTY_INPUT;
		}
		Statement.parse(builder);
		builder.match(kwWHILE);
		builder.match(chLPAREN);
		Expression.parse(builder);
		builder.match(chRPAREN);
    if (!builder.compare(PHP_CLOSING_TAG)) {
      builder.match(opSEMICOLON);
    }
    statement.done(PHPElementTypes.DO_WHILE);
		return PHPElementTypes.DO_WHILE;
	}
}
