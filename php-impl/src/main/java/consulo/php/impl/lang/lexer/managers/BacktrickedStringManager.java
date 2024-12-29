package consulo.php.impl.lang.lexer.managers;

import jakarta.annotation.Nonnull;

import consulo.php.impl.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public class BacktrickedStringManager extends SubstitutionsAwareStringManager
{

	public BacktrickedStringManager(@Nonnull final _PhpFlexLexer lexer)
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
