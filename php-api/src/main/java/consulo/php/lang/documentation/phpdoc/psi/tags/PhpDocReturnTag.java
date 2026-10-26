package consulo.php.lang.documentation.phpdoc.psi.tags;

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;

/**
 * @author jay
 * @date Jun 29, 2008 2:32:45 AM
 */
public interface PhpDocReturnTag extends PhpDocPsiElement, PhpDocTag
{
	public String[] getTypes();

	public String getTypeString();
}
