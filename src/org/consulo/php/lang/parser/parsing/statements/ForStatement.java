package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.Statement;
import org.consulo.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class ForStatement implements PHPTokenTypes
{

	//	kwFOR '('
	//		for_expr ';'
	//		for_expr ';'
	//		for_expr ')' for_statement
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwFOR))
		{
			statement.drop();
			return PHPElementTypes.FOR;
		}
		builder.match(chLPAREN);

		// initial expression
		parseForExpression(builder);
		builder.match(opSEMICOLON);

		// conditional expression
		parseForExpression(builder);
		builder.match(opSEMICOLON);

		// repeated expression
		parseForExpression(builder);

		builder.match(chRPAREN);
		parseForStatement(builder);
		statement.done(PHPElementTypes.FOR);
		return PHPElementTypes.FOR;
	}

	//	for_statement:
	//		statement
	//		| ':' statement_list kwENDFOR ';'
	//	;
	private static void parseForStatement(PHPPsiBuilder builder)
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
	private static void parseForExpression(PHPPsiBuilder builder)
	{
		ParserPart parserPart = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				return Expression.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, parserPart.parse(builder), parserPart, false);
	}
}
