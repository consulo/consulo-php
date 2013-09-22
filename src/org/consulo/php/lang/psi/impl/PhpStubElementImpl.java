package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T>
{
	public PhpStubElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpStubElementImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public final void accept(@NotNull final PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			accept((PhpElementVisitor) visitor);
		}
		else
		{
			super.accept(visitor);
		}
	}

	public abstract void accept(@NotNull final PhpElementVisitor visitor);

}
