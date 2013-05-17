package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 18, 2008 12:32:13 PM
 */
public class PhpTypedElementImpl extends PHPPsiElementImpl implements PhpTypedElement {

  public PhpTypedElementImpl(ASTNode node) {
    super(node);
  }

  @NotNull
  public PhpType getType() {
    PhpType type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
    if (type == null) {
      PhpTypeAnnotatorVisitor.process(this);
    }
    type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
    assert type != null;
    return type;
  }
}
