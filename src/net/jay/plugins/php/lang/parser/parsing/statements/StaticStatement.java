package net.jay.plugins.php.lang.parser.parsing.statements;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.expressions.StaticScalar;
import net.jay.plugins.php.lang.parser.util.ListParsingHelper;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import net.jay.plugins.php.lang.parser.util.ParserPart;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.11.2007
 */
public class StaticStatement implements PHPTokenTypes
{

	//	kwSTATIC static_var_list ';'
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwSTATIC))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();
		parseStaticVarList(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PHPElementTypes.STATIC);
		return PHPElementTypes.STATIC;
	}

	//	static_var_list:
	//		static_var_list ',' VARIABLE
	//		| static_var_list ',' VARIABLE '=' static_scalar
	//		| VARIABLE
	//		| VARIABLE '=' static_scalar
	//	;
	private static void parseStaticVarList(PHPPsiBuilder builder)
	{
		ParserPart staticVariable = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				if(!builder.compare(VARIABLE))
				{
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker var = builder.mark();
				builder.match(VARIABLE);
				if(builder.compareAndEat(opASGN))
				{
					StaticScalar.parse(builder);
				}
				var.done(PHPElementTypes.VARIABLE);
				return PHPElementTypes.VARIABLE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, staticVariable.parse(builder), staticVariable, false);
	}
}