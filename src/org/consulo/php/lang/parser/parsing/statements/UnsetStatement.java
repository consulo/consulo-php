package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class UnsetStatement implements PHPTokenTypes
{

	//	kwUNSET '(' unset_variables ')' ';'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwUNSET))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker unset = builder.mark();
		builder.advanceLexer();
		builder.match(chLPAREN);
		parseUnsetVariables(builder);
		builder.match(chRPAREN);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		unset.done(PHPElementTypes.UNSET);
		return PHPElementTypes.UNSET;
	}

	//	unset_variables:
	//		variable
	//		| unset_variables ',' variable
	//	;
	private static void parseUnsetVariables(PHPPsiBuilder builder)
	{
		ParserPart variable = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				return Variable.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, variable.parse(builder), variable, false);
	}
}
