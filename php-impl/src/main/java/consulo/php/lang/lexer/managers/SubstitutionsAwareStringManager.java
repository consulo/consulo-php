package consulo.php.lang.lexer.managers;

import javax.annotation.Nonnull;

import consulo.php.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 23.03.2007
 *
 * @author jay
 */
public abstract class SubstitutionsAwareStringManager extends StringManager
{

	public SubstitutionsAwareStringManager(@Nonnull final _PhpFlexLexer lexer)
	{
		super(lexer);
	}

	protected boolean checkForVariable(int pos)
	{
		if(safeReadAt(pos - 1) == '\\')
		{
			return false;
		}
		return safeReadAt(pos) == '$';
	}

	protected boolean checkForExprSubtitution(int pos)
	{
		if(safeReadAt(pos - 1) == '\\')
		{
			return false;
		}
		String s = safeReadStringAt(pos, 2);
		return s.equals("{$");
	}
}
