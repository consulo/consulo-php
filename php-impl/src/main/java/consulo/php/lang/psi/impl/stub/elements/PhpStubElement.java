package consulo.php.lang.psi.impl.stub.elements;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import consulo.php.lang.PhpLanguage;
import consulo.psi.tree.IElementTypeAsPsiFactory;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubElement<StubT extends StubElement, PsiT extends PsiElement> extends IStubElementType<StubT, PsiT> implements IElementTypeAsPsiFactory
{
	public PhpStubElement(@Nonnull @NonNls String debugName)
	{
		super(debugName, PhpLanguage.INSTANCE);
	}

	@Override
	@Nonnull
	public abstract PsiT createElement(@Nonnull ASTNode node);

	@Nonnull
	@Override
	public String getExternalId()
	{
		return "php." + toString();
	}
}
