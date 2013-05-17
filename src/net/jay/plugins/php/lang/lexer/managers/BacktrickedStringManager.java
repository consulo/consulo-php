package net.jay.plugins.php.lang.lexer.managers;

import net.jay.plugins.php.lang.lexer.PHPFlexLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public class BacktrickedStringManager extends SubstitutionsAwareStringManager {

	public BacktrickedStringManager(@NotNull final PHPFlexLexer lexer) {
		super(lexer);
		DELIMITER = '`';
	}

	public int eat() {
		int pos = 0;
		while (true) {
// end seen
			if (!canReadAt(pos) || checkForEndDelimiter(pos)) {
				return pos > 0 ? pos : END_SEEN;
			}
// simple escape sequence
			if (checkForSimpleEsc(pos)) {
				return pos > 0 ? pos : SIMPLE_ESCAPE_SEEN;
			}
// expr subtitution
			if (checkForExprSubtitution(pos)) {
				return pos > 0 ? pos : EXPR_SUBT_SEEN;
			}
// variable
			if (checkForVariable(pos)) {
				return pos > 0 ? pos : VARIABLE_SEEN;
			}
			pos++;
		}
	}
}
