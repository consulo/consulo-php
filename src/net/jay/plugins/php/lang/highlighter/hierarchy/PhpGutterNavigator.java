package net.jay.plugins.php.lang.highlighter.hierarchy;

import com.intellij.ide.util.gotoByName.GotoFileCellRenderer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.awt.RelativePoint;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 25, 2008 1:49:36 AM
 */
public class PhpGutterNavigator {
  private static final Logger LOG = Logger.getInstance(PhpGutterNavigator.class.getName());

  public static void browse(@NotNull final MouseEvent e,
                            @NotNull final PhpGutterInfo info) {
    final Project project = info.getProject();
    final ArrayList<Navigatable> navigatable = new ArrayList<Navigatable>();
    final List<PsiElement> views = new ArrayList<PsiElement>();

    for (LightPhpElement element : info.getElements()) {
      PsiElement psiElement = element.getPsi(project);
      if (psiElement instanceof Method) {
        views.add(PsiTreeUtil.getParentOfType(psiElement, PhpClass.class));
      } else {
        views.add(psiElement);
      }
      if (psiElement instanceof Navigatable) {
        navigatable.add((Navigatable) psiElement);
      }
    }

    LOG.assertTrue(views.size() == navigatable.size());

    openTargets(e, info.getTitle(),
      new GotoFileCellRenderer(0),
      views.toArray(new PsiElement[views.size()]),
      navigatable.toArray(new Navigatable[navigatable.size()]));
  }

  public static void openTargets(@NotNull final MouseEvent e,
                                 @NotNull final String title,
                                 @NotNull final ListCellRenderer listRenderer,
                                 @NotNull final PsiElement[] views,
                                 @NotNull final Navigatable... targets) {
    if (targets.length == 0) {
      return;
    }

    if (targets.length == 1) {
      targets[0].navigate(true);
      return;
    }

    final JList list = new JList(views);
    list.setCellRenderer(listRenderer);
    new PopupChooserBuilder(list).setTitle(title).setMovable(true)
      .setItemChoosenCallback(new Runnable() {
        public void run() {
          int[] ids = list.getSelectedIndices();
          if (ids == null || ids.length == 0) return;
          List<Navigatable> selectedElements = new ArrayList<Navigatable>();
          for (int id : ids) {
            selectedElements.add(targets[id]);
          }
          for (Navigatable element : selectedElements) {
            LOG.assertTrue(((PsiElement) element).isValid());
            element.navigate(true);
          }
        }
      }).createPopup().show(new RelativePoint(e));
  }

}
