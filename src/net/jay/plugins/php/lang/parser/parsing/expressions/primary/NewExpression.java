package net.jay.plugins.php.lang.parser.parsing.expressions.primary;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.calls.Function;
import net.jay.plugins.php.lang.parser.parsing.classes.ClassReference;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 21.12.2007 16:42:00
 */
public class NewExpression implements PHPTokenTypes
{

	// kwNEW class_name_reference ctor_arguments
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker newExpr = builder.mark();
		if(builder.compareAndEat(kwNEW))
		{
			IElementType result = ClassReference.parseClassNameReference(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.expected("class name"));
			}
			if(builder.compare(chLPAREN))
			{
				Function.parseFunctionCallParameterList(builder);
			}
			newExpr.done(PHPElementTypes.NEW_EXPRESSION);
			return PHPElementTypes.NEW_EXPRESSION;
		}
		newExpr.drop();
		return PHPElementTypes.EMPTY_INPUT;
	}
}
