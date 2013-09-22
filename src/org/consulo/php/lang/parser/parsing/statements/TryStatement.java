package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.parsing.classes.ClassReference;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 08.11.2007
 */
public class TryStatement implements PhpTokenTypes
{

	//	kwTRY '{' statement_list '}'
	//		non_empty_catch_clauses
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwTRY))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();

		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		parseCatchClauses(builder);

		statement.done(PhpElementTypes.TRY);
		return PhpElementTypes.TRY;
	}

	//	catch_clause:
	//		kwCATCH '(' fully_qualified_class_name VARIABLE_REFERENCE ')' '{' statement_list '}'
	//	;
	//
	//	non_empty_catch_clauses:
	//		catch_clause
	//		| non_empty_catch_clauses catch_clause
	//	;
	private static void parseCatchClauses(PhpPsiBuilder builder)
	{
		if(parseCatchClause(builder) == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("catch clause"));
		}
		//noinspection StatementWithEmptyBody
		while(parseCatchClause(builder) != PhpElementTypes.EMPTY_INPUT)
		{
			;
		}
	}

	private static IElementType parseCatchClause(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwCATCH))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker catchClause = builder.mark();
		builder.advanceLexer();
		builder.match(chLPAREN);
		if(ClassReference.parse(builder) == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("exception class"));
		}
		PsiBuilder.Marker variable = builder.mark();
		builder.match(VARIABLE);
		variable.done(PhpElementTypes.VARIABLE_REFERENCE);

		builder.match(chRPAREN);
		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		catchClause.done(PhpElementTypes.CATCH);
		return PhpElementTypes.CATCH;
	}
}
