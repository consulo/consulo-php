package net.jay.plugins.php.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.MethodReference;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 16, 2008 11:07:06 PM
 */
public class PhpDynamicAsStaticMethodCall extends PhpInspection {
  @Nls
  @NotNull
  public String getDisplayName() {
    return PHPBundle.message("php.inspections.dynamic_as_static_method_call");
  }

  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PHPElementVisitor() {
      @SuppressWarnings({"ConstantConditions"})
      public void visitPhpMethodReference(MethodReference reference) {
        if (reference.canReadName() &&
          reference.getReferenceType().getState() == PhpModifier.State.STATIC) {
          final PsiElement element = reference.getReference().resolve();
          if (element instanceof Method) {
            final Method method = (Method) element;
            if (!method.isStatic()) {
              holder.registerProblem(reference, PHPBundle.message("php.inspections.dynamic_as_static_method_call"));
            }
          }
        }
      }
    };
  }
}
