package consulo.php.test.lexer;

import consulo.language.ast.IElementType;
import consulo.php.PhpLanguageLevel;
import consulo.php.impl.lang.lexer.PhpFlexLexer;
import consulo.testFramework.MockApplicationTestCase;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public class PhpLexerTest extends MockApplicationTestCase
{
	// test which is test - eat heredoc $~x or not - it will throw assertion if failed(check for in lexer)
	public void testHeredocProblem()
	{
		String text = "<?php     private static function getTimestampRegex(): string\n" +
				"    {\n" +
				"        return <<<EOF\n" +
				"        ~^\n" +
				"        (?P<year>[0-9][0-9][0-9][0-9])\n" +
				"        -(?P<month>[0-9][0-9]?)\n" +
				"        -(?P<day>[0-9][0-9]?)\n" +
				"        (?:(?:[Tt]|[ \\t]+)\n" +
				"        (?P<hour>[0-9][0-9]?)\n" +
				"        :(?P<minute>[0-9][0-9])\n" +
				"        :(?P<second>[0-9][0-9])\n" +
				"        (?:\\.(?P<fraction>[0-9]*))?\n" +
				"        (?:[ \\t]*(?P<tz>Z|(?P<tz_sign>[-+])(?P<tz_hour>[0-9][0-9]?)\n" +
				"        (?::(?P<tz_minute>[0-9][0-9]))?))?)?\n" +
				"        $~x\n" +
				"EOF;\n" +
				"    }" +
				"?>";

		PhpFlexLexer lexer = new PhpFlexLexer(false, PhpLanguageLevel.HIGHEST);
		lexer.start(text);

		IElementType elementType = null;
		while((elementType = lexer.getTokenType()) != null)
		{
			lexer.advance();
		}
	}
}
