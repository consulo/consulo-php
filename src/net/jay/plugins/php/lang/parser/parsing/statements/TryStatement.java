package net.jay.plugins.php.lang.parser.parsing.statements;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import net.jay.plugins.php.lang.parser.parsing.classes.ClassReference;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 08.11.2007
 */
public class TryStatement implements PHPTokenTypes
{

	//	kwTRY '{' statement_list '}'
	//		non_empty_catch_clauses
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwTRY))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();

		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		parseCatchClauses(builder);

		statement.done(PHPElementTypes.TRY);
		return PHPElementTypes.TRY;
	}

	//	catch_clause:
	//		kwCATCH '(' fully_qualified_class_name VARIABLE_REFERENCE ')' '{' statement_list '}'
	//	;
	//
	//	non_empty_catch_clauses:
	//		catch_clause
	//		| non_empty_catch_clauses catch_clause
	//	;
	private static void parseCatchClauses(PHPPsiBuilder builder)
	{
		if(parseCatchClause(builder) == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("catch clause"));
		}
		//noinspection StatementWithEmptyBody
		while(parseCatchClause(builder) != PHPElementTypes.EMPTY_INPUT)
			;
	}

	private static IElementType parseCatchClause(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwCATCH))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker catchClause = builder.mark();
		builder.advanceLexer();
		builder.match(chLPAREN);
		if(ClassReference.parse(builder) == PHPElementTypes.EMPTY_INPUT)
		{
			builder.error(PHPParserErrors.expected("exception class"));
		}
		PsiBuilder.Marker variable = builder.mark();
		builder.match(VARIABLE);
		variable.done(PHPElementTypes.VARIABLE_REFERENCE);

		builder.match(chRPAREN);
		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		catchClause.done(PHPElementTypes.CATCH);
		return PHPElementTypes.CATCH;
	}
}
