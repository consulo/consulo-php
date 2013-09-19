package org.consulo.php.lang.psi;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

/**
 * @author jay
 * @date May 15, 2008 11:23:18 AM
 */
public interface PhpFieldReference extends PhpElement, PsiReference
{

	public boolean canReadName();

	@Nullable
	public String getFieldName();

	@Nullable
	public PhpClassReference getClassReference();

	@Nullable
	public PsiElement getObjectReference();
}
