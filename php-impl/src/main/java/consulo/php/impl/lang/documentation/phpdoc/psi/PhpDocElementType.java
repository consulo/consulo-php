package consulo.php.impl.lang.documentation.phpdoc.psi;

import com.jetbrains.php.lang.PhpFileType;
import javax.annotation.Nonnull;
import consulo.language.ast.IElementType;

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
