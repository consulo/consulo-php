package net.jay.plugins.php.lang.parser.parsing.calls;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.classes.ClassReference;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 16.11.2007
 */
public class Function implements PHPTokenTypes {

	//	function_call:
	//		IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects '('
	//			function_call_parameter_list ')'
	//		| variable_without_objects '(' function_call_parameter_list ')'
	//	;
	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parseVariableWithoutObjects(builder);
		if (result != PHPElementTypes.EMPTY_INPUT) {
			variable.done(result);
			parseFunctionCallParameterList(builder);
			return PHPElementTypes.FUNCTION_CALL;
		}
		variable.drop();
		if (builder.compare(IDENTIFIER)) {
			PsiBuilder.Marker rollback = builder.mark();
			builder.advanceLexer();
			if (builder.compare(chLPAREN)) {
				rollback.drop();
				parseFunctionCallParameterList(builder);
			} else if (builder.compare(SCOPE_RESOLUTION)) {
				rollback.rollbackTo();
				ClassReference.parse(builder);
				builder.match(SCOPE_RESOLUTION);
				if (builder.compareAndEat(IDENTIFIER)) {
					if (builder.compare(chLPAREN)) {
						parseFunctionCallParameterList(builder);
            return PHPElementTypes.METHOD_REFERENCE;
          } else {
						rollback.rollbackTo();
						return PHPElementTypes.EMPTY_INPUT;
					}
				} else {
					variable = builder.mark();
					result = Variable.parseVariableWithoutObjects(builder);
					variable.done(result);
					parseFunctionCallParameterList(builder);
          return PHPElementTypes.METHOD_REFERENCE;
        }
			} else {
				rollback.rollbackTo();
				return PHPElementTypes.EMPTY_INPUT;
			}
			return PHPElementTypes.FUNCTION_CALL;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	function_call_parameter_list:
	//		non_empty_function_call_parameter_list
	//		| /* empty */
	//	;
	//
	//	non_empty_function_call_parameter_list:
	//		expr_without_variable
	//		| variable
	//		| '&' variable //write
	//		| non_empty_function_call_parameter_list ',' expr_without_variable
	//		| non_empty_function_call_parameter_list ',' variable
	//		| non_empty_function_call_parameter_list ',' '&' variable //write
	//	;
	public static void parseFunctionCallParameterList(PHPPsiBuilder builder) {
		builder.match(chLPAREN);

		ParserPart functionParameter = new ParserPart() {
			public IElementType parse(PHPPsiBuilder builder) {
				if (builder.compareAndEat(opBIT_AND)) {
					return Variable.parse(builder);
				}
				return Expression.parse(builder);
			}
		};

		PsiBuilder.Marker paramList = builder.mark();
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
			functionParameter.parse(builder),
			functionParameter,
			false);
    if (builder.compareAndEat(opCOMMA)) {
      builder.error(PHPParserErrors.expected("expression"));
    }
    paramList.done(PHPElementTypes.PARAMETER_LIST);

		builder.match(chRPAREN);
	}


}
