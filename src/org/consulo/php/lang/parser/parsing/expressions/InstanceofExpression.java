package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.classes.ClassReference;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 16.12.2007 21:22:48
 */
public class InstanceofExpression implements PHPTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = UnaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(kwINSTANCEOF))
		{
			result = ClassReference.parseClassNameReference(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
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
