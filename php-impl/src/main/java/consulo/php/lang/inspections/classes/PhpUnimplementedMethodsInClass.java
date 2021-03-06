package consulo.php.lang.inspections.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nls;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.ProblemDescriptorImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.php.PhpBundle;
import consulo.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import com.jetbrains.php.lang.psi.elements.ImplementsList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 24, 2008 8:29:52 PM
 */
public class PhpUnimplementedMethodsInClass extends PhpInspection
{
	@Override
	@Nls
	@Nonnull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.unimplemented_methods_in_class");
	}

	/**
	 * @return highlighting level for this inspection tool that is used in default settings
	 */
	@Override
	@Nonnull
	public HighlightDisplayLevel getDefaultLevel()
	{
		return HighlightDisplayLevel.ERROR;
	}

	@Override
	@Nonnull
	public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			public void visitClass(PhpClass clazz)
			{
				if(clazz.getModifier().isAbstract())
				{
					return;
				}

				final Function[] methods = clazz.getOwnMethods();
				final List<Function> abstractMethods = new ArrayList<Function>();
				for(Function method : methods)
				{
					if(method.getModifier().isAbstract())
					{
						abstractMethods.add(method);
					}
				}

				if(abstractMethods.size() > 0)
				{
					StringBuffer methodList = new StringBuffer();
					if(abstractMethods.size() == 1)
					{
						methodList.append("method '").append(abstractMethods.get(0).getName()).append("'");
					}
					else
					{
						methodList.append("methods ");
						for(Iterator<Function> methodIterator = abstractMethods.iterator(); methodIterator.hasNext(); )
						{
							Function abstractMethod = methodIterator.next();
							methodList.append("'").append(abstractMethod.getName()).append("'");
							if(methodIterator.hasNext())
							{
								methodList.append(", ");
							}
						}
					}

					PsiElement endNode;
					if(clazz.getImplementedInterfaces().length > 0)
					{
						endNode = PsiTreeUtil.getChildOfType(clazz, ImplementsList.class);
					}
					else if(clazz.getSuperClass() != null)
					{
						endNode = PsiTreeUtil.getChildOfType(clazz, ExtendsList.class);
					}
					else if(clazz.getNameIdentifier() != null)
					{
						endNode = clazz.getNameIdentifier();
					}
					else
					{
						endNode = clazz.getFirstChild();
					}

					//noinspection ConstantConditions
					ProblemDescriptor descriptor = new ProblemDescriptorImpl(clazz.getFirstChild(), endNode, PhpBundle.message("php.inspections.unimplemented_methods_in_class.descr", methodList.toString()), new LocalQuickFix[0], ProblemHighlightType.GENERIC_ERROR_OR_WARNING, false, null, false);
					holder.registerProblem(descriptor);
				}
			}
		};
	}
}
