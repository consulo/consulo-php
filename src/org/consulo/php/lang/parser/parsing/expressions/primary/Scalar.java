package org.consulo.php.lang.parser.parsing.expressions.primary;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.parsing.classes.StaticClassConstant;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.parsing.expressions.StaticScalar;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 20.12.2007 21:45:27
 */
public class Scalar implements PHPTokenTypes
{

	//	scalar:
	//		IDENTIFIER
	//		| VARIABLE_NAME
	//		| class_constant
	//		| common_scalar
	//		| '"' encaps_list '"'
	//		| HEREDOC_START encaps_list HEREDOC_END
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(builder.compare(VARIABLE_NAME))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			marker.done(PhpElementTypes.VARIABLE_REFERENCE);
			return PhpElementTypes.VARIABLE_REFERENCE;
		}
		IElementType result = StaticClassConstant.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		if(builder.compare(IDENTIFIER))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			marker.done(PhpElementTypes.CONSTANT);
			return PhpElementTypes.CONSTANT;
		}
		if(builder.compare(HEREDOC_START))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseEncapsList(builder);
			builder.match(HEREDOC_END);
			marker.done(PhpElementTypes.HEREDOC);
			return PhpElementTypes.HEREDOC;
		}
		if(builder.compare(chDOUBLE_QUOTE))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseEncapsList(builder);
			builder.match(chDOUBLE_QUOTE);
			marker.done(PhpElementTypes.STRING);
			return PhpElementTypes.STRING;
		}
		result = StaticScalar.parseCommonScalar(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		return PhpElementTypes.EMPTY_INPUT;
	}

	//	encaps_list:
	//		encaps_list encaps_var
	//		| encaps_list HEREDOC_CONTENTS
	//		| encaps_list STRING_LITERAL
	//		| encaps_list EXEC_COMMAND
	//		| /* empty */
	//
	//	;
	public static void parseEncapsList(PhpPsiBuilder builder)
	{
		TokenSet contents = TokenSet.create(HEREDOC_CONTENTS, STRING_LITERAL, EXEC_COMMAND);
		while(true)
		{
			if(!builder.compareAndEat(contents))
			{
				if(parseEncapsVar(builder) == PhpElementTypes.EMPTY_INPUT)
				{
					break;
				}
			}
		}
	}

	//encaps_var:
	//	VARIABLE_REFERENCE
	//	| VARIABLE_REFERENCE '[' encaps_var_offset ']'
	//	| VARIABLE_REFERENCE ARROW IDENTIFIER
	//	| DOLLAR_LBRACE expr '}'
	//	| DOLLAR_LBRACE VARIABLE_NAME '[' expr ']' '}'
	//	| chLBRACE variable '}'
	//;
	private static IElementType parseEncapsVar(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(VARIABLE))
		{
			if(builder.compareAndEat(chLBRACKET))
			{
				IElementType result = parseEncapsVarOffset(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("array index"));
				}
				builder.match(chRBRACKET);
				marker.done(PhpElementTypes.ARRAY);
				return PhpElementTypes.ARRAY;
			}
			marker.done(PhpElementTypes.VARIABLE_REFERENCE);
			if(builder.compareAndEat(ARROW))
			{
				marker = marker.precede();
				builder.match(IDENTIFIER);
				marker.done(PhpElementTypes.FIELD_REFERENCE);
				return PhpElementTypes.FIELD_REFERENCE;
			}
			return PhpElementTypes.VARIABLE_REFERENCE;
		}
		if(builder.compareAndEat(chLBRACE))
		{
			marker.drop();
			IElementType result = Variable.parse(builder);
			builder.match(chRBRACE);
			return result;
		}
		if(builder.compareAndEat(DOLLAR_LBRACE))
		{
			PsiBuilder.Marker rollback = builder.mark();
			if(builder.compareAndEat(VARIABLE_NAME) && builder.compare(chLBRACKET))
			{
				rollback.drop();
				builder.match(chLBRACKET);
				PsiBuilder.Marker index = builder.mark();
				IElementType result = Expression.parse(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("expression"));
				}
				index.done(PhpElementTypes.ARRAY_INDEX);
				builder.match(chRBRACKET);
				marker.done(PhpElementTypes.ARRAY);
				builder.match(chRBRACE);
				return PhpElementTypes.ARRAY;
			}
			rollback.rollbackTo();
			PsiBuilder.Marker varname = builder.mark();
			IElementType result = Expression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				if(!builder.compareAndEat(VARIABLE_NAME))
				{
					builder.error(PhpParserErrors.expected("variable name"));
				}
			}
			varname.done(PhpElementTypes.VARIABLE_NAME);
			marker.done(PhpElementTypes.VARIABLE_REFERENCE);
			builder.match(chRBRACE);
			return PhpElementTypes.VARIABLE_REFERENCE;
		}
		marker.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}

	//	encaps_var_offset:
	//		IDENTIFIER
	//		| VARIABLE_OFFSET_NUMBER
	//		| VARIABLE_REFERENCE
	//	;
	private static IElementType parseEncapsVarOffset(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(!builder.compareAndEat(IDENTIFIER))
		{
			if(!builder.compareAndEat(VARIABLE_OFFSET_NUMBER))
			{
				if(!builder.compareAndEat(VARIABLE))
				{
					marker.drop();
					return PhpElementTypes.EMPTY_INPUT;
				}
			}
		}
		marker.done(PhpElementTypes.ARRAY_INDEX);
		return PhpElementTypes.ARRAY_INDEX;
	}
}
