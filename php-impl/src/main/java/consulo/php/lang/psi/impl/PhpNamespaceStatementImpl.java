package consulo.php.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.php.lang.psi.PhpPackage;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import javax.annotation.Nonnull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpNamespaceStatementImpl extends PhpElementImpl implements PhpNamespace
{
	public PhpNamespaceStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitNamespaceStatement(this);
	}

	@Override
	public String getNamespace()
	{
		ClassReference packageReference = getPackageReference();
		if(packageReference == null)
		{
			return null;
		}
		PsiElement resolve = packageReference.resolve();
		if(resolve instanceof PhpPackage)
		{
			return ((PhpPackage) resolve).getNamespaceName();
		}
		return null;
	}

	@Override
	public ClassReference getPackageReference()
	{
		return findNotNullChildByClass(ClassReference.class);
	}
}
