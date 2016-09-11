package consulo.php.lang.parser.parsing.expressions.primary;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Function;
import consulo.php.lang.parser.parsing.classes.ClassReference;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 21.12.2007 16:42:00
 */
public class NewExpression implements PhpTokenTypes
{

	// kwNEW class_name_reference ctor_arguments
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker newExpr = builder.mark();
		if(builder.compareAndEat(kwNEW))
		{
			if(ClassReference.parseClassNameReference(builder, null, true, false, false) == null)
			{
				builder.error(PhpParserErrors.expected("class name"));
			}
			if(builder.compare(chLPAREN))
			{
				Function.parseFunctionCallParameterList(builder);
			}
			newExpr.done(PhpElementTypes.NEW_EXPRESSION);
			return PhpElementTypes.NEW_EXPRESSION;
		}
		newExpr.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}
}
