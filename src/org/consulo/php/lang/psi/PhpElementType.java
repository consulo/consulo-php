package org.consulo.php.lang.psi;

import com.intellij.psi.tree.IElementType;
import org.consulo.php.lang.PhpLanguage;
import org.jetbrains.annotations.NonNls;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpElementType extends IElementType
{

	public PhpElementType(@NonNls String debugName)
	{
		super(debugName, PhpLanguage.INSTANCE);
	}
}
