package net.jay.plugins.php.lang.phpdoc.lexer;

import java.util.ArrayList;
import java.util.List;

import consulo.language.lexer.Lexer;
import consulo.util.lang.StringUtil;
import net.jay.plugins.php.lang.lexer.LexerTest;
import net.jay.plugins.php.utils.PathUtils;

import org.jetbrains.annotations.NonNls;
import consulo.language.ast.IElementType;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocLexer;
import consulo.php.impl.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import junit.framework.Test;

/**
 * @author jay
 * @date Jun 28, 2008 1:13:51 AM
 */
public class PhpDocLexerTest extends LexerTest {

    @NonNls
    private static final String DATA_PATH = PathUtils.getDataPath(PhpDocLexerTest.class);

    public PhpDocLexerTest() {
        super(DATA_PATH);
    }
//	private static final String DATA_PATH = PathUtils.getDataPath(PhpDocLexerTest.class) + "/live_cases";

    @Override
	public String transform(String testName, String[] data) throws Exception {
        final String fileText = data[0];
        final char[] text = fileText.toCharArray();
        List<IElementType> types = new ArrayList<IElementType>();
        List<String> typeTexts = new ArrayList<String>();
        Lexer lexer = new PhpDocLexer();
        lexer.start(fileText);
        IElementType type = lexer.getTokenType();
        while (type != null) {
            typeTexts.add(getTypeText(text, lexer.getTokenStart(), lexer.getTokenEnd()));
            types.add(type);
            lexer.advance();
            type = lexer.getTokenType();
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < types.size(); i++) {
            IElementType elementType = types.get(i);
            if (elementType != PhpDocTokenTypes.DOC_WHITESPACE) {
                String currentText = typeTexts.get(i);
                currentText = StringUtil.replace(currentText, "\n", "\\n");
                currentText = StringUtil.replace(currentText, "\r", "\\r");
                currentText = StringUtil.replace(currentText, "\t", "\\t");
                buf.append(elementType).append("('").append(currentText).append("')\n");
            }
        }
        return buf.toString();
    }

    public static Test suite() {
        return new PhpDocLexerTest();
    }


}
