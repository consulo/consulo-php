package consulo.php.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.Statement;
import consulo.php.lang.parser.parsing.StatementList;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.parsing.functions.IsReference;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class ForeachStatement implements PhpTokenTypes
{

	//	kwFOREACH '(' expr kwAS
	//		foreach_variable foreach_optional_arg ')'
	//		foreach_statement

	//	foreach_optional_arg:
	//		/* empty */
	//		| opHASH_ARRAY foreach_variable
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwFOREACH))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker foreach = builder.mark();
		builder.advanceLexer();
		builder.match(chLPAREN);

		Expression.parse(builder);
		builder.match(kwAS);
		parseForeachVariable(builder);
		if(builder.compareAndEat(opHASH_ARRAY))
		{
			parseForeachVariable(builder);
		}
		builder.match(chRPAREN);
		parseForeachStatement(builder);

		foreach.done(PhpElementTypes.FOREACH);
		return PhpElementTypes.FOREACH;
	}

	//	foreach_statement:
	//		statement
	//		| ':' statement_list kwENDFOREACH ';'
	//	;
	private static void parseForeachStatement(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(opCOLON))
		{
			StatementList.parse(builder, kwENDFOREACH);
			builder.match(kwENDFOREACH);
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

	//	foreach_variable:
	//		variable
	//		| '&' variable
	//	;
	private static void parseForeachVariable(PhpPsiBuilder builder)
	{
		IsReference.parse(builder);
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parse(builder);
		variable.done(result);
	}
}
