package net.jay.plugins.php.lang.psi.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date May 15, 2008 12:35:16 PM
 */
public interface MethodReference extends PHPPsiElement, PsiPolyVariantReference, PhpTypedElement {
  boolean canReadName();

  String getMethodName();

  @Nullable
  ClassReference getClassReference();

  @Nullable
  PsiElement getObjectReference();

  PhpModifier getReferenceType();

  ASTNode getNameNode();
}
