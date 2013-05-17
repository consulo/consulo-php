package net.jay.plugins.php.lang.documentation.phpdoc.psi;

import com.intellij.psi.PsiComment;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;

/**
 * @author jay
 * @date Jun 29, 2008 12:13:59 AM
 */
public interface PhpDocComment extends PsiComment, PhpDocPsiElement {

  public PhpDocVarTag getVarTag();

  public PhpDocReturnTag getReturnTag();

}
