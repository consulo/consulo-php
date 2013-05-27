package net.jay.plugins.php.lang.lexer.managers;

import net.jay.plugins.php.lang.lexer.PHPFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 29.08.2007
 * Time: 11:20:49
 */
public class LineCommentManager extends ReadingManager
{

	public static int END_SEEN = 0;
	public static int CLOSING_TAG_SEEN = -1;

	public LineCommentManager(PHPFlexLexer lexer)
	{
		super(lexer);
	}

	public int eat()
	{
		int pos = 0;
		while(true)
		{
			// end seen
			if(!canReadAt(pos) || !canReadAt(pos + 1) || checkForEndDelimiter(pos))
			{
				return pos > 0 ? pos : END_SEEN;
			}

			if(checkForClosingTag(pos))
			{
				return pos > 0 ? pos : CLOSING_TAG_SEEN;
			}
			pos++;
		}
	}

	private boolean checkForEndDelimiter(int pos)
	{
		if(safeReadAt(pos + 1) == '\n' || safeReadAt(pos + 1) == '\r')
		{
			return true;
		}
		return false;
	}

	private boolean checkForClosingTag(int pos)
	{
		if(safeReadAt(pos + 1) == '?' || safeReadAt(pos + 1) == '%')
		{
			if(safeReadAt(pos + 2) == '>')
			{
				return true;
			}
		}
		return false;
	}

	public void reset()
	{
		super.reset();
	}
}
