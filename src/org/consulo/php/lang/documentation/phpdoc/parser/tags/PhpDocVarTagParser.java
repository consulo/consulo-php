package org.consulo.php.lang.documentation.phpdoc.parser.tags;

import org.consulo.php.lang.parser.util.PhpPsiBuilder;

import com.intellij.lang.PsiBuilder;

/**
 * @author jay
 * @date Jun 28, 2008 6:09:02 PM
 */
public class PhpDocVarTagParser extends PhpDocTagParser
{

	public static void register()
	{
		PhpDocTagParserRegistry.register(new PhpDocVarTagParser());
	}

	public String getName()
	{
		return "@var";
	}

	public void parse(PhpPsiBuilder builder)
	{
		final PsiBuilder.Marker tag = builder.mark();
		builder.match(DOC_TAG_NAME);
		if(builder.compare(DOC_IDENTIFIER))
		{
			final PsiBuilder.Marker type = builder.mark();
			builder.advanceLexer();
			type.done(phpDocType);
		}
		final PsiBuilder.Marker value = builder.mark();
		while(!builder.compare(DOC_TAG_VALUE_END) && !builder.eof())
		{
			builder.advanceLexer();
		}
		value.done(phpDocTagValue);
		tag.done(phpDocTag);
	}
}
