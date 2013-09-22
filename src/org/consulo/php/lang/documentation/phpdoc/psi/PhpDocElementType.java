package org.consulo.php.lang.documentation.phpdoc.psi;

import org.consulo.php.lang.PhpFileType;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Jun 26, 2008 11:06:54 PM
 */
public class PhpDocElementType extends IElementType
{

	public PhpDocElementType(@NotNull String debugName)
	{
		super(debugName, PhpFileType.INSTANCE.getLanguage());
	}
}
