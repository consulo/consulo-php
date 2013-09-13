package org.consulo.php.lang.parser.parsing.statements;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.Statement;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.parsing.expressions.StaticScalar;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 08.11.2007
 */
public class DeclareStatement implements PHPTokenTypes
{

	//		kwDECLARE '(' declare_list ')' declare_statement
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwDECLARE))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();

		builder.match(chLPAREN);
		parseDeclareList(builder);
		builder.match(chRPAREN);
		parseDeclareStatement(builder);

		statement.done(PHPElementTypes.DECLARE);
		return PHPElementTypes.DECLARE;
	}

	//	declare_statement:
	//		statement
	//		| ':' statement_list kwENDDECLARE ';'
	//	;
	private static void parseDeclareStatement(PHPPsiBuilder builder)
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
	private static void parseDeclareList(PHPPsiBuilder builder)
	{
		ParserPart listParser = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				if(!builder.compare(IDENTIFIER))
				{
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker directive = builder.mark();
				builder.advanceLexer();
				builder.match(opASGN);
				StaticScalar.parse(builder);
				directive.done(PHPElementTypes.DECLARE_DIRECTIVE);
				return PHPElementTypes.DECLARE_DIRECTIVE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, listParser.parse(builder), listParser, false);
	}
}
