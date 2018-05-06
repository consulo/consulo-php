package consulo.php.lang.psi.resolve;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpParameter;
import consulo.php.lang.psi.PhpVariableReference;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 10:04:22 AM
 */
public class PhpScopeProcessor implements PsiScopeProcessor
{
	protected PhpElement element;

	public PhpScopeProcessor(PhpElement element)
	{
		this.element = element;
	}

	public boolean execute(PsiElement element)
	{
		return false;
	}

	@Override
	public boolean execute(PsiElement element, ResolveState state)
	{
		return execute(element);
	}

	@Nullable
	@Override
	public <T> T getHint(@Nonnull Key<T> tKey)
	{
		return null;
	}

	@Override
	public void handleEvent(Event event, Object o)
	{
	}

	protected boolean isAppropriateDeclarationType(PsiElement possibleDeclaration)
	{
		if(element instanceof PhpVariableReference)
		{
			return possibleDeclaration instanceof PhpVariableReference || possibleDeclaration instanceof PhpParameter;
		}
		return false;
	}
}
