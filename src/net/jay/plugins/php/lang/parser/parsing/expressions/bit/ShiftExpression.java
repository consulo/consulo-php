package net.jay.plugins.php.lang.parser.parsing.expressions.bit;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.math.AdditiveExpression;
import net.jay.plugins.php.lang.parser.parsing.expressions.AssignmentExpression;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 16.12.2007
 */
public class ShiftExpression implements PHPTokenTypes {

	private static TokenSet SHIFT_OPERATORS = TokenSet.create(
		opSHIFT_LEFT, opSHIFT_RIGHT
	);

	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = AdditiveExpression.parse(builder);
		if (result != PHPElementTypes.EMPTY_INPUT && builder.compareAndEat(SHIFT_OPERATORS)) {
			result = AssignmentExpression.parseWithoutPriority(builder);
			if (result == PHPElementTypes.EMPTY_INPUT) {
				result = AdditiveExpression.parse(builder);
			}
			if (result == PHPElementTypes.EMPTY_INPUT) {
				builder.error(PHPParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PHPElementTypes.SHIFT_EXPRESSION);
			result = PHPElementTypes.SHIFT_EXPRESSION;
			if (builder.compareAndEat(SHIFT_OPERATORS)) {
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
			result = AdditiveExpression.parse(builder);
		}
		if (result == PHPElementTypes.EMPTY_INPUT) {
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.SHIFT_EXPRESSION);
		if (builder.compareAndEat(SHIFT_OPERATORS)) {
			subParse(builder, newMarker);
		} else {
			newMarker.drop();
		}
		return PHPElementTypes.SHIFT_EXPRESSION;
	}
}
