package consulo.php.lang.inspections.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import consulo.php.PhpBundle;
import consulo.php.lang.inspections.PhpInspection;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpExtendsList;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpImplementsList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.ProblemDescriptorImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jun 24, 2008 8:29:52 PM
 */
public class PhpUnimplementedMethodsInClass extends PhpInspection
{
	@Override
	@Nls
	@NotNull
	public String getDisplayName()
	{
		return PhpBundle.message("php.inspections.unimplemented_methods_in_class");
	}

	/**
	 * @return highlighting level for this inspection tool that is used in default settings
	 */
	@Override
	@NotNull
	public HighlightDisplayLevel getDefaultLevel()
	{
		return HighlightDisplayLevel.ERROR;
	}

	@Override
	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PhpElementVisitor()
		{
			@Override
			public void visitClass(PhpClass clazz)
			{
				if(clazz.hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD))
				{
					return;
				}

				final PhpFunction[] methods = clazz.getFunctions();
				final List<PhpFunction> abstractMethods = new ArrayList<PhpFunction>();
				for(PhpFunction method : methods)
				{
					if(method.hasModifier(PhpTokenTypes.ABSTRACT_KEYWORD))
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
						for(Iterator<PhpFunction> methodIterator = abstractMethods.iterator(); methodIterator.hasNext(); )
						{
							PhpFunction abstractMethod = methodIterator.next();
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
						endNode = PsiTreeUtil.getChildOfType(clazz, PhpImplementsList.class);
					}
					else if(clazz.getSuperClass() != null)
					{
						endNode = PsiTreeUtil.getChildOfType(clazz, PhpExtendsList.class);
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
