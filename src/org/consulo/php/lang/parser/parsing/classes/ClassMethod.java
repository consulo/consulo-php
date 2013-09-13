package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.parsing.StatementList;
import net.jay.plugins.php.lang.parser.parsing.functions.IsReference;
import net.jay.plugins.php.lang.parser.parsing.functions.ParameterList;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 27.10.2007
 */
public class ClassMethod implements PHPTokenTypes
{

	//	method_modifiers kwFUNCTION is_reference IDENTIFIER '(' parameter_list ')' method_body
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(tsMODIFIERS) && !builder.compare(kwFUNCTION))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker method = builder.mark();
		ClassMemberModifiers.parseMethodModifiers(builder);
		if(!builder.compare(kwFUNCTION))
		{
			method.rollbackTo();
			return PHPElementTypes.EMPTY_INPUT;
		}
		builder.match(kwFUNCTION);
		IsReference.parse(builder);
		builder.match(IDENTIFIER);

		builder.match(chLPAREN);
		ParameterList.parse(builder);
		builder.match(chRPAREN);

		IElementType result = parseMethodBody(builder);
		method.done(result);
		return result;
	}

	//	method_body:
	//		';' /* abstract method */
	//		| '{' statement_list '}'
	//	;
	private static IElementType parseMethodBody(PHPPsiBuilder builder)
	{
		if(builder.compareAndEat(opSEMICOLON))
		{
			return PHPElementTypes.CLASS_METHOD;
		}
		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		return PHPElementTypes.CLASS_METHOD;
	}
}
