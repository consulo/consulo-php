package consulo.php.impl.lang.parser.parsing.statements;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.11.2007
 */
public class StaticStatement implements PhpTokenTypes
{

	//	STATIC_KEYWORD static_var_list ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(STATIC_KEYWORD))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();
		if(builder.getTokenType() == SCOPE_RESOLUTION)
		{
			statement.rollbackTo();
			return PhpElementTypes.EMPTY_INPUT;
		}
		parseStaticVarList(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PhpElementTypes.STATIC);
		return PhpElementTypes.STATIC;
	}

	//	static_var_list:
	//		static_var_list ',' VARIABLE_REFERENCE
	//		| static_var_list ',' VARIABLE_REFERENCE '=' static_scalar
	//		| VARIABLE_REFERENCE
	//		| VARIABLE_REFERENCE '=' static_scalar
	//	;
	private static void parseStaticVarList(PhpPsiBuilder builder)
	{
		ParserPart staticVariable = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				if(!builder.compare(VARIABLE))
				{
					return PhpElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker var = builder.mark();
				builder.match(VARIABLE);
				if(builder.compareAndEat(opASGN))
				{
					Expression.parse(builder);
				}
				var.done(PhpElementTypes.VARIABLE_REFERENCE);
				return PhpElementTypes.VARIABLE_REFERENCE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, staticVariable.parse(builder), staticVariable, false);
	}
}
