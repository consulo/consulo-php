package org.consulo.php.lang.documentation.phpdoc.parser.tags;

import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.parser.util.PHPPsiBuilder;

/**
 * @author jay
 * @date Jun 28, 2008 6:02:39 PM
 */
abstract public class PhpDocTagParser implements PhpDocElementTypes
{

	protected PhpDocTagParser()
	{
	}

	public static void register()
	{
	}

	abstract public String getName();

	abstract public void parse(PHPPsiBuilder builder);

}