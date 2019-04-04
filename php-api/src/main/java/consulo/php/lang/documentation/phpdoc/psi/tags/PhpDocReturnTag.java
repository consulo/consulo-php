package consulo.php.lang.documentation.phpdoc.psi.tags;

import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;

/**
 * @author jay
 * @date Jun 29, 2008 2:32:45 AM
 */
public interface PhpDocReturnTag extends PhpDocPsiElement
{
	public String[] getTypes();

	public String getTypeString();
}
