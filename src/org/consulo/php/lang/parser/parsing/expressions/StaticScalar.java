package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.classes.StaticClassConstant;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticScalar implements PhpTokenTypes
{

	//	static_scalar: /* compile-time evaluated scalars */
	//		common_scalar
	//		| IDENTIFIER
	//		| opPLUS static_scalar
	//		| opMINUS static_scalar
	//		| kwARRAY '(' static_array_pair_list ')'
	//		| static_class_constant
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker staticScalar = builder.mark();
		IElementType result = StaticClassConstant.parse(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(opPLUS) || builder.compareAndEat(opMINUS))
			{
				parse(builder);
				staticScalar.done(PhpElementTypes.STATIC_SCALAR);
				result = PhpElementTypes.STATIC_SCALAR;
			}
			else if(builder.compareAndEat(kwARRAY))
			{
				builder.match(chLPAREN);
				StaticArrayPairList.parse(builder);
				builder.match(chRPAREN);
				staticScalar.done(PhpElementTypes.ARRAY);
				result = PhpElementTypes.ARRAY;
			}
			else if(builder.compareAndEat(IDENTIFIER))
			{
				staticScalar.done(PhpElementTypes.CONSTANT);
				result = PhpElementTypes.CONSTANT;
			}
			else
			{
				result = parseCommonScalar(builder);
				if(result != PhpElementTypes.EMPTY_INPUT)
				{
					staticScalar.drop();
				}
			}
		}
		else
		{
			staticScalar.drop();
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			staticScalar.rollbackTo();
		}
		return result;
	}

	//	common_scalar:
	//		INTEGER_LITERAL
	//		| FLOAT_LITERAL
	//		| STRING_LITERAL
	//		| CONST_LINE
	//		| CONST_FILE
	//		| CONST_CLASS
	//		| CONST_METHOD
	//		| CONST_FUNCTION
	//	;

	/**
	 * We can use STRING_LITERAL here because it means
	 * that the string has no variables in it
	 *
	 * @param builder current PsiBuilder wrapper
	 * @return EMPTY_INPUT on empty input, COMMON_SCALAR on success
	 */
	public static IElementType parseCommonScalar(PhpPsiBuilder builder)
	{
		if(builder.compare(tsCOMMON_SCALARS))
		{
			PsiBuilder.Marker scalar = builder.mark();
			IElementType type = builder.getTokenType();
			builder.advanceLexer();
			if(TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL).contains(type))
			{
				scalar.done(PhpElementTypes.NUMBER);
			}
			else if(TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE).contains(type))
			{
				scalar.done(PhpElementTypes.STRING);
			}
			else
			{
				scalar.done(PhpElementTypes.CONSTANT);
			}
			return PhpElementTypes.COMMON_SCALAR;
		}
		return PhpElementTypes.EMPTY_INPUT;
	}

}

