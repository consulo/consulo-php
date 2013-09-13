package org.consulo.php.lang.inspections.classes;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.ProblemDescriptorImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.consulo.php.PHPBundle;
import net.jay.plugins.php.lang.inspections.PhpInspection;
import org.consulo.php.lang.psi.elements.ExtendsList;
import org.consulo.php.lang.psi.elements.ImplementsList;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.consulo.php.lang.psi.elements.PhpMethod;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 8:29:52 PM
 */
public class PhpUnimplementedMethodsInClass extends PhpInspection
{
	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PHPBundle.message("php.inspections.unimplemented_methods_in_class");
	}

	/**
	 * @return highlighting level for this inspection tool that is used in default settings
	 */
	@NotNull
	public HighlightDisplayLevel getDefaultLevel()
	{
		return HighlightDisplayLevel.ERROR;
	}

	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PHPElementVisitor()
		{
			public void visitPhpClass(PhpClass clazz)
			{
				if(clazz.isAbstract())
				{
					return;
				}

				final PhpMethod[] methods = clazz.getMethods();
				final List<PhpMethod> abstractMethods = new ArrayList<PhpMethod>();
				for(PhpMethod method : methods)
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
						for(Iterator<PhpMethod> methodIterator = abstractMethods.iterator(); methodIterator.hasNext(); )
						{
							PhpMethod abstractMethod = methodIterator.next();
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
					ProblemDescriptor descriptor = new ProblemDescriptorImpl(clazz.getFirstChild(), endNode, PHPBundle.message("php.inspections.unimplemented_methods_in_class.descr", methodList.toString()), new LocalQuickFix[0], ProblemHighlightType.GENERIC_ERROR_OR_WARNING, false, null, false);
					holder.registerProblem(descriptor);
				}
			}
		};
	}
}
