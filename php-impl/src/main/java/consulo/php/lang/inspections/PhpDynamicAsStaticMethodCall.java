package consulo.php.lang.inspections;

import javax.annotation.Nonnull;

import consulo.php.PhpBundle;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpMethodReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.Nls;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

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
