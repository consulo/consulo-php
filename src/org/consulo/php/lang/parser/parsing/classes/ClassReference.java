package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.util.PHPParserErrors;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 27.10.2007
 */
public class ClassReference implements PHPTokenTypes
{

	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(builder.compare(IDENTIFIER))
		{
			PsiBuilder.Marker marker = builder.mark();
			builder.advanceLexer();
			marker.done(PHPElementTypes.CLASS_REFERENCE);
			return PHPElementTypes.CLASS_REFERENCE;
		}
		return PHPElementTypes.EMPTY_INPUT;
	}

	//	class_name_reference:
	//		IDENTIFIER
	//		| dynamic_class_name_reference
	//	;
	public static IElementType parseClassNameReference(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = parseDynamicClassNameReference(builder);
		if(result == PHPElementTypes.EMPTY_INPUT)
		{
			if(builder.compareAndEat(IDENTIFIER))
			{
				marker.done(PHPElementTypes.CLASS_REFERENCE);
				return PHPElementTypes.CLASS_REFERENCE;
			}
			else
			{
				marker.drop();
			}
		}
		else
		{
			marker.done(PHPElementTypes.CLASS_REFERENCE);
			result = PHPElementTypes.CLASS_REFERENCE;
		}
		return result;
	}

	//	dynamic_class_name_reference:
	//		base_variable ARROW object_property dynamic_class_name_variable_properties
	//		| base_variable
	//	;
	//
	//	dynamic_class_name_variable_properties:
	//		dynamic_class_name_variable_properties dynamic_class_name_variable_property
	//		| /* empty */
	//	;
	//
	//	dynamic_class_name_variable_property:
	//		ARROW object_property
	//	;
	private static IElementType parseDynamicClassNameReference(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = Variable.parseBaseVariable(builder);
		if(result != PHPElementTypes.EMPTY_INPUT)
		{
			while(builder.compareAndEat(ARROW))
			{
				marker.done(result);
				marker = marker.precede();
				result = Variable.parseObjectProperty(builder);
				if(result == PHPElementTypes.EMPTY_INPUT)
				{
					builder.error(PHPParserErrors.expected("object property"));
				}
			}
		}
		marker.drop();
		return result;
	}

}
