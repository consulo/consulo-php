package consulo.php.lang.parser.parsing.expressions.primary;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 20.12.2007 23:03:43
 */
public class Array implements PhpTokenTypes
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
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(kwARRAY))
		{
			builder.match(LPAREN);
			ParserPart arrayItem = new ParserPart()
			{
				@Override
				public IElementType parse(PhpPsiBuilder builder)
				{
					PsiBuilder.Marker item = builder.mark();
					if(builder.compareAndEat(opBIT_AND))
					{
						IElementType result = Variable.parse(builder);
						if(result == PhpElementTypes.EMPTY_INPUT)
						{
							builder.error(PhpParserErrors.expected("variable"));
						}
						item.done(PhpElementTypes.ARRAY_VALUE);
						return PhpElementTypes.ARRAY_VALUE;
					}
					else
					{
						IElementType result = Expression.parse(builder);
						if(result != PhpElementTypes.EMPTY_INPUT)
						{
							if(builder.compare(HASH_ARRAY))
							{
								item.done(PhpElementTypes.ARRAY_KEY);
								builder.advanceLexer();
								item = builder.mark();
								if(builder.compareAndEat(opBIT_AND))
								{
									result = Variable.parse(builder);
									if(result == PhpElementTypes.EMPTY_INPUT)
									{
										builder.error(PhpParserErrors.expected("variable"));
									}
									item.done(PhpElementTypes.ARRAY_VALUE);
									return PhpElementTypes.ARRAY_VALUE;
								}
								else
								{
									result = Expression.parse(builder);
									if(result == PhpElementTypes.EMPTY_INPUT)
									{
										builder.error(PhpParserErrors.expected("expression"));
									}
									item.done(PhpElementTypes.ARRAY_VALUE);
									return PhpElementTypes.ARRAY_VALUE;
								}
							}
							else
							{
								item.done(PhpElementTypes.ARRAY_VALUE);
								return PhpElementTypes.ARRAY_VALUE;
							}
						}
						else
						{
							item.drop();
							return PhpElementTypes.EMPTY_INPUT;
						}
					}
				}
			};
			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, arrayItem.parse(builder), arrayItem, true);
			builder.match(RPAREN);
			marker.done(PhpElementTypes.ARRAY);
			return PhpElementTypes.ARRAY;
		}
		marker.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}

}
