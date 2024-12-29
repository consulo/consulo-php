package consulo.php.impl.lang.highlighter.hierarchy;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.language.editor.ui.DefaultPsiElementCellRenderer;
import consulo.language.editor.ui.PopupNavigationUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.logging.Logger;
import consulo.navigation.Navigatable;
import consulo.ui.ex.RelativePoint;

import jakarta.annotation.Nonnull;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 25, 2008 1:49:36 AM
 */
public class PhpGutterNavigator
{
	private static final Logger LOG = Logger.getInstance(PhpGutterNavigator.class);

	public static void browse(@Nonnull final MouseEvent e, @Nonnull final PhpGutterInfo info)
	{
		final ArrayList<PsiElement> navigatable = new ArrayList<>();
		final List<PsiElement> views = new ArrayList<>();

		for(PhpNamedElement psiElement : info.getElements())
		{
			if(psiElement instanceof Function)
			{
				views.add(PsiTreeUtil.getParentOfType(psiElement, PhpClass.class));
			}
			else
			{
				views.add(psiElement);
			}

			if(psiElement instanceof Navigatable)
			{
				navigatable.add(psiElement);
			}
		}

		LOG.assertTrue(views.size() == navigatable.size());

		PopupNavigationUtil.getPsiElementPopup(navigatable.toArray(new PsiElement[navigatable.size()]), new DefaultPsiElementCellRenderer(), info.getTitle()).show(new RelativePoint(e));
	}
}
