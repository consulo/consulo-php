package net.jay.plugins.php.lang.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.Statement;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 30.10.2007
 */
public class IfStatement implements PHPTokenTypes {

	//		kwIF '(' expr ')' statement elseif_list else_single
	//		| kwIF '(' expr ')' ':' statement_list new_elseif_list new_else_single kwENDIF ';'
	public static IElementType parse(PHPPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(kwIF)) {
			statement.drop();
			return PHPElementTypes.EMPTY_INPUT;
		}
		builder.match(chLPAREN);
		Expression.parse(builder);
		builder.match(chRPAREN);
		if (builder.compareAndEat(opCOLON)) {
			parseNewStyleIf(builder);
			builder.match(kwENDIF);
      if (!builder.compare(PHP_CLOSING_TAG)) {
        builder.match(opSEMICOLON);
      }
    } else {
			parseOldStyleIf(builder);
		}
		statement.done(PHPElementTypes.IF);
		return PHPElementTypes.IF;
	}

	//		statement_list new_elseif_list new_else_single kwENDIF ';'

	//	new_elseif_list:
	//		/* empty */
	//		| new_elseif_list kwELSEIF '(' expr ')' ':' statement_list
	//	;

	//	new_else_single:
	//		/* empty */
	//		| kwELSE ':' statement_list
	//	;
	private static void parseNewStyleIf(PHPPsiBuilder builder) {
		StatementList.parse(builder, kwELSEIF, kwELSE, kwENDIF);
		while (builder.compare(kwELSEIF)) {
			PsiBuilder.Marker elseifClause = builder.mark();
			builder.advanceLexer();
			builder.match(chLPAREN);
			Expression.parse(builder);
			builder.match(chRPAREN);
			builder.match(opCOLON);
			StatementList.parse(builder, kwELSEIF, kwELSE, kwENDIF);
			elseifClause.done(PHPElementTypes.ELSE_IF);
		}
		if (builder.compare(kwELSE)) {
			PsiBuilder.Marker elseClause = builder.mark();
			builder.advanceLexer();
			builder.match(opCOLON);
			StatementList.parse(builder, kwENDIF);
			elseClause.done(PHPElementTypes.ELSE);
		}
	}

	//		statement elseif_list else_single

	//	elseif_list:
	//		/* empty */
	//		| elseif_list kwELSEIF '(' expr ')' statement
	//	;

	//	else_single:
	//		/* empty */
	//		| kwELSE statement
	//	;
	private static void parseOldStyleIf(PHPPsiBuilder builder) {
		Statement.parse(builder);
		while (builder.compare(kwELSEIF)) {
			PsiBuilder.Marker elseifClause = builder.mark();
			builder.advanceLexer();
			builder.match(chLPAREN);
			Expression.parse(builder);
			builder.match(chRPAREN);
			Statement.parse(builder);
			elseifClause.done(PHPElementTypes.ELSE_IF);
		}
		if (builder.compare(kwELSE)) {
			PsiBuilder.Marker elseClause = builder.mark();
			builder.advanceLexer();
			Statement.parse(builder);
			elseClause.done(PHPElementTypes.ELSE);
		}
	}
}
