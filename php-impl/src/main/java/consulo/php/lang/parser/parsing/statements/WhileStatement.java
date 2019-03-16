package consulo.php.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.Statement;
import consulo.php.lang.parser.parsing.StatementList;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 31.10.2007
 */
public class WhileStatement implements PhpTokenTypes
{

	//	kwWHILE '(' expr ')' while_statement

	//	while_statement:
	//		statement
	//		| ':' statement_list kwENDWHILE ';'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwWHILE))
		{
			statement.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		builder.match(LPAREN);
		Expression.parse(builder);
		builder.match(RPAREN);
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
		statement.done(PhpElementTypes.WHILE);
		return PhpElementTypes.WHILE;
	}
}
