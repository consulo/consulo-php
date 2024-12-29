package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementTypeAsPsiFactory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubElementType<StubT extends StubElement, PsiT extends PsiElement> extends IStubElementType<StubT, PsiT> implements IElementTypeAsPsiFactory
{
	public PhpStubElementType(@Nonnull @NonNls String debugName)
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
