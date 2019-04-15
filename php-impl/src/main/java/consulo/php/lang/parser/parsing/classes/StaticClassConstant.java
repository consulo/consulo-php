package consulo.php.lang.parser.parsing.classes;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Function;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticClassConstant implements PhpTokenTypes
{
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker mainMarker = builder.mark();

		PsiBuilder.Marker marker = ClassReference.parseClassNameReference(builder, null, false, false, false);

		if(marker == null)
		{
			mainMarker.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}

		if(!builder.compareAndEat(SCOPE_RESOLUTION))
		{
			marker.rollbackTo();
			mainMarker.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}

		if(builder.compareAndEat(kwCLASS))
		{
			mainMarker.done(PhpElementTypes.CLASS_CONSTANT_REFERENCE);

			return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
		}
		else
		{
			builder.match(IDENTIFIER);

			if(builder.getTokenType() == LPAREN)
			{
				Function.parseFunctionCallParameterList(builder);

				mainMarker.done(PhpElementTypes.FUNCTION_CALL);

				return PhpElementTypes.FUNCTION_CALL;
			}
			else
			{
				mainMarker.done(PhpElementTypes.CLASS_CONSTANT_REFERENCE);

				return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
			}
		}
	}
}
