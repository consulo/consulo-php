package org.consulo.php.lang.findUsages;

import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.lang.lexer.PhpFlexAdapter;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Processor;

/**
 * @author jay
 * @date Jul 1, 2008 12:25:33 AM
 */
public class PhpWordsScanner implements WordsScanner
{
	@Override
	public void processWords(CharSequence fileText, final Processor<WordOccurrence> processor)
	{
		Lexer lexer = new PhpFlexAdapter(PhpLanguageLevel.HIGHEST);
		lexer.start(fileText, 0, fileText.length(), 0);
		WordOccurrence occurrence = null; // shared occurrence

		while(lexer.getTokenType() != null)
		{
			final IElementType type = lexer.getTokenType();
			if(type == PhpTokenTypes.IDENTIFIER || PhpTokenTypes.KEYWORDS.contains(type))
			{
				if(occurrence == null)
				{
					occurrence = new WordOccurrence(fileText, lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
				}
				else
				{
					occurrence.init(fileText, lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
				}
				if(!processor.process(occurrence))
				{
					return;
				}
			}
			else if(type == PhpTokenTypes.VARIABLE)
			{
				if(occurrence == null)
				{
					occurrence = new WordOccurrence(fileText, lexer.getTokenStart() + 1, lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
				}
				else
				{
					occurrence.init(fileText, lexer.getTokenStart() + 1, lexer.getTokenEnd(), WordOccurrence.Kind.CODE);
				}
				if(!processor.process(occurrence))
				{
					return;
				}
			}
			else if(PhpTokenTypes.tsCOMMENTS.contains(type))
			{
				if(!stripWords(processor, fileText, lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.COMMENTS, occurrence))
				{
					return;
				}
			}
			else if(PhpTokenTypes.tsSTRINGS.contains(type))
			{
				if(!stripWords(processor, fileText, lexer.getTokenStart(), lexer.getTokenEnd(), WordOccurrence.Kind.LITERALS, occurrence))
				{
					return;
				}
			}

			lexer.advance();
		}
	}

	private static boolean stripWords(final Processor<WordOccurrence> processor, final CharSequence tokenText, int from, int to, final WordOccurrence.Kind kind, WordOccurrence occurrence)
	{
		// This code seems strange but it is more effective as Character.isJavaIdentifier_xxx_ is quite costly operation due to unicode
		int index = from;

		ScanWordsLoop:
		while(true)
		{
			while(true)
			{
				if(index == to)
				{
					break ScanWordsLoop;
				}
				char c = tokenText.charAt(index);
				if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') ||
						(Character.isJavaIdentifierStart(c) && c != '$'))
				{
					break;
				}
				index++;
			}
			int index1 = index;
			while(true)
			{
				index++;
				if(index == to)
				{
					break;
				}
				char c = tokenText.charAt(index);
				if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
				{
					continue;
				}
				if(!Character.isJavaIdentifierPart(c) || c == '$')
				{
					break;
				}
			}

			if(occurrence == null)
			{
				occurrence = new WordOccurrence(tokenText, index1, index, kind);
			}
			else
			{
				occurrence.init(tokenText, index1, index, kind);
			}
			if(!processor.process(occurrence))
			{
				return false;
			}
		}
		return true;
	}
}
