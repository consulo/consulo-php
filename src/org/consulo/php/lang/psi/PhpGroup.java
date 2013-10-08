package org.consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface PhpGroup extends PhpElement
{
	PhpGroup[] EMPTY_ARRAY = new PhpGroup[0];

	ArrayFactory<PhpGroup> ARRAY_FACTORY = new ArrayFactory<PhpGroup>()
	{
		@NotNull
		@Override
		public PhpGroup[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new PhpGroup[i];
		}
	};

	@NotNull
	PsiElement[] getStatements();
}
