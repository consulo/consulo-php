package net.jay.plugins.php.lang.parser.parsing.statements;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.Statement;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import net.jay.plugins.php.lang.parser.parsing.calls.Variable;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.parsing.functions.IsReference;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class ForeachStatement implements PHPTokenTypes
{

	//	kwFOREACH '(' expr kwAS
	//		foreach_variable foreach_optional_arg ')'
	//		foreach_statement

	//	foreach_optional_arg:
	//		/* empty */
	//		| opHASH_ARRAY foreach_variable
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwFOREACH))
		{
			return PHPElementTypes.EMPTY_INPUT;
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

		foreach.done(PHPElementTypes.FOREACH);
		return PHPElementTypes.FOREACH;
	}

	//	foreach_statement:
	//		statement
	//		| ':' statement_list kwENDFOREACH ';'
	//	;
	private static void parseForeachStatement(PHPPsiBuilder builder)
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
	private static void parseForeachVariable(PHPPsiBuilder builder)
	{
		IsReference.parse(builder);
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parse(builder);
		variable.done(result);
	}
}
