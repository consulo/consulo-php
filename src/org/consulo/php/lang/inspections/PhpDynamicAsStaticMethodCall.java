package org.consulo.php.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.php.PhpBundle;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpMethodReference;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 16, 2008 11:07:06 PM
 */
public class PhpDynamicAsStaticMethodCall extends PhpInspection
{
	@Override
	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.dynamic_as_static_method_call");
	}

	@Override
	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			@SuppressWarnings({"ConstantConditions"})
			public void visitMethodReference(PhpMethodReference reference)
			{
				if(reference.canReadName() && reference.isStatic())
				{
					final PsiElement element = reference.getReference().resolve();
					if(element instanceof PhpFunction)
					{
						final PhpFunction phpMethod = (PhpFunction) element;
						if(!phpMethod.hasModifier(PhpTokenTypes.STATIC_KEYWORD))
						{
							holder.registerProblem(reference, PhpBundle.message("php.inspections.dynamic_as_static_method_call"));
						}
					}
				}
			}
		};
	}
}
