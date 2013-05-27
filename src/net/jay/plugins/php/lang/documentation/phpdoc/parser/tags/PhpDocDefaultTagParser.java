package net.jay.plugins.php.lang.documentation.phpdoc.parser.tags;

import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import com.intellij.lang.PsiBuilder;

/**
 * @author jay
 * @date Jun 28, 2008 6:05:28 PM
 */
public class PhpDocDefaultTagParser extends PhpDocTagParser
{

	public static void register()
	{
		PhpDocTagParserRegistry.register(new PhpDocDefaultTagParser());
	}

	public String getName()
	{
		return null;
	}

	public void parse(PHPPsiBuilder builder)
	{
		final PsiBuilder.Marker tag = builder.mark();
		builder.match(DOC_TAG_NAME);
		final PsiBuilder.Marker tagValue = builder.mark();
		while(!builder.compare(DOC_TAG_VALUE_END) && !builder.eof())
		{
			builder.advanceLexer();
		}
		tagValue.done(phpDocTagValue);
		tag.done(phpDocTag);
	}
}
