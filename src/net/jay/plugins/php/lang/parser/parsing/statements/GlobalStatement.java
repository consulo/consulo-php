package net.jay.plugins.php.lang.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.calls.Variable;
import net.jay.plugins.php.lang.parser.parsing.expressions.Expression;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 04.11.2007
 */
public class GlobalStatement implements PHPTokenTypes {

	//	kwGLOBAL global_var_list ';'
	public static IElementType parse(PHPPsiBuilder builder) {
		if (!builder.compareAndEat(kwGLOBAL)) {
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
    parseGlobalVarList(builder);
    if (!builder.compare(PHP_CLOSING_TAG)) {
      builder.match(opSEMICOLON);
    }
    statement.done(PHPElementTypes.GLOBAL);
		return PHPElementTypes.GLOBAL;
	}

	//	global_var_list:
	//		global_var_list ',' global_var
	//		| global_var
	//	;
	//
	//	global_var:
	//		VARIABLE
	//		| '$' variable //read
	//		| '$' '{' expr '}'
	//	;
	private static void parseGlobalVarList(PHPPsiBuilder builder) {
		ParserPart globalVariable = new ParserPart() {
			public IElementType parse(PHPPsiBuilder builder) {
				if (!builder.compare(TokenSet.create(VARIABLE, DOLLAR))) {
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker variable = builder.mark();
				if (builder.compareAndEat(VARIABLE)) {
					variable.done(PHPElementTypes.VARIABLE);
					return PHPElementTypes.VARIABLE;
				}
				builder.match(DOLLAR);
				if (builder.compareAndEat(chLBRACE)) {
					Expression.parse(builder);
					builder.match(chRBRACE);
					variable.done(PHPElementTypes.VARIABLE);
					return PHPElementTypes.VARIABLE;
				}
				Variable.parse(builder);
				variable.done(PHPElementTypes.VARIABLE);
				return PHPElementTypes.VARIABLE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
			globalVariable.parse(builder),
			globalVariable,
			false);
	}
}
