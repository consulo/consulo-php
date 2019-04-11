package consulo.php.lang.documentation.phpdoc.psi;

import com.jetbrains.php.lang.PhpFileType;
import javax.annotation.Nonnull;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Jun 26, 2008 11:06:54 PM
 */
public class PhpDocElementType extends IElementType
{

	public PhpDocElementType(@Nonnull String debugName)
	{
		super(debugName, PhpFileType.INSTANCE.getLanguage());
	}
}
