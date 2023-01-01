package consulo.php.impl.lang.parser.parsing;

import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class ReturnType
{
	public static void parseIfColon(PhpPsiBuilder builder)
	{
		if(builder.getTokenType() == PhpTokenTypes.opCOLON)
		{
			builder.advanceLexer();

			if(!ReturnType.parse(builder))
			{
				builder.error("Expected return type");
			}
		}
	}

	public static boolean parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker mark = builder.mark();

		if(builder.getTokenType() == PhpTokenTypes.opQUEST)
		{
			builder.advanceLexer();
		}

		if(ClassReference.parseClassNameReference(builder, null, ClassReference.ALLOW_ARRAY) == null)
		{
			mark.error("Expected class reference");
			return false;
		}
		else
		{
			mark.done(PhpElementTypes.RETURN_TYPE);
			return true;
		}
	}
}
