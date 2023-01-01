package consulo.php.impl.lang.psi.resolve;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.util.dataholder.Key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author jay
 * @date Apr 15, 2008 10:04:22 AM
 */
public class PhpScopeProcessor implements PsiScopeProcessor
{
	protected PhpPsiElement element;

	public PhpScopeProcessor(PhpPsiElement element)
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
		if(element instanceof Variable)
		{
			return possibleDeclaration instanceof Variable || possibleDeclaration instanceof Parameter;
		}
		return false;
	}
}
