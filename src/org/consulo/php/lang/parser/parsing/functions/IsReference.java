package org.consulo.php.lang.parser.parsing.functions;

import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.parser.PHPElementTypes;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;

/**
 * @author markov
 * @date 13.10.2007
 */
public class IsReference
{

	public static void parse(PHPPsiBuilder builder)
	{
		PsiBuilder.Marker isReference = builder.mark();
		builder.compareAndEat(PHPTokenTypes.opBIT_AND);
		isReference.done(PHPElementTypes.IS_REFERENCE);
	}
}
