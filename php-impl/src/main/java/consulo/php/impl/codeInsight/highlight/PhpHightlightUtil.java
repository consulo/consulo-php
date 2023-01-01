package consulo.php.impl.codeInsight.highlight;

import com.jetbrains.php.lang.psi.elements.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoHolder;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.psi.PsiElement;

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
