package consulo.php.lang.parser.parsing.expressions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author markov
 * @date 17.10.2007
 */
public class CommonScalar implements PhpTokenTypes
{
	private static final TokenSet tsCOMMON_SCALARS = TokenSet.orSet(tsNUMBERS, TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE));

	//	common_scalar:
	//		INTEGER_LITERAL
	//		| FLOAT_LITERAL
	//		| STRING_LITERAL
	//	;

	/**
	 * We can use STRING_LITERAL here because it means
	 * that the string has no variables in it
	 *
	 * @param builder current PsiBuilder wrapper
	 * @return EMPTY_INPUT on empty input, COMMON_SCALAR on success
	 */
	public static IElementType parseCommonScalar(PhpPsiBuilder builder)
	{
		if(builder.compare(tsCOMMON_SCALARS))
		{
			PsiBuilder.Marker scalar = builder.mark();
			IElementType type = builder.getTokenType();
			builder.advanceLexer();
			if(TokenSet.create(INTEGER_LITERAL, BINARY_LITERAL, FLOAT_LITERAL).contains(type))
			{
				scalar.done(PhpElementTypes.NUMBER);
				return PhpElementTypes.NUMBER;
			}
			else if(TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE).contains(type))
			{
				scalar.done(PhpElementTypes.STRING);
				return PhpElementTypes.STRING;
			}
			else
			{
				throw new UnsupportedOperationException(type.toString());
			}
		}
		return PhpElementTypes.EMPTY_INPUT;
	}
}

