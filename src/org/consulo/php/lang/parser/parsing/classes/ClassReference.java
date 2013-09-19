package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.parsing.calls.Variable;
import org.consulo.php.lang.parser.util.PhpParserErrors;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 27.10.2007
 */
public class ClassReference implements PhpTokenTypes
{
	@Deprecated
	public static IElementType parse(PhpPsiBuilder builder)
	{
		return parseClassNameReference(builder, false, false);
	}

	public static IElementType parseClassNameReference(PhpPsiBuilder builder, boolean allowStatic, boolean dynamic)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(allowStatic && builder.getTokenType() == STATIC_KEYWORD) {
			builder.advanceLexer();
			marker.done(PhpElementTypes.CLASS_REFERENCE);
			return PhpElementTypes.CLASS_REFERENCE;
		}

		if(dynamic) {
			IElementType result = parseDynamicClassNameReference(builder);
			if(result != PhpElementTypes.EMPTY_INPUT) {
				marker.done(PhpElementTypes.CLASS_REFERENCE);
				return PhpElementTypes.CLASS_REFERENCE;
			}
		}

		builder.compareAndEat(SLASH);

		if(builder.compareAndEat(IDENTIFIER))
		{
			marker.done(PhpElementTypes.CLASS_REFERENCE);

			if(builder.getTokenType() == SLASH) {
				parseClassNameReference(builder, allowStatic, dynamic);
			}
			return PhpElementTypes.CLASS_REFERENCE;
		}
		else
		{
			marker.drop();
		}

		return PhpElementTypes.EMPTY_INPUT;
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
	private static IElementType parseDynamicClassNameReference(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = Variable.parseBaseVariable(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			while(builder.compareAndEat(ARROW))
			{
				marker.done(result);
				marker = marker.precede();
				result = Variable.parseObjectProperty(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.expected("object property"));
				}
			}
		}
		marker.drop();
		return result;
	}

}
