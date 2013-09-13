package org.consulo.php.lang.inspections;

import org.consulo.php.PHPBundle;
import org.consulo.php.lang.psi.elements.PhpMethod;
import org.consulo.php.lang.psi.elements.PhpMethodReference;
import org.consulo.php.lang.psi.elements.PhpModifier;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author jay
 * @date Jun 16, 2008 11:07:06 PM
 */
public class PhpDynamicAsStaticMethodCall extends PhpInspection
{
	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PHPBundle.message("php.inspections.dynamic_as_static_method_call");
	}

	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PHPElementVisitor()
		{
			@SuppressWarnings({"ConstantConditions"})
			public void visitPhpMethodReference(PhpMethodReference reference)
			{
				if(reference.canReadName() && reference.getReferenceType().getState() == PhpModifier.State.STATIC)
				{
					final PsiElement element = reference.getReference().resolve();
					if(element instanceof PhpMethod)
					{
						final PhpMethod phpMethod = (PhpMethod) element;
						if(!phpMethod.isStatic())
						{
							holder.registerProblem(reference, PHPBundle.message("php.inspections.dynamic_as_static_method_call"));
						}
					}
				}
			}
		};
	}
}
