package consulo.php.impl.lang.parser.parsing.expressions;

import consulo.language.ast.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;

/**
 * @author jay
 * @time 16.12.2007 21:22:48
 */
public class InstanceofExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = UnaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(kwINSTANCEOF))
		{
			if(ClassReference.parseClassNameReference(builder, null, false, false) == null)
			{
				builder.error(PhpParserErrors.expected("class reference"));
			}
			marker.done(PhpElementTypes.INSTANCEOF_EXPRESSION);
			result = PhpElementTypes.INSTANCEOF_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
