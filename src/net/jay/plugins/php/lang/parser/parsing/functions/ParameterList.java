package net.jay.plugins.php.lang.parser.parsing.functions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.classes.ClassReference;
import net.jay.plugins.php.lang.parser.parsing.expressions.StaticScalar;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;

/**
 * @author markov
 * @date 14.10.2007
 */
public class ParameterList implements PHPTokenTypes {

	//	parameter_list:
	//		non_empty_parameter_list
	//		| /* empty */
	//	;
	//

	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker parameterList = builder.mark();
		ParserPart parameterParser = new Parameter();
		int result = ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, parameterParser.parse(builder), parameterParser, false);
		parameterList.done(PHPElementTypes.PARAMETER_LIST);
		return (result > 0) ? PHPElementTypes.PARAMETER_LIST : PHPElementTypes.EMPTY_INPUT;
	}

	//	non_empty_parameter_list:
	//		optional_class_type VARIABLE
	//		| optional_class_type '&' VARIABLE
	//		| optional_class_type '&' VARIABLE '=' static_scalar
	//		| optional_class_type VARIABLE '=' static_scalar
	//		| non_empty_parameter_list ',' optional_class_type VARIABLE
	//		| non_empty_parameter_list ',' optional_class_type '&' VARIABLE
	//		| non_empty_parameter_list ',' optional_class_type '&' VARIABLE '=' static_scalar
	//		| non_empty_parameter_list ',' optional_class_type VARIABLE '=' static_scalar
	//	;
	//
	//	optional_class_type:
	//		/* empty */
	//		| IDENTIFIER
	//		| kwARRAY
	//	;

	/*
		non_empty_parameter_list:
			parameter | non_empty_parameter_list ',' parameter
		;

		parameter:
			optional_class_type [opBIT_AND] VARIABLE [opASGN static_scalar]
		;
	 */
	private static class Parameter implements ParserPart {

		public IElementType parse(PHPPsiBuilder builder) {
			PsiBuilder.Marker parameter = builder.mark();
			if (ClassReference.parse(builder) == PHPElementTypes.EMPTY_INPUT) {
				builder.compareAndEat(kwARRAY);
			}
			builder.compareAndEat(opBIT_AND);
			if (!builder.compareAndEat(VARIABLE)) {
				parameter.rollbackTo();
				return PHPElementTypes.EMPTY_INPUT;
			}
			if (builder.compare(opASGN)) {
				PsiBuilder.Marker defaultValue = builder.mark();
				builder.advanceLexer();
				if (StaticScalar.parse(builder) == PHPElementTypes.EMPTY_INPUT) {
					builder.error(PHPParserErrors.expected("default value"));
				}
				defaultValue.done(PHPElementTypes.PARAMETER_DEFAULT_VALUE);
			}
			parameter.done(PHPElementTypes.PARAMETER);
			return PHPElementTypes.PARAMETER;
		}
	}

}
