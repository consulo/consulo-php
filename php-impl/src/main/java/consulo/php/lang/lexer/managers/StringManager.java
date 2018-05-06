package consulo.php.lang.lexer.managers;

import javax.annotation.Nonnull;

import consulo.php.lang.lexer.PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 12.03.2007
 *
 * @author jay
 */
public abstract class StringManager extends ReadingManager
{

	public static final int END_SEEN = 0;
	public static final int SIMPLE_ESCAPE_SEEN = -1;
	public static final int BACKSLASH_SEEN = -2;
	public static final int EXPR_SUBT_SEEN = -3;
	public static final int VARIABLE_SEEN = -4;
	protected char DELIMITER;

	public StringManager(@Nonnull final PhpFlexLexer lexer)
	{
		super(lexer);
	}

	@Override
	public void reset()
	{
		super.reset();
	}

	/**
	 * Checks for end delimiter at the offset pos
	 *
	 * @param pos position to check
	 * @return true if endDelimiter can be find
	 */
	protected boolean checkForEndDelimiter(final int pos)
	{
		char c = safeReadAt(pos);
		return c == DELIMITER;
	}

	/**
	 * Checks for simple escape sequence, i.e. Backslash and end delimiter or two backslashes
	 * at the offset pos
	 *
	 * @param pos position to check
	 * @return true if simple esc was found
	 */
	protected boolean checkForSimpleEsc(final int pos)
	{
		char c0 = safeReadAt(pos);
		char c1 = safeReadAt(pos + 1);
		return c0 == '\\' && (c1 == DELIMITER || c1 == '\\');
	}

	public abstract int eat();
}
