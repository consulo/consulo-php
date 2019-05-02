package net.jay.plugins.php.lang.phpdoc.parser;

import net.jay.plugins.php.lang.parser.ParserTest;
import net.jay.plugins.php.utils.PathUtils;
import junit.framework.Test;

/**
 * @author jay
 * @date Jun 28, 2008 7:12:44 PM
 */
public class PhpDocParserTest extends ParserTest {

    private static final String DATA_PATH = PathUtils.getDataPath(PhpDocParserTest.class);

    public PhpDocParserTest(String dataPath) {
        super(dataPath);
    }

    public static Test _suite() {
        return new PhpDocParserTest(DATA_PATH);
    }
}
