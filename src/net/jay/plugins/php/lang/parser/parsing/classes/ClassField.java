package net.jay.plugins.php.lang.parser.parsing.classes;

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
 * Date: 27.10.2007
 */
public class ClassField implements PHPTokenTypes
{

	//	variable_modifiers class_variable_declaration

	//	class_variable_declaration:
	//		class_variable_declaration ',' VARIABLE
	//		| class_variable_declaration ',' VARIABLE '=' static_scalar
	//		| VARIABLE
	//		| VARIABLE '=' static_scalar
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker classFields = builder.mark();
		if(ClassMemberModifiers.parseVariableModifiers(builder) == PHPElementTypes.EMPTY_INPUT)
		{
			classFields.drop();
			return PHPElementTypes.EMPTY_INPUT;
		}
		ParserPart fieldParser = new ParserPart()
		{
			public IElementType parse(PHPPsiBuilder builder)
			{
				if(!builder.compare(VARIABLE))
				{
					return PHPElementTypes.EMPTY_INPUT;
				}
				PsiBuilder.Marker field = builder.mark();
				builder.match(VARIABLE);
				if(builder.compareAndEat(opASGN))
				{
					StaticScalar.parse(builder);
				}
				field.done(PHPElementTypes.CLASS_FIELD);
				return PHPElementTypes.CLASS_FIELD;
			}
		};
		IElementType result = fieldParser.parse(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			classFields.rollbackTo();
			return PHPElementTypes.EMPTY_INPUT;
		}
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, fieldParser, false);
		classFields.done(PHPElementTypes.CLASS_FIELDS);
		return null;
	}

}
