package net.jay.plugins.php.lang.inspections;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.lang.psi.elements.MethodReference;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;

/**
 * @author jay
 * @date Jun 17, 2008 12:48:26 AM
 */
public class PhpUndefinedMethodCall extends PhpInspection
{
	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PHPBundle.message("php.inspections.undefined_method_call");
	}

	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PHPElementVisitor()
		{
			public void visitPhpMethodReference(MethodReference reference)
			{
				if(reference.canReadName())
				{
					//noinspection ConstantConditions
					final ResolveResult[] results = ((PsiPolyVariantReference) reference.getReference()).multiResolve(false);
					if(results.length == 0)
					{
						holder.registerProblem(reference.getNameNode().getPsi(), PHPBundle.message("php.inspections.undefined_method_call"));
					}
				}
			}
		};
	}
}
