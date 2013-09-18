package org.consulo.php.lang.parser.parsing.expressions;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticArrayPairList
{

	//	static_array_pair_list:
	//		/* empty */
	//		| non_empty_static_array_pair_list possible_comma
	//	;
	//
	//	possible_comma:
	//		/* empty */
	//		| ','
	//	;
	//
	//	non_empty_static_array_pair_list:
	//		non_empty_static_array_pair_list ',' static_scalar opHASH_ARRAY static_scalar
	//		| non_empty_static_array_pair_list ',' static_scalar
	//		| static_scalar opHASH_ARRAY static_scalar
	//		| static_scalar
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		ParserPart parser = new ParserPart()
		{

			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				PsiBuilder.Marker staticArrayPair = builder.mark();
				IElementType result = StaticScalar.parse(builder);
				if(result != PhpElementTypes.EMPTY_INPUT)
				{
					if(builder.compare(PhpTokenTypes.opHASH_ARRAY))
					{
						staticArrayPair.done(PhpElementTypes.ARRAY_KEY);
						builder.advanceLexer();
						staticArrayPair = builder.mark();
						if(StaticScalar.parse(builder) == PhpElementTypes.EMPTY_INPUT)
						{
							builder.error(PhpParserErrors.expected("static value"));
						}
					}
					staticArrayPair.done(PhpElementTypes.ARRAY_VALUE);
				}
				else
				{
					staticArrayPair.rollbackTo();
				}
				return result;
			}
		};

		IElementType result = parser.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, parser, true);
		}
		return result;
	}
}
