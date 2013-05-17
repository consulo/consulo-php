package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import net.jay.plugins.php.lang.psi.elements.Foreach;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 15, 2008 3:36:30 PM
 */
public class ForeachImpl extends PHPPsiElementImpl implements Foreach {
    public ForeachImpl(ASTNode node) {
        super(node);
    }

    public PsiElement getArray() {
        return getFirstPsiChild();
    }

    public Variable getKey() {
        PsiElement[] children = getChildren();
        int variables = 0;
        Variable firstVariable = null;
        for (int i = 1; i < children.length; i++) {
            PsiElement child = children[i];
            if (child instanceof Variable) {
                if (++variables == 1) {
                    firstVariable = (Variable) child;
                }
            }
        }
        if (variables == 2) {
            return firstVariable;
        }
        return null;
    }

    public Variable getValue() {
        PsiElement lastChild = getLastChild();
        while (lastChild != null) {
            if (lastChild instanceof Variable) {
                return (Variable) lastChild;
            }
            lastChild = lastChild.getPrevSibling();
        }
        return null;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof PHPElementVisitor) {
            ((PHPElementVisitor) visitor).visitPhpForeach(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source) {
        if (getKey() != null
                && lastParent != getArray()
                && !getKey().processDeclarations(processor, state, null, source)) {
            return false;
        }
        if (getValue() != null
                && lastParent != getArray()
                && !getValue().processDeclarations(processor, state, null, source)) {
            return false;
        }
        return super.processDeclarations(processor, state, lastParent, source);
    }

}
