package consulo.php.impl.lang.parser.parsing.statements;

import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.Statement;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class ForStatement implements PhpTokenTypes
{

	//	kwFOR '('
	//		for_expr ';'
	//		for_expr ';'
	//		for_expr ')' for_statement
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwFOR))
		{
			statement.drop();
			return PhpElementTypes.FOR;
		}
		builder.match(LPAREN);

		// initial expression
		parseForExpression(builder);
		builder.match(opSEMICOLON);

		// conditional expression
		parseForExpression(builder);
		builder.match(opSEMICOLON);

		// repeated expression
		parseForExpression(builder);

		builder.match(RPAREN);
		parseForStatement(builder);
		statement.done(PhpElementTypes.FOR);
		return PhpElementTypes.FOR;
	}

	//	for_statement:
	//		statement
	//		| ':' statement_list kwENDFOR ';'
	//	;
	private static void parseForStatement(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(opCOLON))
		{
			StatementList.parse(builder, kwENDFOR);
			builder.match(kwENDFOR);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		else
		{
			Statement.parse(builder);
		}
	}

	//	for_expr:
	//		/* empty */
	//		| non_empty_for_expr
	//	;
	//
	//	non_empty_for_expr:
	//		non_empty_for_expr ',' expr
	//		| expr
	//	;
	private static void parseForExpression(PhpPsiBuilder builder)
	{
		ParserPart parserPart = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				return Expression.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, parserPart.parse(builder), parserPart, false);
	}
}
