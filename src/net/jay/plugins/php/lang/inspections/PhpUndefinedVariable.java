package net.jay.plugins.php.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.ArrayUtil;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.completion.PhpCompletionData;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date May 5, 2008 11:26:58 AM
 */
public class PhpUndefinedVariable extends PhpInspection {

  @Nls
  @NotNull
  public String getDisplayName() {
    return PHPBundle.message("php.inspections.undefined_variable");
  }

  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PHPElementVisitor() {
      @SuppressWarnings({"ConstantConditions"})
      public void visitPhpVariable(Variable variable) {
        if (variable.canReadName()) {
          if (ArrayUtil.find(PhpCompletionData.superGlobals, variable.getName()) > -1
            || variable.getName().equals("this")
            || variable.isDeclaration()) {
            return;
          }
          ResolveResult[] results = ((PsiPolyVariantReference) variable.getReference()).multiResolve(false);
          if (results.length == 0) {
            holder.registerProblem(variable, PHPBundle.message("php.inspections.undefined_variable"));
          }
        }
      }
    };
  }
}
