package consulo.php.impl.lang.parser.parsing.statements;

import consulo.language.ast.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.Statement;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 30.10.2007
 */
public class IfStatement implements PhpTokenTypes
{

	//		kwIF '(' expr ')' statement elseif_list else_single
	//		| kwIF '(' expr ')' ':' statement_list new_elseif_list new_else_single kwENDIF ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwIF))
		{
			statement.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		builder.match(LPAREN);
		Expression.parse(builder);
		builder.match(RPAREN);
		if(builder.compareAndEat(opCOLON))
		{
			parseNewStyleIf(builder);
			builder.match(kwENDIF);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		else
		{
			parseOldStyleIf(builder);
		}
		statement.done(PhpElementTypes.IF);
		return PhpElementTypes.IF;
	}

	//		statement_list new_elseif_list new_else_single kwENDIF ';'

	//	new_elseif_list:
	//		/* empty */
	//		| new_elseif_list kwELSEIF '(' expr ')' ':' statement_list
	//	;

	//	new_else_single:
	//		/* empty */
	//		| kwELSE ':' statement_list
	//	;
	private static void parseNewStyleIf(PhpPsiBuilder builder)
	{
		StatementList.parse(builder, kwELSEIF, kwELSE, kwENDIF);
		while(builder.compare(kwELSEIF))
		{
			PsiBuilder.Marker elseifClause = builder.mark();
			builder.advanceLexer();
			builder.match(LPAREN);
			Expression.parse(builder);
			builder.match(RPAREN);
			builder.match(opCOLON);
			StatementList.parse(builder, kwELSEIF, kwELSE, kwENDIF);
			elseifClause.done(PhpElementTypes.ELSE_IF);
		}
		if(builder.compare(kwELSE))
		{
			PsiBuilder.Marker elseClause = builder.mark();
			builder.advanceLexer();
			builder.match(opCOLON);
			StatementList.parse(builder, kwENDIF);
			elseClause.done(PhpElementTypes.ELSE);
		}
	}

	//		statement elseif_list else_single

	//	elseif_list:
	//		/* empty */
	//		| elseif_list kwELSEIF '(' expr ')' statement
	//	;

	//	else_single:
	//		/* empty */
	//		| kwELSE statement
	//	;
	private static void parseOldStyleIf(PhpPsiBuilder builder)
	{
		Statement.parse(builder);
		while(builder.compare(kwELSEIF))
		{
			PsiBuilder.Marker elseifClause = builder.mark();
			builder.advanceLexer();
			builder.match(LPAREN);
			Expression.parse(builder);
			builder.match(RPAREN);
			Statement.parse(builder);
			elseifClause.done(PhpElementTypes.ELSE_IF);
		}
		if(builder.compare(kwELSE))
		{
			PsiBuilder.Marker elseClause = builder.mark();
			builder.advanceLexer();
			Statement.parse(builder);
			elseClause.done(PhpElementTypes.ELSE);
		}
	}
}
