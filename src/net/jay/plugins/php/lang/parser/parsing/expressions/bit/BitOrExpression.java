package net.jay.plugins.php.lang.parser.parsing.expressions.bit;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.AssignmentExpression;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class BitOrExpression implements PHPTokenTypes {

	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = BitXorExpression.parse(builder);
		if (result != PHPElementTypes.EMPTY_INPUT) {
			if (builder.compareAndEat(opBIT_OR)) {
				result = AssignmentExpression.parseWithoutPriority(builder);
				if (result == PHPElementTypes.EMPTY_INPUT) {
					result = BitXorExpression.parse(builder);
				}
				if (result == PHPElementTypes.EMPTY_INPUT) {
					builder.error(PHPParserErrors.expected("expression"));
				}
				PsiBuilder.Marker newMarker = marker.precede();
				marker.done(PHPElementTypes.BIT_EXPRESSION);
				result = PHPElementTypes.BIT_EXPRESSION;
				if (builder.compareAndEat(opBIT_OR)) {
					subParse(builder, newMarker);
				} else {
					newMarker.drop();
				}
			} else {
				marker.drop();
			}
		} else {
			marker.drop();
		}
		return result;
	}

	private static IElementType subParse(PHPPsiBuilder builder, PsiBuilder.Marker marker) {
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if (result == PHPElementTypes.EMPTY_INPUT) {
			result = BitXorExpression.parse(builder);
		}
		if (result == PHPElementTypes.EMPTY_INPUT) {
			builder.error(PHPParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PHPElementTypes.BIT_EXPRESSION);
		if (builder.compareAndEat(opBIT_OR)) {
			subParse(builder, newMarker);
		} else {
			newMarker.drop();
		}
		return PHPElementTypes.BIT_EXPRESSION;
	}
}
