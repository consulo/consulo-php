package consulo.php.lang.parser.parsing.classes;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 27.10.2007
 */
public class ClassReference implements PhpTokenTypes
{
	@Deprecated
	public static PsiBuilder.Marker parse(PhpPsiBuilder builder)
	{
		return parseClassNameReference(builder, null, false, false, false);
	}

	public static PsiBuilder.Marker parseClassNameReference(PhpPsiBuilder builder, PsiBuilder.Marker m, boolean allowStatic, boolean dynamic, boolean allowAs)
	{
		PsiBuilder.Marker marker = m != null ? m.precede() : builder.mark();
		if(allowStatic && builder.getTokenType() == STATIC_KEYWORD)
		{
			builder.advanceLexer();
			marker.done(PhpElementTypes.CLASS_REFERENCE);
			return marker;
		}

		if(dynamic)
		{
			IElementType result = parseDynamicClassNameReference(builder);
			if(result != PhpElementTypes.EMPTY_INPUT)
			{
				marker.done(PhpElementTypes.CLASS_REFERENCE);
				return marker;
			}
		}

		builder.compareAndEat(SLASH);

		if(builder.compareAndEat(IDENTIFIER))
		{
			marker.done(PhpElementTypes.CLASS_REFERENCE);

			if(builder.getTokenType() == SLASH)
			{
				marker = parseClassNameReference(builder, marker, allowStatic, dynamic, false);
			}

			if(marker == null)
			{
				return null;
			}

			if(allowAs && builder.getTokenType() == kwAS)
			{
				marker = marker.precede();

				builder.advanceLexer();

				builder.match(IDENTIFIER);

				marker.done(PhpElementTypes.CLASS_REFERENCE);
			}

			return marker;
		}
		else
		{
			marker.drop();
		}

		return null;
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
