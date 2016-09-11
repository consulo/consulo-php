package consulo.php.lang.psi.impl.stub.elements;

import consulo.php.lang.PhpLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import consulo.psi.tree.IElementTypeAsPsiFactory;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubElement<StubT extends StubElement, PsiT extends PsiElement> extends IStubElementType<StubT,
		PsiT> implements IElementTypeAsPsiFactory
{
	public PhpStubElement(@NotNull @NonNls String debugName)
	{
		super(debugName, PhpLanguage.INSTANCE);
	}

	@Override
	@NotNull
	public abstract PsiT createElement(@NotNull ASTNode node);

	@NotNull
	@Override
	public String getExternalId()
	{
		return "php." + toString();
	}
}
