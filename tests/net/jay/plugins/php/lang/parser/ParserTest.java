package net.jay.plugins.php.lang.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.DebugUtil;
import junit.framework.Assert;
import junit.framework.Test;
import net.jay.plugins.php.testCases.BasePHPFileSetTestCase;
import net.jay.plugins.php.utils.PathUtils;
import net.jay.plugins.php.utils.TestUtils;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public class ParserTest extends BasePHPFileSetTestCase {

    @NonNls
    private static final String DATA_PATH = PathUtils.getDataPath(ParserTest.class);
//	private static final String DATA_PATH = PathUtils.getDataPath(ParserTest.class) + "/substitutions";

    public ParserTest() {
        super(DATA_PATH);
    }

    public ParserTest(String path) {
        super(path);
    }


    public String transform(String testName, String[] data) throws Exception {
        final String fileText = data[0];

        final PsiFile psiFile = TestUtils.createPseudoPhysicalFile(myProject, fileText);

        final String psiLeafText = gatherTextFromPsiFile(psiFile);
        Assert.assertEquals(fileText, psiLeafText);

        return DebugUtil.psiToString(psiFile, false);
    }

    private String gatherTextFromPsiFile(PsiFile psiFile) {
        final StringBuffer result = new StringBuffer();
        PsiElementVisitor myVisitor = new PhpElementVisitor() {
            public void visitElement(PsiElement element) {
// if child is leaf
                if (element.getFirstChild() == null) {
                    result.append(element.getText());
                } else {
                    element.acceptChildren(this);
                }
            }

        };
        psiFile.accept(myVisitor);
        return result.toString();
    }

    public static Test suite() {
        return new ParserTest();
    }

}
