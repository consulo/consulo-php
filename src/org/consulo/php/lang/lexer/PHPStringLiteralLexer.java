package org.consulo.php.lang.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.10.2007
 * Time: 14:32:43
 */
public class PHPStringLiteralLexer extends LexerBase
{
	private static final Logger LOG = Logger.getInstance("#org.consulo.php.lang.lexer.PHPStringLiteralLexer");

	private static final short BEFORE_FIRST_QUOTE = 0;
	private static final short AFTER_FIRST_QUOTE = 1;
	private static final short AFTER_LAST_QUOTE = 2;
	private static final short LAST_STATE = AFTER_LAST_QUOTE;

	public static final short TYPE_SINGLE_QUOTE = 1;
	public static final short TYPE_DOUBLE_QUOTE = 2;

	public static final char NO_QUOTE_CHAR = (char) -1;

	private CharSequence myBuffer;
	private int myStart;
	private int myEnd;
	private int myState;
	private int myLastState;
	private int myBufferEnd;
	private char myQuoteChar;
	private IElementType myOriginalLiteralToken;
	private int myStringType;

	public PHPStringLiteralLexer(char quoteChar, final IElementType originalLiteralToken, int stringType)
	{
		myQuoteChar = quoteChar;
		myOriginalLiteralToken = originalLiteralToken;
		LOG.assertTrue(stringType == TYPE_SINGLE_QUOTE || stringType == TYPE_DOUBLE_QUOTE, "Invalid string type");
		myStringType = stringType;
	}

	public void start(CharSequence buffer, int startOffset, int endOffset, int initialState)
	{
		myBuffer = buffer;
		myStart = startOffset;
		if(myQuoteChar == NO_QUOTE_CHAR)
		{
			myState = AFTER_FIRST_QUOTE;
		}
		else
		{
			myState = initialState;
		}
		myLastState = initialState;
		myBufferEnd = endOffset;
		myEnd = locateToken(myStart);
	}

	public int getState()
	{
		return myLastState;
	}

	private static boolean isHexDigit(char c)
	{
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}

	private static boolean isOctalDigit(char c)
	{
		return (c >= '0' && c <= '7');
	}

	private IElementType getTokenTypeSingleQuote(final char nextChar)
	{
		if(nextChar == '\'' || nextChar == '\\')
		{
			return StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
		}
		return myOriginalLiteralToken;
	}

	private IElementType getTokenTypeDoubleQuote(final char nextChar)
	{
		if(nextChar == 'x')
		{
			for(int i = myStart + 2; i < myStart + 4; i++)
			{
				if(i >= myEnd || (i == myStart + 2 && !isHexDigit(myBuffer.charAt(i))))
					return StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN;
				else if(i > myStart + 2 && !isHexDigit(myBuffer.charAt(i)))
					break;
			}
			return StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
		}

		if(isOctalDigit(nextChar))
		{
			for(int i = myStart + 1; i < myStart + 4; i++)
			{
				if(i >= myEnd || (i == myStart + 1 && !isOctalDigit(myBuffer.charAt(i))))
					return StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN;
				else if(i > myStart + 1 && !isOctalDigit(myBuffer.charAt(i)))
					break;
			}
			return StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
		}

		switch(nextChar)
		{
			case 'n':
			case 'r':
			case 't':
			case '\"':
			case '\\':
				return StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
		}

		return StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN;
	}

	public IElementType getTokenType()
	{
		if(myStart >= myEnd)
			return null;

		if(myBuffer.charAt(myStart) != '\\')
			return myOriginalLiteralToken;

		if(myStart + 1 >= myEnd)
			return StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN;
		final char nextChar = myBuffer.charAt(myStart + 1);

		if(myStringType == TYPE_SINGLE_QUOTE)
		{
			return this.getTokenTypeSingleQuote(nextChar);
		}
		if(myStringType == TYPE_DOUBLE_QUOTE)
		{
			return this.getTokenTypeDoubleQuote(nextChar);
		}
		return null;
	}

	public int getTokenStart()
	{
		return myStart;
	}

	public int getTokenEnd()
	{
		return myEnd;
	}

	private int locateToken(int start)
	{
		if(start == myBufferEnd)
		{
			myState = AFTER_LAST_QUOTE;
		}
		if(myState == AFTER_LAST_QUOTE)
			return start;

		int i = start;
		if(myBuffer.charAt(i) == '\\')
		{
			LOG.assertTrue(myState == AFTER_FIRST_QUOTE);
			i++;
			if(i == myBufferEnd)
			{
				myState = AFTER_LAST_QUOTE;
				return i;
			}

			if(myBuffer.charAt(i) >= '0' && myBuffer.charAt(i) <= '7')
			{
				char first = myBuffer.charAt(i);
				i++;
				if(i < myBufferEnd && myBuffer.charAt(i) >= '0' && myBuffer.charAt(i) <= '7')
				{
					i++;
					if(i < myBufferEnd && first <= '3' && myBuffer.charAt(i) >= '0' && myBuffer.charAt(i) <= '7')
					{
						i++;
					}
				}
				return i;
			}

			if(myBuffer.charAt(i) == 'x')
			{
				i++;
				for(; i < start + 4; i++)
				{
					if(i == myBufferEnd ||
							myBuffer.charAt(i) == '\n' ||
							myBuffer.charAt(i) == myQuoteChar)
					{
						return i;
					}
				}
				return i;
			}
			else
			{
				return i + 1;
			}
		}
		else
		{
			LOG.assertTrue(myState == AFTER_FIRST_QUOTE || myBuffer.charAt(i) == myQuoteChar);
			while(i < myBufferEnd)
			{
				if(myBuffer.charAt(i) == '\\')
				{
					return i;
				}
				//if (myBuffer.charAt(i) == '\n') {
				//  myState = AFTER_LAST_QUOTE;
				//  return i;
				//}
				if(myState == AFTER_FIRST_QUOTE && myBuffer.charAt(i) == myQuoteChar)
				{
					myState = AFTER_LAST_QUOTE;
					return i + 1;
				}
				i++;
				myState = AFTER_FIRST_QUOTE;
			}
		}

		return i;
	}

	public void advance()
	{
		myLastState = myState;
		myStart = myEnd;
		myEnd = locateToken(myStart);
	}

	@Override
	public CharSequence getBufferSequence()
	{
		return myBuffer;
	}


	public int getBufferEnd()
	{
		return myBufferEnd;
	}
}
