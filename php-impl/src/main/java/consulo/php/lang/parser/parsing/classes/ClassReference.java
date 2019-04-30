package consulo.php.lang.parser.parsing.classes;

import javax.annotation.Nullable;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.BitUtil;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.parsing.calls.Variable;
import consulo.php.lang.parser.util.PhpParserErrors;
import consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @time 27.10.2007
 */
public class ClassReference implements PhpTokenTypes
{
	public static final int ALLOW_STATIC = 1 << 0;
	public static final int ALLOW_DYNAMIC = 1 << 1;
	public static final int ALLOW_ARRAY = 1 << 2;

	@Deprecated
	@Nullable
	public static PsiBuilder.Marker parse(PhpPsiBuilder builder)
	{
		return parseClassNameReference(builder, null, 0);
	}

	@Nullable
	@Deprecated
	public static PsiBuilder.Marker parseClassNameReference(PhpPsiBuilder builder, PsiBuilder.Marker m, boolean allowStatic, boolean dynamic)
	{
		int flags = 0;
		flags = BitUtil.set(flags, ALLOW_STATIC, allowStatic);
		flags = BitUtil.set(flags, ALLOW_DYNAMIC, dynamic);
		return parseClassNameReference(builder, m, flags);
	}

	@Nullable
	public static PsiBuilder.Marker parseClassNameReference(PhpPsiBuilder builder, PsiBuilder.Marker m, int flags)
	{
		PsiBuilder.Marker marker = m != null ? m.precede() : builder.mark();
		if(BitUtil.isSet(flags, ALLOW_STATIC) && builder.getTokenType() == STATIC_KEYWORD)
		{
			builder.advanceLexer();
			marker.done(PhpElementTypes.CLASS_REFERENCE);
			return marker;
		}

		if(BitUtil.isSet(flags, ALLOW_ARRAY) && builder.getTokenType() == kwARRAY)
		{
			builder.advanceLexer();
			marker.done(PhpElementTypes.CLASS_REFERENCE);
			return marker;
		}

		if(BitUtil.isSet(flags, ALLOW_DYNAMIC))
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
				marker = parseClassNameReference(builder, marker, flags);
			}

			if(marker == null)
			{
				return null;
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
