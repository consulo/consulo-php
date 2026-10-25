package consulo.php.impl.lang.lexer.managers;

import consulo.php.impl.lang.lexer._PhpFlexLexer;

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

	private record Heredoc(String id, boolean nowdoc)
	{
	}

	private StatesManager stManager;
	// heredocs nest through interpolation: <<<A {$f(<<<B ... B)} A
	private final java.util.Deque<Heredoc> myHeredocs = new java.util.ArrayDeque<>();
	// length of the closing delimiter found by the last eat(): indentation + label
	private int myEndLength;

	private static int myID = _PhpFlexLexer.ST_START_HEREDOC;

	public HeredocManager(_PhpFlexLexer lexer, StatesManager manager)
	{
		super(lexer);
		stManager = manager;
	}

	/**
	 * Called on {@code <<<LABEL}, {@code <<<"LABEL"} or {@code <<<'LABEL'} (nowdoc)
	 */
	public void startHeredoc()
	{
		String text = lexer.yytext().toString();
		String id = text.substring(text.indexOf("<<<") + 3).trim();
		boolean nowdoc = id.startsWith("'");
		if(id.startsWith("'") || id.startsWith("\""))
		{
			id = id.substring(1, id.length() - 1);
		}
		myHeredocs.push(new Heredoc(id, nowdoc));
		stManager.toState(myID);
	}

	public void endHeredoc()
	{
		if(!myHeredocs.isEmpty())
		{
			myHeredocs.pop();
		}
	}

	public String getHeredocID()
	{
		Heredoc heredoc = myHeredocs.peek();
		return heredoc == null ? "" : heredoc.id();
	}

	public boolean isNowdoc()
	{
		Heredoc heredoc = myHeredocs.peek();
		return heredoc != null && heredoc.nowdoc();
	}

	public int getHeredocEndLength()
	{
		return myEndLength;
	}

	public static boolean isLabelChar(char c)
	{
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_' || c >= 0x80 && c != (char) -1;
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
			return (nextChar >= 'a' && nextChar <= 255) || nextChar == '_' || nextChar == '{';
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

	/**
	 * PHP 7.3+: the closing label starts a line (indentation allowed) and is not followed by a label character
	 */
	private boolean checkForEndDelimiter(int pos)
	{
		if(safeReadAt(pos) != '\n')
		{
			return false;
		}
		int labelStart = pos + 1;
		while(safeReadAt(labelStart) == ' ' || safeReadAt(labelStart) == '\t')
		{
			labelStart++;
		}
		String heredocID = getHeredocID();
		String id = safeReadStringAt(labelStart, heredocID.length());
		if(heredocID.isEmpty() || !id.equals(heredocID) || isLabelChar(safeReadAt(labelStart + heredocID.length())))
		{
			return false;
		}
		myEndLength = labelStart - pos - 1 + heredocID.length();
		return true;
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
			if(!isNowdoc())
			{
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
			}
			pos++;
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		myHeredocs.clear();
	}
}
