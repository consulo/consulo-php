package consulo.php.impl.lang.parser.parsing.classes;

import consulo.language.ast.IElementType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.parsing.expressions.Expression;
import consulo.php.impl.lang.parser.util.ListParsingHelper;
import consulo.php.impl.lang.parser.util.ParserPart;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 27.10.2007
 */
public class ClassField implements PhpTokenTypes
{

	//	variable_modifiers class_variable_declaration

	//	class_variable_declaration:
	//		class_variable_declaration ',' VARIABLE_REFERENCE
	//		| class_variable_declaration ',' VARIABLE_REFERENCE '=' static_scalar
	//		| VARIABLE_REFERENCE
	//		| VARIABLE_REFERENCE '=' static_scalar
	//	;
	public static IElementType parse(PhpPsiBuilder builder, final boolean constValue)
	{
		ParserPart fieldParser = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				IElementType type = constValue ? IDENTIFIER : VARIABLE;
				if(!builder.compare(type))
				{
					return PhpElementTypes.EMPTY_INPUT;
				}

				builder.match(type);
				if(builder.compareAndEat(opASGN))
				{
					Expression.parse(builder);
				}

				return PhpElementTypes.CLASS_FIELD;
			}
		};
		IElementType result = fieldParser.parse(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, fieldParser, false);
		builder.match(opSEMICOLON);
		return result;
	}

}
