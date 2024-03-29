package consulo.php.impl.lang.parser.parsing.calls;

import consulo.language.parser.PsiBuilder;
import consulo.util.lang.StringUtil;
import consulo.language.ast.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 16.11.2007
 */
public class Function implements PhpTokenTypes
{

	//	function_call:
	//		IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects '('
	//			function_call_parameter_list ')'
	//		| variable_without_objects '(' function_call_parameter_list ')'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parseVariableWithoutObjects(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			variable.done(result);
			parseFunctionCallParameterList(builder);
			return PhpElementTypes.FUNCTION_REFERENCE;
		}
		variable.drop();

		if(builder.getTokenType() == IDENTIFIER || builder.getTokenType() == SLASH)
		{
			boolean slash = builder.getTokenType() == SLASH;

			CharSequence functionName = !slash ? builder.getTokenSequence() : null;

			PsiBuilder.Marker rollback = builder.mark();

			PsiBuilder.Marker referenceMark = builder.mark();
			if(slash)
			{
				builder.advanceLexer();

				if(builder.getTokenType() != IDENTIFIER)
				{
					referenceMark.drop();
					rollback.rollbackTo();

					return PhpElementTypes.EMPTY_INPUT;
				}
				else
				{
					builder.advanceLexer();
				}
			}
			else
			{
				builder.advanceLexer();
			}
			referenceMark.done(PhpElementTypes.METHOD_REFERENCE);

			if(builder.compare(LPAREN))
			{
				if(StringUtil.equals(functionName, "define"))
				{
					rollback.rollbackTo();

					PsiBuilder.Marker nextMarker = builder.mark();
					builder.advanceLexer(); // IDENTIFIER

					parseFunctionCallParameterList(builder);

					nextMarker.done(PhpElementTypes.FUNCTION_REFERENCE);

					return PhpElementTypes.DEFINE;
				}
				else
				{
					rollback.drop();

					parseFunctionCallParameterList(builder);
				}
			}
			else if(builder.compare(SCOPE_RESOLUTION))
			{
				rollback.rollbackTo();

				ClassReference.parseClassNameReference(builder, null, ClassReference.ALLOW_STATIC);
				builder.match(SCOPE_RESOLUTION);

				if(builder.compareAndEat(kwCLASS))
				{
					rollback.rollbackTo();
					return PhpElementTypes.EMPTY_INPUT;
				}

				if(builder.compareAndEat(IDENTIFIER))
				{
					if(builder.compare(LPAREN))
					{
						parseFunctionCallParameterList(builder);
						return PhpElementTypes.METHOD_REFERENCE;
					}
					else
					{
						rollback.rollbackTo();
						return PhpElementTypes.EMPTY_INPUT;
					}
				}
				else
				{
					variable = builder.mark();
					result = Variable.parseVariableWithoutObjects(builder);
					variable.done(result);
					parseFunctionCallParameterList(builder);
					return PhpElementTypes.METHOD_REFERENCE;
				}
			}
			else
			{
				rollback.rollbackTo();
				return PhpElementTypes.EMPTY_INPUT;
			}
			referenceMark.drop();
			return PhpElementTypes.FUNCTION_REFERENCE;
		}
		return PhpElementTypes.EMPTY_INPUT;
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
	public static void parseFunctionCallParameterList(PhpPsiBuilder builder)
	{
		builder.match(LPAREN);

		ParserPart functionParameter = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				if(builder.compareAndEat(opBIT_AND))
				{
					return Variable.parse(builder);
				}
				return Expression.parse(builder);
			}
		};

		PsiBuilder.Marker paramList = builder.mark();
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, functionParameter.parse(builder), functionParameter, false);
		if(builder.compareAndEat(opCOMMA))
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		paramList.done(PhpElementTypes.EXPRESSION_PARAMETER_LIST);

		builder.match(RPAREN);
	}
}
