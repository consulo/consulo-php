package consulo.php.impl.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class SwitchStatement implements PhpTokenTypes
{

	//	kwSWITCH '(' expr ')' switch_case_list
	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwSWITCH))
		{
			statement.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		builder.match(LPAREN);
		Expression.parse(builder);
		builder.match(RPAREN);

		parseSwitchCaseList(builder);

		statement.done(PhpElementTypes.SWITCH);
		return PhpElementTypes.SWITCH;
	}

	//	switch_case_list:
	//		'{' case_list '}'
	//		| '{' ';' case_list '}'
	//		| ':' case_list kwENDSWITCH ';'
	//		| ':' ';' case_list kwENDSWITCH ';'
	//	;
	private static void parseSwitchCaseList(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(opCOLON))
		{
			builder.compareAndEat(opSEMICOLON);
			parseCaseList(builder, kwENDSWITCH);
			builder.match(kwENDSWITCH);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		else
		{
			builder.match(LBRACE);
			builder.compareAndEat(opSEMICOLON);
			parseCaseList(builder, RBRACE);
			builder.match(RBRACE);
		}

	}

	//	case_list:
	//		/* empty */
	//		| case_list kwCASE expr case_separator statement_list
	//		| case_list kwDEFAULT case_separator statement_list
	//	;
	//
	//	case_separator:
	//		':'
	//		| ';'
	//	;
	private static void parseCaseList(PhpPsiBuilder builder, IElementType whereToStop)
	{
		while(!builder.eof() && !builder.compare(whereToStop))
		{
			IElementType result = parseCase(builder, whereToStop);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
				builder.advanceLexer();
			}
		}
	}

	private static IElementType parseCase(PhpPsiBuilder builder, IElementType whereToStop)
	{
		PsiBuilder.Marker caseMarker = builder.mark();
		if(builder.compareAndEat(kwDEFAULT))
		{
			builder.match(TokenSet.create(opCOLON, opSEMICOLON));
			StatementList.parse(builder, whereToStop, kwCASE, kwDEFAULT);
			caseMarker.done(PhpElementTypes.CASE_DEFAULT);
			return PhpElementTypes.CASE_DEFAULT;
		}
		if(builder.compareAndEat(kwCASE))
		{
			IElementType result = Expression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			builder.match(TokenSet.create(opCOLON, opSEMICOLON));
			StatementList.parse(builder, whereToStop, kwCASE, kwDEFAULT);
			caseMarker.done(PhpElementTypes.CASE);
			return PhpElementTypes.CASE;
		}
		caseMarker.drop();
		return PhpElementTypes.EMPTY_INPUT;
	}

}
