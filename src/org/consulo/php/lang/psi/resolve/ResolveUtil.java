package org.consulo.php.lang.psi.resolve;

import org.consulo.php.lang.psi.PhpFunction;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

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
			if(cur instanceof PhpFunction)
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

	public static ResolveResult[] multiResolve(PsiReference element)
	{
		return new ResolveResult[0];
	}

}
