package consulo.php.lang.inspections;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nls;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import consulo.annotations.RequiredReadAction;
import consulo.php.PhpBundle;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 17, 2008 12:48:26 AM
 */
public class PhpUndefinedMethodCall extends PhpInspection
{
	@Override
	@Nls
	@Nonnull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.undefined_method_call");
	}

	@Override
	@Nonnull
	public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			@RequiredReadAction
			public void visitMethodReference(MethodReference reference)
			{
				if(reference.canReadName())
				{
					PsiElement firstChild = reference.getFirstPsiChild();
					if(firstChild instanceof PsiReference)
					{
						PsiElement classReferenceResult = ((PsiReference) firstChild).resolve();
						if(classReferenceResult == null)
						{
							return;
						}
					}

					final PsiElement element = reference.resolve();
					if(element == null)
					{
						holder.registerProblem(reference.getNameIdentifier(), PhpBundle.message("php.inspections.undefined_method_call"));
					}
				}
			}
		};
	}
}
