package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import net.jay.plugins.php.lang.psi.elements.Catch;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 15, 2008 4:08:31 PM
 */
public class CatchImpl extends PHPPsiElementImpl implements Catch {

    public CatchImpl(ASTNode node) {
        super(node);
    }

    public ClassReference getExceptionType() {
        return PsiTreeUtil.getChildOfType(this, ClassReference.class);
    }

    public Variable getException() {
        return PsiTreeUtil.getChildOfType(this, Variable.class);
    }

    public PsiElement getStatement() {
        final Variable exception = getException();
        if (exception != null) {
            return exception.getNextPsiSibling();
        }
        return null;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof PHPElementVisitor) {
            ((PHPElementVisitor) visitor).visitPhpCatch(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, 
                                       @NotNull ResolveState state,
                                       PsiElement lastParent,
                                       @NotNull PsiElement source) {
        if (lastParent == null || lastParent == getStatement()) {
            if (getException() != null
                    && !getException().processDeclarations(processor, state, null, source)) {
                return false;
            }
        }
        return super.processDeclarations(processor, state, lastParent, source);
    }

}
