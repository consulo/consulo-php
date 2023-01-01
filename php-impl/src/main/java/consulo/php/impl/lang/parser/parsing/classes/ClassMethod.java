package consulo.php.impl.lang.parser.parsing.classes;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.parsing.ReturnType;
import consulo.php.impl.lang.parser.parsing.StatementList;
import consulo.php.impl.lang.parser.parsing.functions.IsReference;
import consulo.php.impl.lang.parser.parsing.functions.ParameterList;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 27.10.2007
 */
public class ClassMethod implements PhpTokenTypes
{
	public static IElementType parseMethod(PhpPsiBuilder builder)
	{
		builder.match(kwFUNCTION);
		IsReference.parse(builder);
		builder.match(IDENTIFIER);

		ParameterList.parseFunctionParamList(builder);

		ReturnType.parseIfColon(builder);

		if(builder.getTokenType() == opSEMICOLON)
		{
			builder.advanceLexer();
			return PhpStubElements.CLASS_METHOD;
		}
		builder.match(LBRACE);
		StatementList.parse(builder, RBRACE);
		builder.match(RBRACE);

		builder.compareAndEat(opSEMICOLON);

		return PhpStubElements.CLASS_METHOD;
	}
}
