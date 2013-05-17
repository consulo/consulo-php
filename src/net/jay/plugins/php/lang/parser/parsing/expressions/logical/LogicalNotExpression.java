package net.jay.plugins.php.lang.parser.parsing.expressions.logical;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.InstanceofExpression;
import net.jay.plugins.php.lang.parser.parsing.expressions.AssignmentExpression;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.PsiBuilder;

/**
 * @author jay
 * @time 16.12.2007 21:00:36
 */
public class LogicalNotExpression implements PHPTokenTypes {

	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		if (builder.compareAndEat(opNOT)) {
			parse(builder);
			marker.done(PHPElementTypes.UNARY_EXPRESSION);
			return PHPElementTypes.UNARY_EXPRESSION;
		} else {
			marker.drop();
			IElementType result = AssignmentExpression.parseWithoutPriority(builder);
			if (result == PHPElementTypes.EMPTY_INPUT) {
				result = InstanceofExpression.parse(builder);
			}
			return result;
		}
	}
}
