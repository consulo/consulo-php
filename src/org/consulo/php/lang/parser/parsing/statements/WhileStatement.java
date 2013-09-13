package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.Statement;
import org.consulo.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 31.10.2007
 */
public class WhileStatement implements PHPTokenTypes
{

	//	kwWHILE '(' expr ')' while_statement

	//	while_statement:
	//		statement
	//		| ':' statement_list kwENDWHILE ';'
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwWHILE))
		{
			statement.drop();
			return PHPElementTypes.EMPTY_INPUT;
		}
		builder.match(chLPAREN);
		Expression.parse(builder);
		builder.match(chRPAREN);
		if(builder.compareAndEat(opCOLON))
		{
			StatementList.parse(builder, kwENDWHILE);
			builder.match(kwENDWHILE);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		else
		{
			Statement.parse(builder);
		}
		statement.done(PHPElementTypes.WHILE);
		return PHPElementTypes.WHILE;
	}
}
