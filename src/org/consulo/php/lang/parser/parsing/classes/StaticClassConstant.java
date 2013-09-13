package org.consulo.php.lang.parser.parsing.classes;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author markov
 * @date 17.10.2007
 */
public class StaticClassConstant implements PHPTokenTypes
{

	//	static_class_constant:
	//		IDENTIFIER SCOPE_RESOLUTION IDENTIFIER
	//	;
	public static IElementType parse(PHPPsiBuilder builder)
	{
		if(!builder.compare(IDENTIFIER))
		{
			return PHPElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker classConstantReference = builder.mark();
		ClassReference.parse(builder);
		if(!builder.compareAndEat(SCOPE_RESOLUTION))
		{
			classConstantReference.rollbackTo();
			return PHPElementTypes.EMPTY_INPUT;
		}
		builder.match(IDENTIFIER);
		classConstantReference.done(PHPElementTypes.CLASS_CONSTANT_REFERENCE);
		return PHPElementTypes.CLASS_CONSTANT_REFERENCE;
	}
}
