package consulo.php.impl.lang.inspections;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.php.localize.PhpLocalize;
import jakarta.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 16, 2008 11:07:06 PM
 */
@ExtensionImpl
public class PhpDynamicAsStaticMethodCall extends PhpInspection {
    @Override
    @Nonnull
    public LocalizeValue getDisplayName() {
        return PhpLocalize.phpInspectionsDynamic_as_static_method_call();
    }

    @Override
    @Nonnull
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PhpElementVisitor() {
            @Override
            @SuppressWarnings({"ConstantConditions"})
            public void visitMethodReference(MethodReference reference) {
                if (reference.canReadName() && reference.isStatic()) {
                    final PsiElement element = reference.getReference().resolve();
                    if (element instanceof Function) {
                        final Function phpMethod = (Function) element;
                        if (!phpMethod.getModifier().isStatic()) {
                            holder.registerProblem(reference, PhpLocalize.phpInspectionsDynamic_as_static_method_call().get());
                        }
                    }
                }
            }
        };
    }
}
