package consulo.php.impl.lang.inspections.classes;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.php.PhpBundle;
import consulo.php.impl.lang.inspections.PhpInspection;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 8:29:52 PM
 */
@ExtensionImpl
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
				if(clazz.getModifier().isAbstract() || clazz.getNameIdentifier() == null)
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

					holder.registerProblem(clazz.getNameIdentifier(), PhpBundle.message("php.inspections.unimplemented_methods_in_class.descr", methodList.toString()));
				}
			}
		};
	}
}
