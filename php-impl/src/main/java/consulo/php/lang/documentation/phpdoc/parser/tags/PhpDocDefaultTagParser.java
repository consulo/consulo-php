package consulo.php.lang.documentation.phpdoc.parser.tags;

import consulo.php.lang.parser.util.PhpPsiBuilder;
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

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public void parse(PhpPsiBuilder builder)
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
