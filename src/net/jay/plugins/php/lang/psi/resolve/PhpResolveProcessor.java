package net.jay.plugins.php.lang.psi.resolve;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.List;
import java.util.ArrayList;

/**
 * @author jay
 * @date Apr 15, 2008 10:10:23 AM
 */
public class PhpResolveProcessor extends PhpScopeProcessor {
  private List<PsiElement> result = new ArrayList<PsiElement>();

  public PhpResolveProcessor(PsiNamedElement element) {
    super(element);
  }

  public List<PsiElement> getResult() {
    return result;
  }

  public boolean execute(PsiElement psiElement) {
    if (psiElement instanceof PsiNamedElement) {
      //noinspection ConstantConditions
      if (
        isAppropriateDeclarationType(psiElement)
        && ((PsiNamedElement) psiElement).getName() != null
        && ((PsiNamedElement) psiElement).getName().equals(element.getName())
        && !result.contains(psiElement)) {
        result.add(psiElement);
      }
    }
    return true;
  }

}
