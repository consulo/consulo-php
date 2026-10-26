package consulo.php.lang.documentation.phpdoc.psi.tags;

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;

/**
 * @author jay
 * @date Jun 29, 2008 2:06:19 AM
 */
public interface PhpDocVarTag extends PhpDocPsiElement, PhpDocParamTag
{
	public String getTypeString();
}
