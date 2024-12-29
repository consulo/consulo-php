package consulo.php.impl.lang.psi.resolve;

import jakarta.annotation.Nullable;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.Function;
import consulo.language.psi.PsiFile;
import consulo.language.psi.resolve.ResolveState;

/**
 * @author jay
 * @date Apr 15, 2008 10:33:43 AM
 */
public class ResolveUtil
{
	@Nullable
	public static PsiElement treeWalkUp(PsiScopeProcessor processor, PsiElement elt, PsiElement lastParent, PsiElement place)
	{
		if(elt == null)
		{
			return null;
		}

		PsiElement cur = elt;
		do
		{
			if(!cur.processDeclarations(processor, ResolveState.initial(), cur == elt ? lastParent : null, place))
			{
				if(processor instanceof PhpResolveProcessor)
				{
					return ((PhpResolveProcessor) processor).getResult().iterator().next();
				}
			}
			// stop walkup at function level
			if(cur instanceof Function)
			{
				return null;
			}
			if(cur instanceof PsiFile)
			{
				break;
			}

			cur = cur.getPrevSibling();
		}
		while(cur != null);

		return treeWalkUp(processor, elt.getContext(), elt, place);
	}

	public static void treeWalkUp(PsiElement place, PsiScopeProcessor processor)
	{
		PsiElement lastParent = null;
		PsiElement run = place;
		while(run != null)
		{
			if(!run.processDeclarations(processor, ResolveState.initial(), lastParent, place))
			{
				return;
			}
			lastParent = run;
			run = run.getContext();
		}
	}
}
