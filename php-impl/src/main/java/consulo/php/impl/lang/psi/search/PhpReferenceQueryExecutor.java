package consulo.php.impl.lang.psi.search;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.search.ReferencesSearch;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.language.psi.search.ReferencesSearchQueryExecutor;
import consulo.project.util.query.QueryExecutorBase;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-23
 */
@ExtensionImpl
public class PhpReferenceQueryExecutor extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> implements ReferencesSearchQueryExecutor {
    public PhpReferenceQueryExecutor() {
        super(true);
    }

    @Override
    public void processQuery(
        @Nonnull ReferencesSearch.SearchParameters queryParameters,
        @Nonnull Processor<? super PsiReference> consumer
    ) {
        PsiElement elementToSearch = queryParameters.getElementToSearch();
        if (elementToSearch instanceof PhpClass) {
            queryParameters.getOptimizer().searchWord(Variable.$THIS, queryParameters.getEffectiveSearchScope(), true, elementToSearch);
        }
        else if (elementToSearch instanceof Field) {
            CharSequence nameCS = ((Field)elementToSearch).getNameCS();
            queryParameters.getOptimizer().searchWord(nameCS.toString(), queryParameters.getEffectiveSearchScope(), true, elementToSearch);
        }
    }
}
