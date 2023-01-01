package consulo.php.impl.lang.parser.parsing.classes;

import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.calls.Function;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.ast.IElementType;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticClassConstant
{
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker mainMarker = builder.mark();

		PsiBuilder.Marker marker = ClassReference.parseClassNameReference(builder, null, ClassReference.ALLOW_STATIC);

		if(marker == null)
		{
			mainMarker.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}

		if(!builder.compareAndEat(PhpTokenTypes.SCOPE_RESOLUTION))
		{
			marker.rollbackTo();
			mainMarker.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}

		if(builder.compareAndEat(PhpTokenTypes.kwCLASS))
		{
			mainMarker.done(PhpElementTypes.CLASS_CONSTANT_REFERENCE);

			return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
		}
		else
		{
			builder.match(PhpTokenTypes.IDENTIFIER);

			if(builder.getTokenType() == PhpTokenTypes.LPAREN)
			{
				Function.parseFunctionCallParameterList(builder);

				mainMarker.done(PhpElementTypes.METHOD_REFERENCE);

				return PhpElementTypes.METHOD_REFERENCE;
			}
			else
			{
				mainMarker.done(PhpElementTypes.CLASS_CONSTANT_REFERENCE);

				return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
			}
		}
	}
}
