package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import net.jay.plugins.php.lang.psi.elements.ExtendsList;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:07:36 PM
 */
public class ExtendsListImpl extends PHPPsiElementImpl implements ExtendsList {
  public ExtendsListImpl(ASTNode node) {
    super(node);
  }

  @Nullable
  public PhpClass getExtendsClass() {
    final PsiElement[] children = getChildren();
    assert children.length <= 1;
    if (children.length > 0) {
      final PsiElement element = children[0];
      if (element instanceof ClassReference) {
        //noinspection ConstantConditions
        final PsiElement resolveResult = element.getReference().resolve();
        if (resolveResult instanceof PhpClass) {
          return (PhpClass) resolveResult;
        }
      }
    }
    return null;
  }
}
