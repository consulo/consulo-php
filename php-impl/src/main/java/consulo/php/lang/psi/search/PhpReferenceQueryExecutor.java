package consulo.php.lang.psi.search;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.Variable;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-23
 */
public class PhpReferenceQueryExecutor extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>
{
	public PhpReferenceQueryExecutor()
	{
		super(true);
	}

	@Override
	public void processQuery(@Nonnull ReferencesSearch.SearchParameters queryParameters, @Nonnull Processor<PsiReference> consumer)
	{
		PsiElement elementToSearch = queryParameters.getElementToSearch();
		if(elementToSearch instanceof PhpClass)
		{
			queryParameters.getOptimizer().searchWord(Variable.$THIS, queryParameters.getEffectiveSearchScope(), true, elementToSearch);
		}
		else if(elementToSearch instanceof Field)
		{
			CharSequence nameCS = ((Field) elementToSearch).getNameCS();
			queryParameters.getOptimizer().searchWord(nameCS.toString(), queryParameters.getEffectiveSearchScope(), true, elementToSearch);
		}
	}
}
