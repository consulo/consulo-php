package net.jay.plugins.php.lang.lexer.managers;

import org.jetbrains.annotations.NotNull;
import net.jay.plugins.php.lang.lexer.PHPFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 12.03.2007
 *
 * @author jay
 */
public class SingleQuotedStringManager extends StringManager {

	public SingleQuotedStringManager(@NotNull final PHPFlexLexer lexer) {
		super(lexer);
		DELIMITER = '\'';
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
			pos++;
		}
	}

}
