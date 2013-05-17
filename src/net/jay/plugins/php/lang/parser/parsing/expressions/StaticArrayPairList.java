package net.jay.plugins.php.lang.parser.parsing.expressions;

import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.PsiBuilder;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticArrayPairList {

	//	static_array_pair_list:
	//		/* empty */
	//		| non_empty_static_array_pair_list possible_comma
	//	;
	//
	//	possible_comma:
	//		/* empty */
	//		| ','
	//	;
	//
	//	non_empty_static_array_pair_list:
	//		non_empty_static_array_pair_list ',' static_scalar opHASH_ARRAY static_scalar
	//		| non_empty_static_array_pair_list ',' static_scalar
	//		| static_scalar opHASH_ARRAY static_scalar
	//		| static_scalar
	//	;
	public static IElementType parse(PHPPsiBuilder builder) {
		ParserPart parser = new ParserPart() {

			public IElementType parse(PHPPsiBuilder builder) {
				PsiBuilder.Marker staticArrayPair = builder.mark();
				IElementType result = StaticScalar.parse(builder);
				if (result != PHPElementTypes.EMPTY_INPUT) {
					if (builder.compare(PHPTokenTypes.opHASH_ARRAY)) {
						staticArrayPair.done(PHPElementTypes.ARRAY_KEY);
						builder.advanceLexer();
            staticArrayPair = builder.mark();
            if (StaticScalar.parse(builder) == PHPElementTypes.EMPTY_INPUT) {
							builder.error(PHPParserErrors.expected("static value"));
						}
					}
					staticArrayPair.done(PHPElementTypes.ARRAY_VALUE);
				} else {
					staticArrayPair.rollbackTo();
				}
				return result;
			}
		};

		IElementType result = parser.parse(builder);
		if (result != PHPElementTypes.EMPTY_INPUT) {
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
				result,
				parser,
				true);
		}
		return result;
	}
}
