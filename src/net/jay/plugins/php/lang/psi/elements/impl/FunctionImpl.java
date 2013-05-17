package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpFunction;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import net.jay.plugins.php.lang.psi.elements.Function;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.Parameter;
import net.jay.plugins.php.lang.psi.elements.ParameterList;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 3, 2008 10:16:23 PM
 */
public class FunctionImpl extends PhpNamedElementImpl implements Function {
  public FunctionImpl(ASTNode node) {
    super(node);
  }

  public Parameter[] getParameters() {
    final PsiElement[] psiElements = getParameterList().getParameters();
    final Parameter[] result = new Parameter[psiElements.length];
    for (int i = 0; i < psiElements.length; i++) {
      assert psiElements[i] instanceof Parameter;
      result[i] = (Parameter) psiElements[i];
    }
    return result;
  }

  public ParameterList getParameterList() {
    return PsiTreeUtil.getChildOfType(this, ParameterList.class);
  }

  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return null;
  }

  public void accept(@NotNull final PsiElementVisitor psiElementVisitor) {
    if (psiElementVisitor instanceof PHPElementVisitor) {
      ((PHPElementVisitor) psiElementVisitor).visitPhpFunction(this);
    } else {
      super.accept(psiElementVisitor);
    }
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement psiElement, @NotNull PsiElement psiElement1) {
    for (Parameter parameter : getParameters()) {
      if (!processor.execute(parameter, resolveState)) {
        return false;
      }
    }
    return super.processDeclarations(processor, resolveState, psiElement, psiElement1);
  }

  @NotNull
  public LightPhpElement getLightCopy(LightPhpElement parent) {
    LightPhpFunction function = new LightPhpFunction(parent, getName());
    for (LightCopyContainer container : getChildrenForCache()) {
      function.addChild(container.getLightCopy(function));
    }

    final PhpDocComment docComment = getDocComment();
    if (docComment != null) {
      final PhpDocReturnTag tag = docComment.getReturnTag();
      if (tag != null) {
        function.setTypeString(tag.getTypeString());
      }
    }

    parent.registerChild(function);
    return function;
  }

  @Override
  public Icon getIcon() {
    return PHPIcons.FIELD;
  }
}
