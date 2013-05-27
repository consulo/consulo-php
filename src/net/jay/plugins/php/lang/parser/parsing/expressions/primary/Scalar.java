package net.jay.plugins.php.lang.parser.parsing.expressions.primary;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.calls.Variable;
import net.jay.plugins.php.lang.parser.parsing.classes.StaticClassConstant;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.parsing.expressions.StaticScalar;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

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
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(builder.compare(VARIABLE_NAME))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			marker.done(PHPElementTypes.VARIABLE);
			return PHPElementTypes.VARIABLE;
		}
		IElementType result = StaticClassConstant.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		if(builder.compare(IDENTIFIER))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			marker.done(PHPElementTypes.CONSTANT);
			return PHPElementTypes.CONSTANT;
		}
		if(builder.compare(HEREDOC_START))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseEncapsList(builder);
			builder.match(HEREDOC_END);
			marker.done(PHPElementTypes.HEREDOC);
			return PHPElementTypes.HEREDOC;
		}
		if(builder.compare(chDOUBLE_QUOTE))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			parseEncapsList(builder);
			builder.match(chDOUBLE_QUOTE);
			marker.done(PHPElementTypes.STRING);
			return PHPElementTypes.STRING;
		}
		result = StaticScalar.parseCommonScalar(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	encaps_list:
	//		encaps_list encaps_var
	//		| encaps_list HEREDOC_CONTENTS
	//		| encaps_list STRING_LITERAL
	//		| encaps_list EXEC_COMMAND
	//		| /* empty */
	//
	//	;
	public static void parseEncapsList(PHPPsiBuilder builder)
	{
		TokenSet contents = TokenSet.create(HEREDOC_CONTENTS, STRING_LITERAL, EXEC_COMMAND);
		while(true)
		{
			if(!builder.compareAndEat(contents))
			{
				if(parseEncapsVar(builder) == PHPElementTypes.EMPTY_INPUT)
				{
					break;
				}
			}
		}
	}

	//encaps_var:
	//	VARIABLE
	//	| VARIABLE '[' encaps_var_offset ']'
	//	| VARIABLE ARROW IDENTIFIER
	//	| DOLLAR_LBRACE expr '}'
	//	| DOLLAR_LBRACE VARIABLE_NAME '[' expr ']' '}'
	//	| chLBRACE variable '}'
	//;
	private static IElementType parseEncapsVar(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(VARIABLE))
		{
			if(builder.compareAndEat(chLBRACKET))
			{
				IElementType result = parseEncapsVarOffset(builder);
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.expected("array index"));
				}
				builder.match(chRBRACKET);
				marker.done(PHPElementTypes.ARRAY);
				return PHPElementTypes.ARRAY;
			}
			marker.done(PHPElementTypes.VARIABLE);
			if(builder.compareAndEat(ARROW))
			{
				marker = marker.precede();
				builder.match(IDENTIFIER);
				marker.done(PHPElementTypes.FIELD_REFERENCE);
				return PHPElementTypes.FIELD_REFERENCE;
			}
			return PHPElementTypes.VARIABLE;
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
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.expected("expression"));
				}
				index.done(PHPElementTypes.ARRAY_INDEX);
				builder.match(chRBRACKET);
				marker.done(PHPElementTypes.ARRAY);
				builder.match(chRBRACE);
				return PHPElementTypes.ARRAY;
			}
			rollback.rollbackTo();
			PsiBuilder.Marker varname = builder.mark();
			IElementType result = Expression.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				if(!builder.compareAndEat(VARIABLE_NAME))
				{
					builder.error(PHPParserErrors.expected("variable name"));
				}
			}
			varname.done(PHPElementTypes.VARIABLE_NAME);
			marker.done(PHPElementTypes.VARIABLE);
			builder.match(chRBRACE);
			return PHPElementTypes.VARIABLE;
		}
		marker.drop();
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	encaps_var_offset:
	//		IDENTIFIER
	//		| VARIABLE_OFFSET_NUMBER
	//		| VARIABLE
	//	;
	private static IElementType parseEncapsVarOffset(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(!builder.compareAndEat(IDENTIFIER))
		{
			if(!builder.compareAndEat(VARIABLE_OFFSET_NUMBER))
			{
				if(!builder.compareAndEat(VARIABLE))
				{
					marker.drop();
					return PHPElementTypes.EMPTY_INPUT;
				}
			}
		}
		marker.done(PHPElementTypes.ARRAY_INDEX);
		return PHPElementTypes.ARRAY_INDEX;
	}
}
