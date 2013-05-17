package net.jay.plugins.php.lang.parser.parsing.expressions.math;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.AssignmentExpression;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;

/**
 * @author jay
 * @time 16.12.2007 20:43:48
 */
public class AdditiveExpression implements PHPTokenTypes {

	private static TokenSet ADDITIVE_OPERATORS = TokenSet.create(
		opPLUS, opMINUS, opCONCAT
	);

	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = MultiplicativeExpression.parse(builder);
		if (result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(ADDITIVE_OPERATORS)) {
			result = AssignmentExpression.parseWithoutPriority(builder);
			if (result == PHPElementTypes.EMPTY_INPUT) {
				result = MultiplicativeExpression.parse(builder);
			}
			if (result == PHPElementTypes.EMPTY_INPUT) {
				builder.error(PHPParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PHPElementTypes.ADDITIVE_EXPRESSION);
			result = PHPElementTypes.ADDITIVE_EXPRESSION;
			if (builder.compareAndEat(ADDITIVE_OPERATORS)) {
				subParse(builder, newMarker);
			} else {
				newMarker.drop();
			}
		} else {
			marker.drop();
		}
		return result;
	}

	private static IElementType subParse(PHPPsiBuilder builder, PsiBuilder.Marker marker) {
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if (result == PHPElementTypes.EMPTY_INPUT) {
			result = MultiplicativeExpression.parse(builder);
		}
		if (result == PHPElementTypes.EMPTY_INPUT) {
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.ADDITIVE_EXPRESSION);
		if (builder.compareAndEat(ADDITIVE_OPERATORS)) {
			subParse(builder, newMarker);
		} else {
			newMarker.drop();
		}
		return PHPElementTypes.ADDITIVE_EXPRESSION;
	}

}
