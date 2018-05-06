package consulo.php.lang.lexer.managers;

import javax.annotation.Nonnull;

import consulo.php.lang.lexer.PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public class BacktrickedStringManager extends SubstitutionsAwareStringManager
{

	public BacktrickedStringManager(@Nonnull final PhpFlexLexer lexer)
	{
		super(lexer);
		DELIMITER = '`';
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
