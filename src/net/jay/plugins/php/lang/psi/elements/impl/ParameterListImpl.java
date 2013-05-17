package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import net.jay.plugins.php.lang.psi.elements.ParameterList;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 15, 2008 2:01:07 PM
 */
public class ParameterListImpl extends PHPPsiElementImpl implements ParameterList {

    public ParameterListImpl(ASTNode node) {
        super(node);
    }

    public PsiElement[] getParameters() {
        return getChildren();
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof PHPElementVisitor) {
            ((PHPElementVisitor) visitor).visitPhpParameterList(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState state,
                                       PsiElement lastParent,
                                       @NotNull PsiElement source) {
        if (lastParent == null) {
            for (PsiElement parameter : getParameters()) {
                if (!parameter.processDeclarations(processor, state, null, source)) {
                    return false;
                }
            }
        }
        return super.processDeclarations(processor, state, lastParent, source);
    }

}
