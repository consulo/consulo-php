package consulo.php.lang.lexer.managers;

import javax.annotation.Nonnull;

import consulo.php.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 12.03.2007
 *
 * @author jay
 */
public class SingleQuotedStringManager extends StringManager
{
	public SingleQuotedStringManager(@Nonnull final _PhpFlexLexer lexer)
	{
		super(lexer);
		DELIMITER = '\'';
	}

	@Override
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
			pos++;
		}
	}

}
