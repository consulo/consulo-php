package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.classes.ClassReference;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 16.12.2007 21:22:48
 */
public class InstanceofExpression implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = UnaryExpression.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(kwINSTANCEOF))
		{
			result = ClassReference.parseClassNameReference(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("class reference"));
			}
			marker.done(PHPElementTypes.INSTANCEOF_EXPRESSION);
			result = PHPElementTypes.INSTANCEOF_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
