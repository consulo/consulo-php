package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import net.jay.plugins.php.lang.psi.elements.Global;
import net.jay.plugins.php.lang.psi.elements.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date May 5, 2008 8:06:55 AM
 */
public class GlobalImpl extends PHPPsiElementImpl implements Global {
    public GlobalImpl(ASTNode node) {
        super(node);
    }

    public Variable[] getVariables() {
        List<Variable> variables = new ArrayList<Variable>();
        for (PsiElement element : this.getChildren()) {
            if (element instanceof Variable) {
                variables.add((Variable) element);
            }
        }
        return variables.toArray(new Variable[variables.size()]);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source) {
        for (Variable variable : this.getVariables()) {
            if (!processor.execute(variable, state)) {
                return false;
            }
        }
        return super.processDeclarations(processor, state, lastParent, source);
    }

}
