package net.jay.plugins.php.lang.documentation.phpdoc.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.documentation.phpdoc.parser.tags.PhpDocDefaultTagParser;
import net.jay.plugins.php.lang.documentation.phpdoc.parser.tags.PhpDocReturnTagParser;
import net.jay.plugins.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import net.jay.plugins.php.lang.parser.util.PHPPsiBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 28, 2008 4:38:44 PM
 */
public class PhpDocParser implements PsiParser {

  public PhpDocParser() {
    PhpDocDefaultTagParser.register();
    PhpDocVarTagParser.register();
    PhpDocReturnTagParser.register();
  }

  @NotNull
  public ASTNode parse(IElementType root, PsiBuilder builder) {
    PHPPsiBuilder phpBuilder = new PHPPsiBuilder(builder);
    PsiBuilder.Marker rootMarker = phpBuilder.mark();
    new PhpDocParsing().parse(phpBuilder);
    rootMarker.done(root);
    return phpBuilder.getTreeBuilt();
  }
}
