package consulo.php.lang.parser.parsing.statements;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.parsing.expressions.Expression;
import consulo.php.lang.parser.util.ListParsingHelper;
import consulo.php.lang.parser.util.ParserPart;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 04.11.2007
 */
public class GlobalStatement implements PhpTokenTypes
{

	//	kwGLOBAL global_var_list ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compareAndEat(kwGLOBAL))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		parseGlobalVarList(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PhpElementTypes.GLOBAL);
		return PhpElementTypes.GLOBAL;
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
	private static void parseGlobalVarList(PhpPsiBuilder builder)
	{
		ParserPart globalVariable = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				if(!builder.compare(TokenSet.create(VARIABLE, DOLLAR)))
				{
					return PhpElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker variable = builder.mark();
				if(builder.compareAndEat(VARIABLE))
				{
					variable.done(PhpElementTypes.VARIABLE_REFERENCE);
					return PhpElementTypes.VARIABLE_REFERENCE;
				}
				builder.match(DOLLAR);
				if(builder.compareAndEat(LBRACE))
				{
					Expression.parse(builder);
					builder.match(RBRACE);
					variable.done(PhpElementTypes.VARIABLE_REFERENCE);
					return PhpElementTypes.VARIABLE_REFERENCE;
				}
				Variable.parse(builder);
				variable.done(PhpElementTypes.VARIABLE_REFERENCE);
				return PhpElementTypes.VARIABLE_REFERENCE;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, globalVariable.parse(builder), globalVariable, false);
	}
}
