package net.jay.plugins.php.lang.psi.elements;

import java.util.List;

import com.intellij.psi.PsiElement;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public interface PHPPsiElement extends PsiElement
{
	PHPPsiElement getFirstPsiChild();

	PHPPsiElement getNextPsiSibling();

	PHPPsiElement getPrevPsiSibling();

	List<LightCopyContainer> getChildrenForCache();
}
