package consulo.php.impl.lang.documentation.phpdoc.parser.tags;

import consulo.php.impl.lang.parser.util.PhpPsiBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @date Jun 28, 2008 6:02:26 PM
 */
public class PhpDocTagParserRegistry
{
	private static PhpDocTagParser defaultParser;
	private static Map<String, PhpDocTagParser> parsers = new HashMap<String, PhpDocTagParser>();

	public static void register(PhpDocTagParser parser)
	{
		if(parser.getName() == null)
		{
			defaultParser = parser;
		}
		else
		{
			parsers.put(parser.getName(), parser);
		}
	}

	public static void parse(PhpPsiBuilder builder)
	{
		final PhpDocTagParser tagParser = parsers.get(builder.getTokenText());
		if(tagParser != null)
		{
			tagParser.parse(builder);
		}
		else
		{
			defaultParser.parse(builder);
		}
	}

}
