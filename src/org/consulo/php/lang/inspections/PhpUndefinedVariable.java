package org.consulo.php.lang.inspections;

import org.consulo.php.PhpBundle;
import org.consulo.php.completion.PhpCompletionData;
import org.consulo.php.lang.psi.elements.PhpVariableReference;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.ArrayUtil;

/**
 * @author jay
 * @date May 5, 2008 11:26:58 AM
 */
public class PhpUndefinedVariable extends PhpInspection
{

	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.undefined_variable");
	}

	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PHPElementVisitor()
		{
			@SuppressWarnings({"ConstantConditions"})
			public void visitVariableReference(PhpVariableReference variable)
			{
				if(variable.canReadName())
				{
					if(ArrayUtil.find(PhpCompletionData.superGlobals, variable.getName()) > -1 || variable.getName().equals("this") || variable.isDeclaration())
					{
						return;
					}
					ResolveResult[] results = ((PsiPolyVariantReference) variable.getReference()).multiResolve(false);
					if(results.length == 0)
					{
						holder.registerProblem(variable, PhpBundle.message("php.inspections.undefined_variable"));
					}
				}
			}
		};
	}
}
