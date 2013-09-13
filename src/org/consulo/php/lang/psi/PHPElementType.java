package org.consulo.php.lang.psi;

import net.jay.plugins.php.lang.PHPFileType;

import org.jetbrains.annotations.NonNls;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPElementType extends IElementType
{

	public PHPElementType(@NonNls String debugName)
	{
		super(debugName, PHPFileType.INSTANCE.getLanguage());
	}
}
