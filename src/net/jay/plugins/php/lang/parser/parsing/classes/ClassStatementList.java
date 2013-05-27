package net.jay.plugins.php.lang.parser.parsing.classes;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.parser.util.PHPParserErrors;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

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
	public static void parse(PHPPsiBuilder builder)
	{
		while(!builder.eof() && !builder.compare(chRBRACE))
		{
			IElementType result = parseStatement(builder);
			if(result == PHPElementTypes.EMPTY_INPUT)
			{
				builder.error(PHPParserErrors.unexpected(builder.getTokenType()));
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
	private static IElementType parseStatement(PHPPsiBuilder builder)
	{
		IElementType result = ClassConstant.parse(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			builder.match(opSEMICOLON);
		}
		else
		{
			result = ClassField.parse(builder);
			if(result != PHPElementTypes.EMPTY_INPUT)
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
