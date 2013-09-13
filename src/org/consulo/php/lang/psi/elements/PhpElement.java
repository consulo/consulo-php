package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PhpElement extends PsiElement
{
	PhpElement getFirstPsiChild();

	PhpElement getNextPsiSibling();

	PhpElement getPrevPsiSibling();
}
