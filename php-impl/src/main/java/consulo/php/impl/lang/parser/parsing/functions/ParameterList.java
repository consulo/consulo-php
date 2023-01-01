package consulo.php.impl.lang.parser.parsing.functions;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * @author markov
 * @date 14.10.2007
 */
public class ParameterList implements PhpTokenTypes
{
	public static IElementType parseFunctionParamList(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker parameterList = builder.mark();
		builder.match(LPAREN);
		ParserPart parameterParser = new Parameter();
		int result = ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, parameterParser.parse(builder), parameterParser, false);
		builder.match(RPAREN);
		parameterList.done(PhpElementTypes.FUNCTION_PARAMETER_LIST);
		return (result > 0) ? PhpElementTypes.FUNCTION_PARAMETER_LIST : PhpElementTypes.EMPTY_INPUT;
	}

	//	non_empty_parameter_list:
	//		optional_class_type VARIABLE_REFERENCE
	//		| optional_class_type '&' VARIABLE_REFERENCE
	//		| optional_class_type '&' VARIABLE_REFERENCE '=' static_scalar
	//		| optional_class_type VARIABLE_REFERENCE '=' static_scalar
	//		| non_empty_parameter_list ',' optional_class_type VARIABLE_REFERENCE
	//		| non_empty_parameter_list ',' optional_class_type '&' VARIABLE_REFERENCE
	//		| non_empty_parameter_list ',' optional_class_type '&' VARIABLE_REFERENCE '=' static_scalar
	//		| non_empty_parameter_list ',' optional_class_type VARIABLE_REFERENCE '=' static_scalar
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
			optional_class_type [opBIT_AND] [ELLIPSIS] VARIABLE_REFERENCE [opASGN static_scalar]
		;
	 */
	private static class Parameter implements ParserPart
	{
		@Override
		public IElementType parse(PhpPsiBuilder builder)
		{
			PsiBuilder.Marker parameter = builder.mark();

			if(builder.getTokenType() == opQUEST)
			{
				builder.advanceLexer();
			}

			if(!builder.compareAndEat(kwARRAY))
			{
				ClassReference.parse(builder);
			}

			builder.compareAndEat(opBIT_AND);
			builder.compareAndEat(ELLIPSIS);
			if(!builder.compareAndEat(VARIABLE))
			{
				parameter.rollbackTo();
				return PhpElementTypes.EMPTY_INPUT;
			}
			if(builder.compare(opASGN))
			{
				PsiBuilder.Marker defaultValue = builder.mark();
				builder.advanceLexer();
				if(Expression.parse(builder) == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("default value"));
				}
				defaultValue.done(PhpElementTypes.PARAMETER_DEFAULT_VALUE);
			}
			parameter.done(PhpElementTypes.PARAMETER);
			return PhpElementTypes.PARAMETER;
		}
	}

}
