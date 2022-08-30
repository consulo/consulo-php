package consulo.php.impl.lang.parser.parsing.statements;

import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class EchoStatement implements PhpTokenTypes
{

	//	kwECHO echo_expr_list ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwECHO))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker echo = builder.mark();
		builder.advanceLexer();
		parseEchoExpressions(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		echo.done(PhpElementTypes.ECHO);
		return PhpElementTypes.ECHO;
	}

	//	echo_expr_list:
	//		echo_expr_list ',' expr
	//		| expr
	//	;
	private static void parseEchoExpressions(PhpPsiBuilder builder)
	{
		ParserPart expression = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				return Expression.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, expression.parse(builder), expression, false);
	}
}
