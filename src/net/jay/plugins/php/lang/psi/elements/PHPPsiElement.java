package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PHPPsiElement extends PsiElement {
  PHPPsiElement getFirstPsiChild();

  PHPPsiElement getNextPsiSibling();

  PHPPsiElement getPrevPsiSibling();

  List<LightCopyContainer> getChildrenForCache();
}
