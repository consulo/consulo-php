package net.jay.plugins.php.lang.parser.parsing.calls;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.classes.ClassReference;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.11.2007
 */
public class Variable implements PHPTokenTypes
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
	public static IElementType parse(PHPPsiBuilder builder)
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
			if(method != PHPElementTypes.EMPTY_INPUT)
			{
				result = method;
			}
		}
		reference.drop();
		return result;
	}

	public static IElementType parseAssignable(PHPPsiBuilder builder)
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
	private static IElementType parseMethodOrNot(PHPPsiBuilder builder)
	{
		if(builder.compare(chLPAREN))
		{
			Function.parseFunctionCallParameterList(builder);
			return PHPElementTypes.METHOD_REFERENCE;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	object_property:
	//		object_dim_list
	//		| variable_without_objects
	//	;
	public static IElementType parseObjectProperty(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker objectProperty = builder.mark();
		IElementType result = parseVariableWithoutObjects(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			objectProperty.drop();
			result = parseObjectDimList(builder);
		}
		else
		{
			objectProperty.done(result);
		}

		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("field name"));
		}
		return PHPElementTypes.FIELD_REFERENCE;
	}

	//	object_dim_list:
	//		object_dim_list '[' dim_offset ']'
	//		| object_dim_list '{' expr '}'
	//		| variable_name
	//	;
	private static IElementType parseObjectDimList(PHPPsiBuilder builder)
	{
		TokenSet tokens = TokenSet.create(chLBRACE, chLBRACKET);
		PsiBuilder.Marker preceder = builder.mark();
		IElementType result = parseVariableName(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			preceder.drop();
			return result;
		}

		while(builder.compare(tokens))
		{
			preceder.done(result);
			preceder = preceder.precede();
			if(builder.compareAndEat(chLBRACE))
			{
				Expression.parse(builder);
				builder.match(chRBRACE);
				result = PHPElementTypes.ARRAY;
			}
			else if(builder.compareAndEat(chLBRACKET))
			{
				PsiBuilder.Marker arrayIndex = builder.mark();
				parseDimOffset(builder);
				arrayIndex.done(PHPElementTypes.ARRAY_INDEX);
				builder.match(chRBRACKET);
				result = PHPElementTypes.ARRAY;
			}
			else
			{
				builder.error(PHPParserErrors.unexpected(builder.getTokenType()));
			}
		}

		preceder.drop();
		return result;
	}

	//	variable_name:
	//		IDENTIFIER
	//		| '{' expr '}'
	//	;
	private static IElementType parseVariableName(PHPPsiBuilder builder)
	{
		if(builder.compareAndEat(IDENTIFIER))
		{
			return PHPElementTypes.FIELD_REFERENCE;
		}
		if(builder.compareAndEat(chLBRACE))
		{
			Expression.parse(builder);
			builder.match(chRBRACE);
			return PHPElementTypes.FIELD_REFERENCE;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	base_variable_with_function_calls:
	//		base_variable
	//		| function_call
	//	;
	private static IElementType parseBaseVariableOrFunctionCall(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker rollback = builder.mark();
		IElementType result = parseBaseVariable(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			rollback.drop();
			result = Function.parse(builder);
		}
		else if(builder.compare(chLPAREN))
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
	public static IElementType parseBaseVariable(PHPPsiBuilder builder)
	{
		IElementType result = parseVariableWithoutObjects(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			result = parseStaticMember(builder);
		}
		return result;
	}

	//	static_member:
	//		fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects
	//	;
	private static IElementType parseStaticMember(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker rollback = builder.mark();
		PsiBuilder.Marker var = builder.mark();
		if(ClassReference.parse(builder) != PHPElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(SCOPE_RESOLUTION))
			{
				IElementType result = parseVariableWithoutObjects(builder, var, PHPElementTypes.FIELD_REFERENCE);
				if(result != PHPElementTypes.EMPTY_INPUT)
				{
					rollback.drop();
					if(result == PHPElementTypes.VARIABLE_REFERENCE)
					{
						return PHPElementTypes.FIELD_REFERENCE;
					}
					return result;
				}
			}
		}
		rollback.rollbackTo();
		return PHPElementTypes.EMPTY_INPUT;
	}

	public static IElementType parseVariableWithoutObjects(PHPPsiBuilder builder)
	{
		return parseVariableWithoutObjects(builder, null, null);
	}

	//	variable_without_objects:
	//		reference_variable
	//		| simple_indirect_reference reference_variable
	//	;
	public static IElementType parseVariableWithoutObjects(PHPPsiBuilder builder, PsiBuilder.Marker variableMarker, IElementType outerElement)
	{
		IElementType result = parseReferenceVariable(builder, variableMarker, outerElement);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			if(builder.compare(DOLLAR))
			{
				parseSimpleIndirectReference(builder);
				PsiBuilder.Marker var = builder.mark();
				IElementType referenceVariable = parseReferenceVariable(builder);
				if(referenceVariable != PHPElementTypes.EMPTY_INPUT)
				{
					var.done(referenceVariable);
				}
				else
				{
					var.drop();
				}
				return PHPElementTypes.VARIABLE_REFERENCE;
			}
		}
		return result;
	}

	//	simple_indirect_reference:
	//		'$'
	//		| simple_indirect_reference '$'
	//	;
	private static void parseSimpleIndirectReference(PHPPsiBuilder builder)
	{
		while(builder.compare(DOLLAR))
		{
			PsiBuilder.Marker rollback = builder.mark();
			builder.advanceLexer();
			if(builder.compare(chLBRACE))
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

	private static IElementType parseReferenceVariable(PHPPsiBuilder builder)
	{
		return parseReferenceVariable(builder, null, null);
	}

	//	reference_variable:
	//		reference_variable '[' dim_offset ']'
	//		| reference_variable '{' expr '}'
	//		| compound_variable
	//	;
	private static IElementType parseReferenceVariable(PHPPsiBuilder builder, PsiBuilder.Marker preceder, IElementType outerElement)
	{
		TokenSet tokens = TokenSet.create(chLBRACE, chLBRACKET);
		if(preceder == null)
		{
			preceder = builder.mark();
		}
		IElementType result = parseCompoundVariable(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
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
			if(builder.compareAndEat(chLBRACE))
			{
				Expression.parse(builder);
				builder.match(chRBRACE);
				result = PHPElementTypes.ARRAY;
			}
			else if(builder.compareAndEat(chLBRACKET))
			{
				PsiBuilder.Marker arrayIndex = builder.mark();
				parseDimOffset(builder);
				arrayIndex.done(PHPElementTypes.ARRAY_INDEX);
				builder.match(chRBRACKET);
				result = PHPElementTypes.ARRAY;
			}
			else
			{
				builder.error(PHPParserErrors.unexpected(builder.getTokenType()));
			}
		}

		preceder.drop();
		return result;
	}

	//	compound_variable:
	//		VARIABLE_REFERENCE
	//		| '$' '{' expr '}'
	//	;
	private static IElementType parseCompoundVariable(PHPPsiBuilder builder)
	{
		if(builder.compareAndEat(VARIABLE))
		{
			return PHPElementTypes.VARIABLE_REFERENCE;
		}
		PsiBuilder.Marker rollback = builder.mark();
		if(builder.compareAndEat(DOLLAR))
		{
			if(builder.compareAndEat(chLBRACE))
			{
				rollback.drop();
				Expression.parse(builder);
				builder.match(chRBRACE);
				return PHPElementTypes.VARIABLE_REFERENCE;
			}
		}
		rollback.rollbackTo();
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	dim_offset:
	//		/* empty */
	//		| expr
	//	;
	private static void parseDimOffset(PHPPsiBuilder builder)
	{
		Expression.parse(builder);
	}

}
