package consulo.php.impl.lang.parser.parsing;

import consulo.language.ast.TokenSet;
import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.parsing.statements.*;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.ast.IElementType;
import consulo.php.impl.lang.parser.parsing.statements.ForStatement;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:18
 */
public class Statement implements PhpTokenTypes
{

	//	statement:
	//		'{' statement_list '}'
	//		| kwIF '(' expr ')' statement elseif_list else_single
	//		| kwIF '(' expr ')' ':' statement_list new_elseif_list new_else_single kwENDIF ';'
	//		| kwWHILE '(' expr ')' while_statement
	//		| kwDO statement kwWHILE '(' expr ')' ';'
	//		| kwFOR '('
	//			for_expr ';'
	//			for_expr ';'
	//			for_expr ')' for_statement
	//		| kwSWITCH '(' expr ')' switch_case_list
	//		| kwBREAK ';'
	//		| kwBREAK expr ';'
	//		| kwCONTINUE ';'
	//		| kwCONTINUE expr ';'
	//		| kwRETURN ';'
	//		| kwRETURN expr ';'
	//		| kwGLOBAL global_var_list ';'
	//		| STATIC_KEYWORD static_var_list ';'
	//		| kwECHO echo_expr_list ';'
	//		| HTML
	//		| expr ';'
	//		| kwUNSET '(' unset_variables ')' ';'
	//		| kwFOREACH '(' variable kwAS
	//			foreach_variable foreach_optional_arg ')'
	//			foreach_statement
	//		| kwFOREACH '(' expr_without_variable kwAS
	//			variable foreach_optional_arg ')'
	//			foreach_statement
	//		| kwDECLARE '(' declare_list ')' declare_statement
	//		| ';' /* empty statement */
	//		| kwTRY '{' statement_list '}'
	//			non_empty_catch_clauses
	//		| kwTHROW expr ';'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		//		'{' statement_list '}'
		if(builder.compareAndEat(LBRACE))
		{
			StatementList.parse(builder, RBRACE);
			builder.match(RBRACE);
			return PhpElementTypes.GROUP_STATEMENT;
		}
		//		HTML
		if(builder.compare(TokenSet.create(HTML, PHP_CLOSING_TAG)))
		{
			builder.compareAndEat(PHP_CLOSING_TAG);
			PsiBuilder.Marker html = builder.mark();
			if(builder.compareAndEat(HTML))
			{
				html.done(PhpElementTypes.HTML);
			}
			else
			{
				html.drop();
			}
			builder.compareAndEat(TokenSet.create(PHP_OPENING_TAG, PHP_ECHO_OPENING_TAG));
			return PhpElementTypes.HTML;
		}
		//		';' /* empty statement */
		if(builder.compare(opSEMICOLON))
		{
			PsiBuilder.Marker statement = builder.mark();
			builder.advanceLexer();
			statement.done(PhpElementTypes.STATEMENT);
			return PhpElementTypes.STATEMENT;
		}
		IElementType result = parseStatementByKeyword(builder);
		//		expr ';'
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = Expression.parse(builder);
			if(result != PhpElementTypes.EMPTY_INPUT && builder.getTokenType() != PHP_CLOSING_TAG)
			{
				builder.match(opSEMICOLON);
			}
		}
		return result;
	}

	//	statement:
	//		kwIF '(' expr ')' statement elseif_list else_single
	//		| kwIF '(' expr ')' ':' statement_list new_elseif_list new_else_single kwENDIF ';'
	//		| kwWHILE '(' expr ')' while_statement
	//		| kwDO statement kwWHILE '(' expr ')' ';'
	//		| kwFOR '('
	//			for_expr ';'
	//			for_expr ';'
	//			for_expr ')' for_statement
	//		| kwSWITCH '(' expr ')' switch_case_list
	//		| kwBREAK ';'
	//		| kwBREAK expr ';'
	//		| kwCONTINUE ';'
	//		| kwCONTINUE expr ';'
	//		| kwRETURN ';'
	//		| kwRETURN expr ';'
	//		| kwGLOBAL global_var_list ';'
	//		| STATIC_KEYWORD static_var_list ';'
	//		| kwECHO echo_expr_list ';'
	//		| kwUNSET '(' unset_variables ')' ';'
	//		| kwFOREACH '(' variable kwAS
	//			foreach_variable foreach_optional_arg ')'
	//			foreach_statement
	//		| kwFOREACH '(' expr_without_variable kwAS
	//			variable foreach_optional_arg ')'
	//			foreach_statement
	//		| kwDECLARE '(' declare_list ')' declare_statement
	//		| kwTRY '{' statement_list '}'
	//			non_empty_catch_clauses
	//		| kwTHROW expr ';'
	//	;
	private static IElementType parseStatementByKeyword(PhpPsiBuilder builder)
	{
		if(!builder.compare(tsSTATEMENT_FIRST_TOKENS))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		if(builder.compare(kwIF))
		{
			return IfStatement.parse(builder);
		}
		if(builder.compare(kwWHILE))
		{
			return WhileStatement.parse(builder);
		}
		if(builder.compare(kwDO))
		{
			return DoWhileStatement.parse(builder);
		}
		if(builder.compare(kwFOR))
		{
			return ForStatement.parse(builder);
		}
		if(builder.compare(kwSWITCH))
		{
			return SwitchStatement.parse(builder);
		}
		if(builder.compare(kwBREAK))
		{
			return BreakStatement.parse(builder);
		}
		if(builder.compare(kwCONTINUE))
		{
			return ContinueStatement.parse(builder);
		}
		if(builder.compare(kwRETURN))
		{
			return ReturnStatement.parse(builder);
		}
		if(builder.compare(kwGLOBAL))
		{
			return GlobalStatement.parse(builder);
		}
		if(builder.compare(STATIC_KEYWORD))
		{
			return StaticStatement.parse(builder);
		}
		if(builder.compare(kwECHO))
		{
			return EchoStatement.parse(builder);
		}
		if(builder.compare(kwUNSET))
		{
			return UnsetStatement.parse(builder);
		}
		if(builder.compare(kwFOREACH))
		{
			return ForeachStatement.parse(builder);
		}
		if(builder.compare(kwDECLARE))
		{
			return DeclareStatement.parse(builder);
		}
		if(builder.compare(kwTRY))
		{
			return TryStatement.parse(builder);
		}
		if(builder.compare(kwTHROW))
		{
			return ThrowStatement.parse(builder);
		}
		return PhpElementTypes.EMPTY_INPUT;
	}
}
