package com.jetbrains.php.lang.psi.elements;

import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PhpPsiElement extends PsiElement
{
	public static final PhpPsiElement[] EMPTY_ARRAY = new PhpPsiElement[0];

	public static ArrayFactory<PhpPsiElement> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new PhpPsiElement[count];

	PhpPsiElement getFirstPsiChild();

	PhpPsiElement getNextPsiSibling();

	PhpPsiElement getPrevPsiSibling();
}
