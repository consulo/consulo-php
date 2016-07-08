package org.consulo.php.lang.psi;

import consulo.lombok.annotations.ArrayFactoryFields;
import com.intellij.psi.PsiElement;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
@ArrayFactoryFields
public interface PhpElement extends PsiElement
{
	PhpElement getFirstPsiChild();

	PhpElement getNextPsiSibling();

	PhpElement getPrevPsiSibling();
}
