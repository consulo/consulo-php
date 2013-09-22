package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.ParserPart;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 06.11.2007
 */
public class UnsetStatement implements PhpTokenTypes
{

	//	kwUNSET '(' unset_variables ')' ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwUNSET))
		{
			return PhpElementTypes.EMPTY_INPUT;
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
		unset.done(PhpElementTypes.UNSET);
		return PhpElementTypes.UNSET;
	}

	//	unset_variables:
	//		variable
	//		| unset_variables ',' variable
	//	;
	private static void parseUnsetVariables(PhpPsiBuilder builder)
	{
		ParserPart variable = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				return Variable.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, variable.parse(builder), variable, false);
	}
}
