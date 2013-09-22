package org.consulo.php.lang.lexer;

import org.consulo.php.PhpLanguageLevel;
import com.intellij.lexer.FlexAdapter;

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
