package consulo.php.lang.inspections;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nls;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import consulo.php.PhpBundle;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 16, 2008 11:07:06 PM
 */
public class PhpDynamicAsStaticMethodCall extends PhpInspection
{
	@Override
	@Nls
	@Nonnull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.dynamic_as_static_method_call");
	}

	@Override
	@Nonnull
	public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			@SuppressWarnings({"ConstantConditions"})
			public void visitMethodReference(MethodReference reference)
			{
				if(reference.canReadName() && reference.isStatic())
				{
					final PsiElement element = reference.getReference().resolve();
					if(element instanceof Function)
					{
						final Function phpMethod = (Function) element;
						if(!phpMethod.getModifier().isStatic())
						{
							holder.registerProblem(reference, PhpBundle.message("php.inspections.dynamic_as_static_method_call"));
						}
					}
				}
			}
		};
	}
}
