package consulo.php.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.StatementList;
import consulo.php.lang.parser.parsing.classes.ClassReference;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
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

		builder.match(LBRACE);
		StatementList.parse(builder, RBRACE);
		builder.match(RBRACE);
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
		builder.match(LPAREN);
		if(ClassReference.parse(builder) == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("exception class"));
		}
		PsiBuilder.Marker variable = builder.mark();
		builder.match(VARIABLE);
		variable.done(PhpElementTypes.VARIABLE_REFERENCE);

		builder.match(RPAREN);
		builder.match(LBRACE);
		StatementList.parse(builder, RBRACE);
		builder.match(RBRACE);
		catchClause.done(PhpElementTypes.CATCH);
		return PhpElementTypes.CATCH;
	}
}
