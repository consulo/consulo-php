package net.jay.plugins.php.lang.documentation.phpdoc.parser.tags;

import com.intellij.util.containers.HashMap;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;

import java.util.Map;

/**
 * @author jay
 * @date Jun 28, 2008 6:02:26 PM
 */
public class PhpDocTagParserRegistry {

  private static PhpDocTagParser defaultParser;
  private static Map<String, PhpDocTagParser> parsers = new HashMap<String, PhpDocTagParser>();

  public static void register(PhpDocTagParser parser) {
    if (parser.getName() == null) {
      defaultParser = parser;
    } else {
      parsers.put(parser.getName(), parser);
    }
  }

  public static void parse(PHPPsiBuilder builder) {
    final PhpDocTagParser tagParser = parsers.get(builder.getTokenText());
    if (tagParser != null) {
      tagParser.parse(builder);
    } else {
      defaultParser.parse(builder);
    }
  }

}
