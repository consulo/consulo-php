package net.jay.plugins.php.lang.lexer;

import java.util.ArrayList;
import java.util.List;

import consulo.language.ast.IElementType;
import consulo.language.lexer.Lexer;
import net.jay.plugins.php.testCases.BasePHPFileSetTestCase;
import net.jay.plugins.php.utils.PathUtils;

import org.jetbrains.annotations.NonNls;
import consulo.util.lang.StringUtil;
import consulo.php.PhpLanguageLevel;
import consulo.php.impl.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpTokenTypes;
import junit.framework.Test;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public class LexerTest extends BasePHPFileSetTestCase {

    @NonNls
    private static final String DATA_PATH = PathUtils.getDataPath(LexerTest.class);// + "/heredoc";

    public LexerTest() {
        super(DATA_PATH, LightLexerTest.class);
    }

    public LexerTest(String path) {
        super(path, LightLexerTest.class);
    }

    protected String getTypeText(char[] whole, int start, int end) {
        char[] subString = new char[end - start];
        System.arraycopy(whole, start, subString, 0, end - start);
        return new String(subString);
    }


    @Override
	public String transform(String testName, String[] data) throws Exception {
        final String fileText = data[0];
        final char[] text = fileText.toCharArray();
        List<IElementType> types = new ArrayList<IElementType>();
        List<String> typeTexts = new ArrayList<String>();
        Lexer lexer = new PhpFlexLexer(false, PhpLanguageLevel.HIGHEST);
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
            if (elementType != PhpTokenTypes.WHITE_SPACE) {
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
        return new LexerTest();
    }


}
