package consulo.php.impl.lang.lexer.managers;

import consulo.php.impl.lang.lexer._PhpFlexLexer;

import javax.annotation.Nonnull;

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
