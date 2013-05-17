package net.jay.plugins.php.lang.parser.parsing.expressions;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.classes.StaticClassConstant;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticScalar implements PHPTokenTypes {

	//	static_scalar: /* compile-time evaluated scalars */
	//		common_scalar
	//		| IDENTIFIER
	//		| opPLUS static_scalar
	//		| opMINUS static_scalar
	//		| kwARRAY '(' static_array_pair_list ')'
	//		| static_class_constant
	//	;
	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker staticScalar = builder.mark();
		IElementType result = StaticClassConstant.parse(builder);
		if (result == PHPElementTypes.EMPTY_INPUT) {
			if (builder.compareAndEat(opPLUS) || builder.compareAndEat(opMINUS)) {
				parse(builder);
				staticScalar.done(PHPElementTypes.STATIC_SCALAR);
				result = PHPElementTypes.STATIC_SCALAR;
			} else if (builder.compareAndEat(kwARRAY)) {
				builder.match(chLPAREN);
				StaticArrayPairList.parse(builder);
				builder.match(chRPAREN);
				staticScalar.done(PHPElementTypes.ARRAY);
				result = PHPElementTypes.ARRAY;
			} else if (builder.compareAndEat(IDENTIFIER)) {
				staticScalar.done(PHPElementTypes.CONSTANT);
				result = PHPElementTypes.CONSTANT;
			} else {
				result = parseCommonScalar(builder);
				if (result != PHPElementTypes.EMPTY_INPUT) {
					staticScalar.drop();
				}
			}
		} else {
      staticScalar.drop();
    }
		if (result == PHPElementTypes.EMPTY_INPUT) {
			staticScalar.rollbackTo();
		}
		return result;
	}

	//	common_scalar:
	//		INTEGER_LITERAL
	//		| FLOAT_LITERAL
	//		| STRING_LITERAL
	//		| CONST_LINE
	//		| CONST_FILE
	//		| CONST_CLASS
	//		| CONST_METHOD
	//		| CONST_FUNCTION
	//	;
	/**
	 * We can use STRING_LITERAL here because it means
	 * that the string has no variables in it
	 *
	 * @param builder current PsiBuilder wrapper
	 * @return EMPTY_INPUT on empty input, COMMON_SCALAR on success
	 */
	public static IElementType parseCommonScalar(PHPPsiBuilder builder) {
		if (builder.compare(tsCOMMON_SCALARS)) {
			PsiBuilder.Marker scalar = builder.mark();
			IElementType type = builder.getTokenType();
			builder.advanceLexer();
			if (TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL).contains(type)) {
				scalar.done(PHPElementTypes.NUMBER);
			} else if (TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE).contains(type)) {
				scalar.done(PHPElementTypes.STRING);
			} else {
				scalar.done(PHPElementTypes.CONSTANT);
			}
			return PHPElementTypes.COMMON_SCALAR;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

}

