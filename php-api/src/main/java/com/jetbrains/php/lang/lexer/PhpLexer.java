/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.php.lang.lexer;

import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.ast.IElementType;
import consulo.language.lexer.Lexer;
import consulo.language.lexer.LexerBase;
import consulo.language.parser.ParserDefinition;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * PHP lexer in the shape of PhpStorm's {@code PhpLexer}: string literals come as quote, content and quote tokens,
 * the content token is {@link PhpTokenTypes#STRING_LITERAL_SINGLE_QUOTE} or {@link PhpTokenTypes#STRING_LITERAL}
 * without the quotes.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public class PhpLexer extends LexerBase
{
	private final Lexer myDelegate;

	private CharSequence myBuffer = "";
	private int myBufferEnd;

	// parts of the current delegate token: the token itself or its split into quote, content and quote
	private final IElementType[] myPartTypes = new IElementType[3];
	private final int[] myPartStarts = new int[4];
	private int myPartCount;
	private int myPart;

	public PhpLexer(boolean highlightingMode)
	{
		this(highlightingMode, PhpLanguageLevel.HIGHEST);
	}

	public PhpLexer(boolean highlightingMode, @Nonnull PhpLanguageLevel languageLevel)
	{
		// the highlighting mode only changes the highlighting of the delegate, the token stream is the same
		myDelegate = ParserDefinition.forLanguage(PhpLanguage.INSTANCE).createLexer(languageLevel);
	}

	@Override
	public void start(@Nonnull CharSequence buffer, int startOffset, int endOffset, int initialState)
	{
		myBuffer = buffer;
		myBufferEnd = endOffset;
		myDelegate.start(buffer, startOffset, endOffset, initialState);
		splitToken();
	}

	@Override
	public int getState()
	{
		return myPart == 0 ? myDelegate.getState() : 0;
	}

	@Nullable
	@Override
	public IElementType getTokenType()
	{
		return myPartCount == 0 ? null : myPartTypes[myPart];
	}

	@Override
	public int getTokenStart()
	{
		return myPartStarts[myPart];
	}

	@Override
	public int getTokenEnd()
	{
		return myPartStarts[myPart + 1];
	}

	@Override
	public void advance()
	{
		if(myPart + 1 < myPartCount)
		{
			myPart++;
			return;
		}
		myDelegate.advance();
		splitToken();
	}

	@Nonnull
	@Override
	public CharSequence getBufferSequence()
	{
		return myBuffer;
	}

	@Override
	public int getBufferEnd()
	{
		return myBufferEnd;
	}

	private void splitToken()
	{
		myPart = 0;
		IElementType type = myDelegate.getTokenType();
		if(type == null)
		{
			myPartCount = 0;
			return;
		}

		int start = myDelegate.getTokenStart();
		int end = myDelegate.getTokenEnd();
		if(type == PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE || type == PhpTokenTypes.STRING_LITERAL)
		{
			char quote = type == PhpTokenTypes.STRING_LITERAL ? '"' : '\'';
			// a binary prefix stays part of the opening quote
			int open = start;
			if(open < end && (myBuffer.charAt(open) == 'b' || myBuffer.charAt(open) == 'B'))
			{
				open++;
			}
			if(end - open >= 2 && myBuffer.charAt(open) == quote && myBuffer.charAt(end - 1) == quote)
			{
				IElementType quoteType = quote == '"' ? PhpTokenTypes.chDOUBLE_QUOTE : PhpTokenTypes.chSINGLE_QUOTE;
				myPartStarts[0] = start;
				myPartTypes[0] = quoteType;
				if(end - open == 2)
				{
					// an empty string has no content token
					myPartCount = 2;
					myPartTypes[1] = quoteType;
					myPartStarts[1] = end - 1;
					myPartStarts[2] = end;
					return;
				}
				myPartCount = 3;
				myPartTypes[1] = type;
				myPartTypes[2] = quoteType;
				myPartStarts[1] = open + 1;
				myPartStarts[2] = end - 1;
				myPartStarts[3] = end;
				return;
			}
		}

		myPartCount = 1;
		myPartTypes[0] = type;
		myPartStarts[0] = start;
		myPartStarts[1] = end;
	}
}
