package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.expressions.StaticScalar;
import org.consulo.php.lang.parser.util.ListParsingHelper;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;
import org.consulo.php.lang.parser.util.ParserPart;

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
	//		class_variable_declaration ',' VARIABLE_REFERENCE
	//		| class_variable_declaration ',' VARIABLE_REFERENCE '=' static_scalar
	//		| VARIABLE_REFERENCE
	//		| VARIABLE_REFERENCE '=' static_scalar
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(ClassMemberModifiers.parseVariableModifiers(builder) == PHPElementTypes.EMPTY_INPUT)
		{
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
			return PHPElementTypes.EMPTY_INPUT;
		}
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, result, fieldParser, false);
		return null;
	}

}
