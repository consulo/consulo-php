package org.consulo.php.lang.lexer.managers;

import net.jay.plugins.php.lang.lexer.PHPFlexLexer;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 12.03.2007
 * <p/>
 * PHP 5 behavior is as follows with double quoted strings:
 * "${}" causes           syntax error, unexpected '}'
 * "$"                    is a dollar
 * "{}"                   are braces
 * "{$}" causes           syntax error, unexpected '}', expecting T_VARIABLE or '$'
 * "{$ANY_NOT_WORD_CHAR}" causes syntax error
 * "\{$}"                 is itself
 *
 * @author jay
 */
public class DoubleQuotedStringManager extends SubstitutionsAwareStringManager
{


	public DoubleQuotedStringManager(@NotNull final PHPFlexLexer lexer)
	{
		super(lexer);
		DELIMITER = '"';
	}

	public int eat()
	{
		int pos = 0;
		while(true)
		{
			// end seen
			if(!canReadAt(pos) || checkForEndDelimiter(pos))
			{
				return pos > 0 ? pos : END_SEEN;
			}
			// simple escape sequence
			if(checkForSimpleEsc(pos))
			{
				return pos > 0 ? pos : SIMPLE_ESCAPE_SEEN;
			}
			// expr subtitution
			if(checkForExprSubtitution(pos))
			{
				return pos > 0 ? pos : EXPR_SUBT_SEEN;
			}
			// variable
			if(checkForVariable(pos))
			{
				return pos > 0 ? pos : VARIABLE_SEEN;
			}
			pos++;
		}
	}

}
