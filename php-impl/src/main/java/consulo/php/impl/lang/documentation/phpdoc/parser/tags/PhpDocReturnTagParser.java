package consulo.php.impl.lang.documentation.phpdoc.parser.tags;

import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.language.parser.PsiBuilder;

/**
 * @author jay
 * @date Jun 29, 2008 2:30:08 AM
 */
public class PhpDocReturnTagParser extends PhpDocTagParser
{

	public static void register()
	{
		PhpDocTagParserRegistry.register(new PhpDocReturnTagParser());
	}

	@Override
	public String getName()
	{
		return "@return";
	}

	@Override
	public void parse(PhpPsiBuilder builder)
	{
		final PsiBuilder.Marker tag = builder.mark();
		builder.match(DOC_TAG_NAME);
		while(builder.compare(DOC_IDENTIFIER))
		{
			final PsiBuilder.Marker type = builder.mark();
			builder.advanceLexer();
			type.done(phpDocType);
			if(!builder.compareAndEat(DOC_PIPE))
			{
				break;
			}
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
