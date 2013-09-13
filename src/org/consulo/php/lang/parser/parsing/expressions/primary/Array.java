package org.consulo.php.lang.parser.parsing.expressions.primary;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 20.12.2007 23:03:43
 */
public class Array implements PHPTokenTypes
{

	//	array_pair_list:
	//		/* empty */
	//		| non_empty_array_pair_list possible_comma
	//	;
	//
	//	non_empty_array_pair_list:
	//		non_empty_array_pair_list ',' expr opHASH_ARRAY expr
	//		| non_empty_array_pair_list ',' expr
	//		| expr opHASH_ARRAY expr
	//		| expr
	//		| non_empty_array_pair_list ',' expr opHASH_ARRAY '&' variable //write
	//		| non_empty_array_pair_list ',' '&' variable //write
	//		| expr opHASH_ARRAY '&' variable //write
	//		| '&' variable //write
	//	;
	//	kwARRAY '(' array_pair_list ')'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(kwARRAY))
		{
			builder.match(chLPAREN);
			ParserPart arrayItem = new ParserPart()
			{
				public IElementType parse(PHPPsiBuilder builder)
				{
					PsiBuilder.Marker item = builder.mark();
					if(builder.compareAndEat(opBIT_AND))
					{
						IElementType result = Variable.parse(builder);
						if(result == PHPElementTypes.EMPTY_INPUT)
						{
							builder.error(PHPParserErrors.expected("variable"));
						}
						item.done(PHPElementTypes.ARRAY_VALUE);
						return PHPElementTypes.ARRAY_VALUE;
					}
					else
					{
						IElementType result = Expression.parse(builder);
						if(result != PHPElementTypes.EMPTY_INPUT)
						{
							if(builder.compare(opHASH_ARRAY))
							{
								item.done(PHPElementTypes.ARRAY_KEY);
								builder.advanceLexer();
								item = builder.mark();
								if(builder.compareAndEat(opBIT_AND))
								{
									result = Variable.parse(builder);
									if(result == PHPElementTypes.EMPTY_INPUT)
									{
										builder.error(PHPParserErrors.expected("variable"));
									}
									item.done(PHPElementTypes.ARRAY_VALUE);
									return PHPElementTypes.ARRAY_VALUE;
								}
								else
								{
									result = Expression.parse(builder);
									if(result == PHPElementTypes.EMPTY_INPUT)
									{
										builder.error(PHPParserErrors.expected("expression"));
									}
									item.done(PHPElementTypes.ARRAY_VALUE);
									return PHPElementTypes.ARRAY_VALUE;
								}
							}
							else
							{
								item.done(PHPElementTypes.ARRAY_VALUE);
								return PHPElementTypes.ARRAY_VALUE;
							}
						}
						else
						{
							item.drop();
							return PHPElementTypes.EMPTY_INPUT;
						}
					}
				}
			};
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, arrayItem.parse(builder), arrayItem, true);
			builder.match(chRPAREN);
			marker.done(PHPElementTypes.ARRAY);
			return PHPElementTypes.ARRAY;
		}
		marker.drop();
		return PHPElementTypes.EMPTY_INPUT;
	}

}
