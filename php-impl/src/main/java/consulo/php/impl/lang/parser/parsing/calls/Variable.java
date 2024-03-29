package consulo.php.impl.lang.parser.parsing.calls;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.11.2007
 */
public class Variable implements PhpTokenTypes
{

	public static TokenSet START_TOKENS = TokenSet.create(VARIABLE, DOLLAR, IDENTIFIER);

	//	variable:
	//		base_variable_with_function_calls ARROW object_property method_or_not variable_properties
	//		| base_variable_with_function_calls
	//	;

	//	variable_properties:
	//		variable_properties variable_property
	//		| /* empty */
	//	;
	//
	//	variable_property:
	//		ARROW object_property method_or_not
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker reference = builder.mark();
		IElementType result = parseBaseVariableOrFunctionCall(builder);
		while(builder.compare(ARROW))
		{
			reference.done(result);
			reference = reference.precede();
			builder.advanceLexer();
			result = parseObjectProperty(builder);
			IElementType method = parseMethodOrNot(builder);
			if(method != PhpElementTypes.EMPTY_INPUT)
			{
				result = method;
			}
		}

		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			while(builder.getTokenType() == LBRACKET)
			{
				reference.done(result);
				reference = reference.precede();

				builder.advanceLexer();

				if(builder.getTokenType() == RBRACKET)
				{
					builder.advanceLexer();
				}
				else
				{
					if(Expression.parse(builder) == PhpElementTypes.EMPTY_INPUT)
					{
						builder.error("Expression expected");
						break;
					}
					else
					{
						builder.compareAndEat(RBRACKET);
					}
				}

				result = PhpElementTypes.ARRAY_ACCESS_EXPRESSION;
			}
		}

		reference.drop();
		return result;
	}

	public static IElementType parseAssignable(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker reference = builder.mark();
		IElementType result = parseBaseVariable(builder);
		while(builder.compare(ARROW))
		{
			reference.done(result);
			reference = reference.precede();
			builder.advanceLexer();
			result = parseObjectProperty(builder);
		}
		reference.drop();
		return result;
	}

	//	method_or_not:
	//		'(' function_call_parameter_list ')'
	//		| /* empty */
	//	;
	private static IElementType parseMethodOrNot(PhpPsiBuilder builder)
	{
		if(builder.compare(LPAREN))
		{
			Function.parseFunctionCallParameterList(builder);
			return PhpElementTypes.METHOD_REFERENCE;
		}
		return PhpElementTypes.EMPTY_INPUT;
	}

	//	object_property:
	//		object_dim_list
	//		| variable_without_objects
	//	;
	public static IElementType parseObjectProperty(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker objectProperty = builder.mark();
		IElementType result = parseVariableWithoutObjects(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			objectProperty.drop();
			result = parseObjectDimList(builder);
		}
		else
		{
			objectProperty.done(result);
		}

		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("field name"));
		}
		return PhpElementTypes.FIELD_REFERENCE;
	}

	//	object_dim_list:
	//		object_dim_list '[' dim_offset ']'
	//		| object_dim_list '{' expr '}'
	//		| variable_name
	//	;
	private static IElementType parseObjectDimList(PhpPsiBuilder builder)
	{
		TokenSet tokens = TokenSet.create(LBRACE);
		PsiBuilder.Marker preceder = builder.mark();
		IElementType result = parseVariableName(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			preceder.drop();
			return result;
		}

		while(builder.compare(tokens))
		{
			preceder.done(result);
			preceder = preceder.precede();
			if(builder.compareAndEat(LBRACE))
			{
				Expression.parse(builder);
				builder.match(RBRACE);
				result = PhpElementTypes.ARRAY;
			}
			else
			{
				builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
			}
		}

		preceder.drop();
		return result;
	}

	//	variable_name:
	//		IDENTIFIER
	//		| '{' expr '}'
	//	;
	private static IElementType parseVariableName(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(IDENTIFIER))
		{
			return PhpElementTypes.FIELD_REFERENCE;
		}
		if(builder.compareAndEat(LBRACE))
		{
			Expression.parse(builder);
			builder.match(RBRACE);
			return PhpElementTypes.FIELD_REFERENCE;
		}
		return PhpElementTypes.EMPTY_INPUT;
	}

	//	base_variable_with_function_calls:
	//		base_variable
	//		| function_call
	//	;
	private static IElementType parseBaseVariableOrFunctionCall(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker rollback = builder.mark();
		IElementType result = parseBaseVariable(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			rollback.drop();
			result = Function.parse(builder);
		}
		else if(builder.compare(LPAREN))
		{
			rollback.rollbackTo();
			result = Function.parse(builder);
		}
		else
		{
			rollback.drop();
		}

		return result;
	}

	//	base_variable:
	//		reference_variable
	//		| simple_indirect_reference reference_variable
	//		| static_member
	//	;
	public static IElementType parseBaseVariable(PhpPsiBuilder builder)
	{
		IElementType result = parseVariableWithoutObjects(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = parseStaticMember(builder);
		}
		return result;
	}

	//	static_member:
	//		fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects
	//	;
	private static IElementType parseStaticMember(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker rollback = builder.mark();
		PsiBuilder.Marker var = builder.mark();
		if(ClassReference.parseClassNameReference(builder, null, ClassReference.ALLOW_STATIC) != PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(SCOPE_RESOLUTION))
			{
				IElementType result = parseVariableWithoutObjects(builder, var, PhpElementTypes.FIELD_REFERENCE);
				if(result != PhpElementTypes.EMPTY_INPUT)
				{
					rollback.drop();
					if(result == PhpElementTypes.VARIABLE_REFERENCE)
					{
						return PhpElementTypes.FIELD_REFERENCE;
					}
					return result;
				}
			}
		}
		rollback.rollbackTo();
		return PhpElementTypes.EMPTY_INPUT;
	}

	public static IElementType parseVariableWithoutObjects(PhpPsiBuilder builder)
	{
		return parseVariableWithoutObjects(builder, null, null);
	}

	//	variable_without_objects:
	//		reference_variable
	//		| simple_indirect_reference reference_variable
	//	;
	public static IElementType parseVariableWithoutObjects(PhpPsiBuilder builder, PsiBuilder.Marker variableMarker, IElementType outerElement)
	{
		IElementType result = parseReferenceVariable(builder, variableMarker, outerElement);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compare(DOLLAR))
			{
				parseSimpleIndirectReference(builder);
				PsiBuilder.Marker var = builder.mark();
				IElementType referenceVariable = parseReferenceVariable(builder);
				if(referenceVariable != PhpElementTypes.EMPTY_INPUT)
				{
					var.done(referenceVariable);
				}
				else
				{
					var.drop();
				}
				return PhpElementTypes.VARIABLE_REFERENCE;
			}
		}
		return result;
	}

	//	simple_indirect_reference:
	//		'$'
	//		| simple_indirect_reference '$'
	//	;
	private static void parseSimpleIndirectReference(PhpPsiBuilder builder)
	{
		while(builder.compare(DOLLAR))
		{
			PsiBuilder.Marker rollback = builder.mark();
			builder.advanceLexer();
			if(builder.compare(LBRACE))
			{
				rollback.rollbackTo();
				break;
			}
			else
			{
				rollback.drop();
			}
		}
	}

	private static IElementType parseReferenceVariable(PhpPsiBuilder builder)
	{
		return parseReferenceVariable(builder, null, null);
	}

	//	reference_variable:
	//		reference_variable '[' dim_offset ']'
	//		| reference_variable '{' expr '}'
	//		| compound_variable
	//	;
	private static IElementType parseReferenceVariable(PhpPsiBuilder builder, PsiBuilder.Marker preceder, IElementType outerElement)
	{
		TokenSet tokens = TokenSet.create(LBRACE, LBRACKET);
		if(preceder == null)
		{
			preceder = builder.mark();
		}
		IElementType result = parseCompoundVariable(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			preceder.drop();
			return result;
		}

		while(builder.compare(tokens))
		{
			preceder.done(outerElement == null ? result : outerElement);
			if(outerElement != null)
			{
				outerElement = null;
			}
			preceder = preceder.precede();
			if(builder.compareAndEat(LBRACE))
			{
				Expression.parse(builder);
				builder.match(RBRACE);
				result = PhpElementTypes.ARRAY;
			}
			else if(builder.compareAndEat(LBRACKET))
			{
				PsiBuilder.Marker arrayIndex = builder.mark();
				parseDimOffset(builder);
				arrayIndex.done(PhpElementTypes.ARRAY_INDEX);
				builder.match(RBRACKET);
				result = PhpElementTypes.ARRAY;
			}
			else
			{
				builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
			}
		}

		preceder.drop();
		return result;
	}

	//	compound_variable:
	//		VARIABLE_REFERENCE
	//		| '$' '{' expr '}'
	//	;
	private static IElementType parseCompoundVariable(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(VARIABLE))
		{
			return PhpElementTypes.VARIABLE_REFERENCE;
		}
		PsiBuilder.Marker rollback = builder.mark();
		if(builder.compareAndEat(DOLLAR))
		{
			if(builder.compareAndEat(LBRACE))
			{
				rollback.drop();
				Expression.parse(builder);
				builder.match(RBRACE);
				return PhpElementTypes.VARIABLE_REFERENCE;
			}
		}
		rollback.rollbackTo();
		return PhpElementTypes.EMPTY_INPUT;
	}

	//	dim_offset:
	//		/* empty */
	//		| expr
	//	;
	private static void parseDimOffset(PhpPsiBuilder builder)
	{
		Expression.parse(builder);
	}

}
