package consulo.php.impl.lang.parser.parsing.functions;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.ReturnType;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:29
 */
public class Function implements PhpTokenTypes
{

	//	function_declaration_statement:
	//		kwFUNCTION is_reference IDENTIFIER '(' parameter_list ')'
	//			'{' statement_list '}'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwFUNCTION))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker function = builder.mark();
		builder.advanceLexer();
		IsReference.parse(builder);
		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PhpParserErrors.expected("function name"));
		}

		ParameterList.parseFunctionParamList(builder);

		ReturnType.parseIfColon(builder);

		builder.match(LBRACE);
		StatementList.parse(builder, RBRACE);
		builder.match(RBRACE);
		function.done(PhpElementTypes.FUNCTION);
		return PhpElementTypes.FUNCTION;
	}
}
