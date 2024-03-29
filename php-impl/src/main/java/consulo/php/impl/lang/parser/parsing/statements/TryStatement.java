package consulo.php.impl.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.parsing.classes.ClassReference;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;

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
		if(builder.getTokenType()== FINALLY_KEYWORD || builder.getTokenType() == kwCATCH)
		{
			//noinspection StatementWithEmptyBody
			while(parseCatchClause(builder) != PhpElementTypes.EMPTY_INPUT)
			{
			}

			//noinspection StatementWithEmptyBody
			while(parseFinally(builder) != PhpElementTypes.EMPTY_INPUT)
			{
			}
		}
		else
		{
			builder.error("Expected catch or finally statement");
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

	private static IElementType parseFinally(PhpPsiBuilder builder)
	{
		if(!builder.compare(FINALLY_KEYWORD))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker finallyMarker = builder.mark();
		builder.advanceLexer();

		builder.match(LBRACE);
		StatementList.parse(builder, RBRACE);
		builder.match(RBRACE);

		finallyMarker.done(PhpElementTypes.FINALLY_STATEMENT);
		return PhpElementTypes.FINALLY_STATEMENT;
	}
}
