package consulo.php.impl.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.calls.Variable;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;

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
		builder.match(LPAREN);
		parseUnsetVariables(builder);
		builder.match(RPAREN);
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
