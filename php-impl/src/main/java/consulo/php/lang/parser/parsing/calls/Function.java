package consulo.php.lang.parser.parsing.calls;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.classes.ClassReference;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

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
			return PhpElementTypes.FUNCTION_CALL;
		}
		variable.drop();

		if(builder.getTokenType() == IDENTIFIER || builder.getTokenType() == SLASH)
		{
			boolean slash = builder.getTokenType() == SLASH;

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
				rollback.drop();
				parseFunctionCallParameterList(builder);
			}
			else if(builder.compare(SCOPE_RESOLUTION))
			{
				rollback.rollbackTo();
				ClassReference.parse(builder);
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
			return PhpElementTypes.FUNCTION_CALL;
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
		paramList.done(PhpElementTypes.PARAMETER_LIST);

		builder.match(RPAREN);
	}


}
