package consulo.php.codeInsight.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import consulo.annotation.access.RequiredReadAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-05-25
 */
public class PhpHightlightUtil
{
	@RequiredReadAction
	public static void checkIsStaticForSelf(HighlightInfoHolder holder, PhpPsiElement element)
	{
		if(element instanceof MethodReference)
		{
			validateSelfQualifierReference(holder, ((MethodReference) element).resolve(), ((MethodReference) element).getClassReference(), ((MethodReference) element).getNameIdentifier());
		}
		else if(element instanceof FieldReference)
		{
			validateSelfQualifierReference(holder, ((FieldReference) element).resolve(), ((FieldReference) element).getClassReference(), ((FieldReference) element).getNameIdentifier());
		}
		else if(element instanceof ClassConstantReference)
		{
			validateSelfQualifierReference(holder, ((ClassConstantReference) element).resolve(), ((ClassConstantReference) element).getClassReference(), ((ClassConstantReference) element).getNameIdentifier());
		}
	}

	private static void validateSelfQualifierReference(@Nonnull HighlightInfoHolder holder,
													   @Nullable PsiElement resolvedElement,
													   @Nullable ClassReference classReference,
													   @Nonnull PsiElement referenceElement)
	{
		if(resolvedElement == null)
		{
			return;
		}

		if(isSelfQualifier(classReference) && resolvedElement instanceof PhpElementWithModifier && !((PhpElementWithModifier) resolvedElement).getModifier().isStatic())
		{
			String name = ((PhpNamedElement) resolvedElement).getName();
			holder.add(HighlightInfo.newHighlightInfo(HighlightInfoType.ERROR).range(referenceElement).descriptionAndTooltip("'" + name + "' is not static").createUnconditionally());
		}
	}

	private static boolean isSelfQualifier(@Nullable ClassReference classReference)
	{
		return classReference != null && PhpClass.SELF.equals(classReference.getReferenceName()) && classReference.getQualifier() == null;
	}
}
