package consulo.php.lang.parser.parsing.functions;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.parser.util.PhpPsiBuilder;
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
		builder.compareAndEat(PhpTokenTypes.opBIT_AND);
		isReference.done(PhpElementTypes.IS_REFERENCE);
	}
}
