package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import net.jay.plugins.php.lang.psi.elements.GroupStatement;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date May 9, 2008 5:12:53 PM
 */
public class GroupStatementImpl extends PHPPsiElementImpl implements GroupStatement {
    public GroupStatementImpl(ASTNode node) {
        super(node);
    }

    public PsiElement[] getStatements() {
        return getChildren();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState state,
                                       PsiElement lastParent,
                                       @NotNull PsiElement source) {
        if (lastParent == null) {
            for (PsiElement statement : getStatements()) {
                if (!statement.processDeclarations(processor, state, null, source)) {
                    return false;
                }
            }
        } else if (lastParent instanceof PHPPsiElement) {
            PHPPsiElement statement = ((PHPPsiElement) lastParent).getPrevPsiSibling();
            while (statement != null) {
                if (!statement.processDeclarations(processor, state, null, source)) {
                    return false;
                }
                statement = statement.getPrevPsiSibling();
            }
        }
        return super.processDeclarations(processor, state, lastParent, source);
    }

}
