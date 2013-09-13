package org.consulo.php.lang.parser.parsing.functions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:29
 */
public class Function implements PHPTokenTypes
{

	//	function_declaration_statement:
	//		kwFUNCTION is_reference IDENTIFIER '(' parameter_list ')'
	//			'{' statement_list '}'
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(kwFUNCTION))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker function = builder.mark();
		builder.advanceLexer();
		IsReference.parse(builder);
		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PHPParserErrors.expected("function name"));
		}
		builder.match(chLPAREN);
		ParameterList.parse(builder);
		builder.match(chRPAREN);
		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		function.done(PHPElementTypes.FUNCTION);
		return PHPElementTypes.FUNCTION;
	}
}
