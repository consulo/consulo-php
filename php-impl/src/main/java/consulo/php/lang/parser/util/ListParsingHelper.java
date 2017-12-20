package consulo.php.lang.parser.util;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author markov
 * @date 15.10.2007
 */
public class ListParsingHelper implements PhpTokenTypes
{

	public static int parseCommaDelimitedExpressionWithLeadExpr(final PhpPsiBuilder builder, final PsiBuilder.Marker result, final ParserPart2 parser, final boolean eatFollowComma)
	{
		if(result == null)
		{
			if(builder.compare(opCOMMA))
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			else
			{
				return 0;
			}
		}
		int count = 1;
		PsiBuilder.Marker beforeLastCommaMarker = builder.mark();
		while(builder.compareAndEat(opCOMMA))
		{
			if(parser.parse(builder) != null)
			{
				count++;
				beforeLastCommaMarker.drop();
				beforeLastCommaMarker = builder.mark();
			}
			else
			{
				if(builder.compare(opCOMMA))
				{
					builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
				}
				else
				{
					if(!eatFollowComma)
					{
						builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
					}
				}
			}
		}
		if(!eatFollowComma)
		{
			beforeLastCommaMarker.rollbackTo();
		}
		else
		{
			beforeLastCommaMarker.drop();
		}
		return count;

	}

	/**
	 * Parses comma delimited list of expressions
	 *
	 * @param builder        Current builder wrapper
	 * @param result         Result of parsing first element
	 * @param parser         method, used in parsing each single expression in list
	 * @param eatFollowComma If true, the following comma will be eaten
	 * @return number of expressions in list
	 */
	public static int parseCommaDelimitedExpressionWithLeadExpr(final PhpPsiBuilder builder, final IElementType result, final ParserPart parser, final boolean eatFollowComma)
	{
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compare(opCOMMA))
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			else
			{
				return 0;
			}
		}
		int count = 1;
		PsiBuilder.Marker beforeLastCommaMarker = builder.mark();
		while(builder.compareAndEat(opCOMMA))
		{
			if(parser.parse(builder) != PhpElementTypes.EMPTY_INPUT)
			{
				count++;
				beforeLastCommaMarker.drop();
				beforeLastCommaMarker = builder.mark();
			}
			else
			{
				if(builder.compare(opCOMMA))
				{
					builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
				}
				else
				{
					if(!eatFollowComma)
					{
						builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
					}
				}
			}
		}
		if(!eatFollowComma)
		{
			beforeLastCommaMarker.rollbackTo();
		}
		else
		{
			beforeLastCommaMarker.drop();
		}
		return count;

	}

}
