package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 26.10.2007
 */
public class ClassStatementList implements PHPTokenTypes
{

	//	class_statement_list:
	//		class_statement_list class_statement
	//		| /* empty */
	//	;
	public static void parse(PhpPsiBuilder builder)
	{
		while(!builder.eof() && !builder.compare(chRBRACE))
		{
			IElementType result = parseStatement(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
				builder.advanceLexer();
			}
		}
	}

	//	class_statement:
	//		variable_modifiers class_variable_declaration ';'
	//		| class_constant_declaration ';'
	//		| method_modifiers kwFUNCTION is_reference IDENTIFIER
	//			'(' parameter_list ')' method_body
	//	;
	private static IElementType parseStatement(PhpPsiBuilder builder)
	{
		IElementType result = ClassConstant.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			builder.match(opSEMICOLON);
		}
		else
		{
			result = ClassField.parse(builder);
			if(result != PhpElementTypes.EMPTY_INPUT)
			{
				builder.match(opSEMICOLON);
			}
			else
			{
				result = ClassMethod.parse(builder);
			}
		}
		return result;
	}

}
