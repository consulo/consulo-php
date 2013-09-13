package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class EchoStatement implements PHPTokenTypes
{

	//	kwECHO echo_expr_list ';'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwECHO))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker echo = builder.mark();
		builder.advanceLexer();
		parseEchoExpressions(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		echo.done(PHPElementTypes.ECHO);
		return PHPElementTypes.ECHO;
	}

	//	echo_expr_list:
	//		echo_expr_list ',' expr
	//		| expr
	//	;
	private static void parseEchoExpressions(PHPPsiBuilder builder)
	{
		ParserPart expression = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				return Expression.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, expression.parse(builder), expression, false);
	}
}
