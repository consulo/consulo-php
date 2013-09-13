package org.consulo.php.psi.impl.stub.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.consulo.php.lang.PHPLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubElement<StubT extends StubElement, PsiT extends PsiElement> extends IStubElementType<StubT, PsiT>
{
	public PhpStubElement(@NotNull @NonNls String debugName)
	{
		super(debugName, PHPLanguage.INSTANCE);
	}

	public abstract PsiT createPsi(ASTNode node);

	@NotNull
	@Override
	public String getExternalId()
	{
		return "php." + toString();
	}
}
