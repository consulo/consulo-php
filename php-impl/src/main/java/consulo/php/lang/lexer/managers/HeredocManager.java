package consulo.php.lang.lexer.managers;

import consulo.php.lang.lexer._PhpFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: Jul 8, 2007
 *
 * @author jay
 */
public class HeredocManager extends ReadingManager
{

	public static final int END_SEEN = 0;
	public static final int SIMPLE_ESCAPE_SEEN = -1;
	public static final int EXPR_SUBT_SEEN = -3;
	public static final int VARIABLE_SEEN = -4;

	private StatesManager stManager;
	private String heredocID;
	private static int myID = _PhpFlexLexer.ST_START_HEREDOC;

	public HeredocManager(_PhpFlexLexer lexer, StatesManager manager)
	{
		super(lexer);
		stManager = manager;
	}

	public void startHeredoc()
	{
		heredocID = lexer.yytext().toString().substring(3).trim();
		stManager.toState(myID);
	}

	public String getHeredocID()
	{

		return heredocID;
	}

	public int getHeredocEndLength()
	{
		return heredocID.length();
	}

	/**
	 * Checks for simple escape sequence, i.e. Backslash and end delimiter or two backslashes
	 * at the offset pos
	 *
	 * @param pos position to check
	 * @return true if simple esc was found
	 */
	private boolean checkForSimpleEsc(final int pos)
	{
		char c0 = safeReadAt(pos);
		char c1 = safeReadAt(pos + 1);
		return c0 == '\\' && (c1 == '\'' || c1 == '"' || c1 == '\\');
	}

	private boolean checkForVariable(int pos)
	{
		if(safeReadAt(pos - 1) == '\\')
		{
			return false;
		}
		if(safeReadAt(pos) == '$')
		{
			char nextChar = safeReadAt(pos + 1);
			if(nextChar == '~')
			{
				return false;
			}
			return (nextChar >= 'a' && nextChar <= 255) || nextChar == '_';
		}
		return false;
	}

	private boolean checkForExprSubtitution(int pos)
	{
		if(safeReadAt(pos - 1) == '\\')
		{
			return false;
		}
		String s = safeReadStringAt(pos, 2);
		return s.equals("{$");
	}

	private boolean checkForEndDelimiter(int pos)
	{
		if(safeReadAt(pos) != '\n')
		{
			return false;
		}
		String id = safeReadStringAt(pos + 1, heredocID.length());
		if(!id.equals(heredocID))
		{
			return false;
		}
		int afterID = pos + 1 + heredocID.length();
		if(safeReadAt(afterID) == '\n')
		{
			return true;
		}
		else if(safeReadAt(afterID) == ';' && safeReadAt(afterID + 1) == '\n')
		{
			return true;
		}
		else
		{
			return false;
		}
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

			if(checkForVariable(pos))
			{
				return pos > 0 ? pos : VARIABLE_SEEN;
			}
			pos++;
		}
	}

	@Override
	public void reset()
	{
		super.reset();
	}
}
