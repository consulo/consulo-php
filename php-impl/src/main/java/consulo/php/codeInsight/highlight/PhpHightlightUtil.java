package consulo.php.codeInsight.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;

/**
 * @author VISTALL
 * @since 2019-05-25
 */
public class PhpHightlightUtil
{
	public static void checkIsStaticForSelf(HighlightInfoHolder holder, PhpPsiElement element)
	{
		if(element instanceof MethodReference)
		{
			PsiElement resolvedElement = ((MethodReference) element).resolve();
			if(resolvedElement == null)
			{
				return;
			}

			if(((MethodReference) element).isStatic() && resolvedElement instanceof PhpElementWithModifier && !((PhpElementWithModifier) resolvedElement).getModifier().isStatic())
			{
				String name = ((PhpNamedElement) resolvedElement).getName();
				holder.add(HighlightInfo.newHighlightInfo(HighlightInfoType.ERROR).range(((MethodReference) element).getNameIdentifier()).descriptionAndTooltip("'" + name + "' is not static")
						.createUnconditionally());
			}
		}

		else if(element instanceof ClassConstantReference)
		{

		}
	}
}
