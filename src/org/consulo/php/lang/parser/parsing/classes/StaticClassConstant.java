package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticClassConstant implements PhpTokenTypes
{

	//	static_class_constant:
	//		IDENTIFIER SCOPE_RESOLUTION IDENTIFIER
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(IDENTIFIER))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker classConstantReference = builder.mark();
		ClassReference.parse(builder);
		if(!builder.compareAndEat(SCOPE_RESOLUTION))
		{
			classConstantReference.rollbackTo();
			return PhpElementTypes.EMPTY_INPUT;
		}
		builder.match(IDENTIFIER);
		classConstantReference.done(PhpElementTypes.CLASS_CONSTANT_REFERENCE);
		return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
	}
}
