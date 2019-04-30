package consulo.php.lang.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.Statement;
import consulo.php.lang.parser.parsing.StatementList;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 08.11.2007
 */
public class DeclareStatement implements PhpTokenTypes
{

	//		kwDECLARE '(' declare_list ')' declare_statement
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwDECLARE))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();

		builder.match(LPAREN);
		parseDeclareList(builder);
		builder.match(RPAREN);
		parseDeclareStatement(builder);

		statement.done(PhpElementTypes.DECLARE);
		return PhpElementTypes.DECLARE;
	}

	//	declare_statement:
	//		statement
	//		| ':' statement_list kwENDDECLARE ';'
	//	;
	private static void parseDeclareStatement(PhpPsiBuilder builder)
	{
		if(builder.compareAndEat(opCOLON))
		{
			StatementList.parse(builder, kwENDDECLARE);
			if(!builder.compare(PHP_CLOSING_TAG))
			{
				builder.match(opSEMICOLON);
			}
		}
		else
		{
			Statement.parse(builder);
		}
	}

	//	declare_list:
	//		IDENTIFIER '=' static_scalar
	//		| declare_list ',' IDENTIFIER '=' static_scalar
	//	;
	private static void parseDeclareList(PhpPsiBuilder builder)
	{
		ParserPart listParser = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				if(!builder.compare(IDENTIFIER))
				{
					return PhpElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker directive = builder.mark();
				builder.advanceLexer();
				builder.match(opASGN);
				Expression.parse(builder);
				directive.done(PhpElementTypes.DECLARE_DIRECTIVE);
				return PhpElementTypes.DECLARE_DIRECTIVE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, listParser.parse(builder), listParser, false);
	}
}
