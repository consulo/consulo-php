package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import net.jay.plugins.php.lang.psi.elements.ImplementsList;
import net.jay.plugins.php.lang.psi.elements.PhpInterface;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

/**
 * @author jay
 * @date Jun 24, 2008 9:21:14 PM
 */
public class ImplementsListImpl extends PHPPsiElementImpl implements ImplementsList {
  public ImplementsListImpl(ASTNode node) {
    super(node);
  }

  @NotNull
  public List<PhpInterface> getInterfaces() {
    List<PhpInterface> result = new ArrayList<PhpInterface>();

    final PsiElement[] children = getChildren();
    for (PsiElement child : children) {
      if (child instanceof ClassReference) {
        //noinspection ConstantConditions
        final PsiElement element = child.getReference().resolve();
        if (element instanceof PhpInterface) {
          result.add((PhpInterface) element);
        }
      }
    }

    return result;
  }
}
