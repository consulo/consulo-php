package org.consulo.php.lang.lexer;

import com.intellij.lexer.FlexAdapter;
import org.consulo.php.PhpLanguageLevel;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpFlexAdapter extends FlexAdapter
{

	public PhpFlexAdapter(PhpLanguageLevel languageLevel)
	{
		super(new PhpFlexLexer(false, languageLevel));
	}

}
