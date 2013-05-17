package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date May 15, 2008 11:23:18 AM
 */
public interface FieldReference extends PHPPsiElement, PsiReference {

  public boolean canReadName();

  @Nullable
  public String getFieldName();

  @Nullable
  public ClassReference getClassReference();

  @Nullable
  public PsiElement getObjectReference();

  public PhpModifier getReferenceType();

}
