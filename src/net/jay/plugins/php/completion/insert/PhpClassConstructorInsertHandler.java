package net.jay.plugins.php.completion.insert;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import net.jay.plugins.php.completion.PhpLookupItem;
import net.jay.plugins.php.lang.psi.elements.Function;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpClass;

/**
 * @author jay
 * @date Jun 24, 2008 7:34:13 PM
 */
public class PhpClassConstructorInsertHandler extends PhpMethodInsertHandler {

  private static PhpClassConstructorInsertHandler instance = null;

  public static PhpClassConstructorInsertHandler getInstance() {
    if (instance == null) {
      instance = new PhpClassConstructorInsertHandler();
    }
    return instance;
  }

  protected Function getMethod(Editor editor, LookupElement element) {
    PhpLookupItem item = (PhpLookupItem) element.getObject();
    final PHPPsiElement psiElement = item.getLightElement().getPsi(editor.getProject());
    if (psiElement instanceof PhpClass) {
      return (Function) ((PhpClass) psiElement).getConstructor();
    }
    return null;
  }
}
