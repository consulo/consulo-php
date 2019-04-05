package consulo.php.lang.inspections;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nls;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.php.PhpBundle;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 5, 2008 11:26:58 AM
 */
public class PhpUndefinedVariable extends PhpInspection
{

	@Override
	@Nls
	@Nonnull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.undefined_variable");
	}

	@Override
	@Nonnull
	public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			@SuppressWarnings({"ConstantConditions"})
			public void visitVariableReference(Variable variable)
			{
				if(variable.canReadName())
				{
					if(Variable.SUPERGLOBALS.contains(variable.getName()) || variable.getName().equals(Variable.THIS) || variable.isDeclaration())
					{
						return;
					}
					ResolveResult[] results = ((PsiPolyVariantReference) variable.getReference()).multiResolve(false);
					if(results.length == 0)
					{
						holder.registerProblem(variable, PhpBundle.message("php.inspections.undefined_variable.name", variable.getName()));
					}
				}
			}
		};
	}
}
