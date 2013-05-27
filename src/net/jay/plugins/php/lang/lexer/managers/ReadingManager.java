package net.jay.plugins.php.lang.lexer.managers;

import net.jay.plugins.php.lang.lexer.PHPFlexLexer;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 01.03.2007
 *
 * @author jay
 */

/**
 * Class used for basic reading from lexers zzBuffer.
 * One instance per each _RubyLexer instance.
 */
public class ReadingManager
{

	protected PHPFlexLexer lexer;
	protected CharSequence zzBuffer;

	public ReadingManager(@NotNull final PHPFlexLexer lexer)
	{
		this.lexer = lexer;
		zzBuffer = lexer.getBuffer();
	}

	protected void reset()
	{
		zzBuffer = lexer.getBuffer();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////// Safe reading ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Reads a symbol
	 *
	 * @param pos offset by the beginning of current token
	 * @return (char)-1 if there is no symbol at position
	 */
	public char safeReadAt(final int pos)
	{
		final int zzStartRead = lexer.getTokenStart();
		if(canReadAt(pos))
		{
			return zzBuffer.charAt(zzStartRead + pos);
		}
		else
		{
			return (char) -1;
		}
	}

	/**
	 * @param pos offset by the beginning of current token
	 * @return true, if there is a symbol at position
	 */
	public boolean canReadAt(final int pos)
	{
		final int zzStartRead = lexer.getTokenStart();
		int len = zzBuffer.length();
		int loc = zzStartRead + pos;
		return (0 <= loc && loc < len);
	}


	/**
	 * Reads a string
	 *
	 * @param pos  offset by the beginning of current token
	 * @param sLen expected string length
	 * @return string
	 */
	public String safeReadStringAt(final int pos, final int sLen)
	{
		String result = "";
		for(int i = 0; canReadAt(pos + i) && i < sLen; i++)
		{
			result += safeReadAt(pos + i);
		}
		return result;
	}

}
