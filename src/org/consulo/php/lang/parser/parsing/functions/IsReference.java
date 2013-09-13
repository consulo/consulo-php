package org.consulo.php.lang.parser.parsing.functions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;

/**
 * @author markov
 * @date 13.10.2007
 */
public class IsReference
{

	public static void parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker isReference = builder.mark();
		builder.compareAndEat(PHPTokenTypes.opBIT_AND);
		isReference.done(PhpElementTypes.IS_REFERENCE);
	}
}
