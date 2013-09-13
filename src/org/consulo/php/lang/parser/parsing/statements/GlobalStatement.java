package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.parsing.expressions.Expression;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 04.11.2007
 */
public class GlobalStatement implements PHPTokenTypes
{

	//	kwGLOBAL global_var_list ';'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compareAndEat(kwGLOBAL))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		parseGlobalVarList(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
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
	//		VARIABLE_REFERENCE
	//		| '$' variable //read
	//		| '$' '{' expr '}'
	//	;
	private static void parseGlobalVarList(PHPPsiBuilder builder)
	{
		ParserPart globalVariable = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				if(!builder.compare(TokenSet.create(VARIABLE, DOLLAR)))
				{
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker variable = builder.mark();
				if(builder.compareAndEat(VARIABLE))
				{
					variable.done(PHPElementTypes.VARIABLE_REFERENCE);
					return PHPElementTypes.VARIABLE_REFERENCE;
				}
				builder.match(DOLLAR);
				if(builder.compareAndEat(chLBRACE))
				{
					Expression.parse(builder);
					builder.match(chRBRACE);
					variable.done(PHPElementTypes.VARIABLE_REFERENCE);
					return PHPElementTypes.VARIABLE_REFERENCE;
				}
				Variable.parse(builder);
				variable.done(PHPElementTypes.VARIABLE_REFERENCE);
				return PHPElementTypes.VARIABLE_REFERENCE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, globalVariable.parse(builder), globalVariable, false);
	}
}
