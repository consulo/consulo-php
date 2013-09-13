package org.consulo.php.lang.documentation.phpdoc.parser;

import org.consulo.php.lang.documentation.phpdoc.parser.tags.PhpDocTagParserRegistry;
import org.consulo.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @date Jun 28, 2008 4:39:52 PM
 */
public class PhpDocParsing implements PhpDocElementTypes
{

	public void parse(PhpPsiBuilder builder)
	{
		builder.match(DOC_COMMENT_START);
		while(!builder.compare(DOC_COMMENT_END) && !builder.eof())
		{
			if(builder.compare(DOC_TAG_NAME))
			{
				PhpDocTagParserRegistry.parse(builder);
			}
			else
			{
				builder.advanceLexer();
			}
		}
		builder.match(DOC_COMMENT_END);
	}

}
