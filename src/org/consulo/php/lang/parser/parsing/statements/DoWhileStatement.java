package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.Statement;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class DoWhileStatement implements PHPTokenTypes
{

	//		kwDO statement kwWHILE '(' expr ')' ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwDO))
		{
			statement.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		Statement.parse(builder);
		builder.match(kwWHILE);
		builder.match(chLPAREN);
		Expression.parse(builder);
		builder.match(chRPAREN);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PhpElementTypes.DO_WHILE);
		return PhpElementTypes.DO_WHILE;
	}
}
