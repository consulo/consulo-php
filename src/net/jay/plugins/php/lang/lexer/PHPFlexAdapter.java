package net.jay.plugins.php.lang.lexer;

import com.intellij.lexer.FlexAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPFlexAdapter extends FlexAdapter
{

	public PHPFlexAdapter()
	{
		super(new PHPFlexLexer(false));
	}

}
