package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class SwitchStatement implements PHPTokenTypes
{

	//	kwSWITCH '(' expr ')' switch_case_list
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker statement = builder.mark();
		if(!builder.compareAndEat(kwSWITCH))
		{
			statement.drop();
			return PHPElementTypes.EMPTY_INPUT;
		}
		builder.match(chLPAREN);
		Expression.parse(builder);
		builder.match(chRPAREN);

		parseSwitchCaseList(builder);

		statement.done(PHPElementTypes.SWITCH);
		return PHPElementTypes.SWITCH;
	}

	//	switch_case_list:
	//		'{' case_list '}'
	//		| '{' ';' case_list '}'
	//		| ':' case_list kwENDSWITCH ';'
	//		| ':' ';' case_list kwENDSWITCH ';'
	//	;
	private static void parseSwitchCaseList(PHPPsiBuilder builder)
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
			builder.match(chLBRACE);
			builder.compareAndEat(opSEMICOLON);
			parseCaseList(builder, chRBRACE);
			builder.match(chRBRACE);
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
	private static void parseCaseList(PHPPsiBuilder builder, IElementType whereToStop)
	{
		while(!builder.eof() && !builder.compare(whereToStop))
		{
			IElementType result = parseCase(builder, whereToStop);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.unexpected(builder.getTokenType()));
				builder.advanceLexer();
			}
		}
	}

	private static IElementType parseCase(PHPPsiBuilder builder, IElementType whereToStop)
	{
		PsiBuilder.Marker caseMarker = builder.mark();
		if(builder.compareAndEat(kwDEFAULT))
		{
			builder.match(TokenSet.create(opCOLON, opSEMICOLON));
			StatementList.parse(builder, whereToStop, kwCASE, kwDEFAULT);
			caseMarker.done(PHPElementTypes.CASE_DEFAULT);
			return PHPElementTypes.CASE_DEFAULT;
		}
		if(builder.compareAndEat(kwCASE))
		{
			IElementType result = Expression.parse(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			builder.match(TokenSet.create(opCOLON, opSEMICOLON));
			StatementList.parse(builder, whereToStop, kwCASE, kwDEFAULT);
			caseMarker.done(PHPElementTypes.CASE);
			return PHPElementTypes.CASE;
		}
		caseMarker.drop();
		return PHPElementTypes.EMPTY_INPUT;
	}

}
