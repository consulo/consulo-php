package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpNamespaceStatementImpl extends PhpStubbedNamedElementImpl<PhpNamespaceStub> implements PhpNamespace
{
	public PhpNamespaceStatementImpl(ASTNode node)
	{
		super(node);
	}

	public PhpNamespaceStatementImpl(@Nonnull PhpNamespaceStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitNamespaceStatement(this);
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public String getName()
	{
		PhpNamespaceStub stub = getStub();
		if(stub != null)
		{
			return stub.getName();
		}

		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? "" : nameIdentifier.getText();
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return getName();
	}

	@Nullable
	@Override
	public GroupStatement getStatements()
	{
		return findChildByClass(GroupStatement.class);
	}

	@Nonnull
	@Override
	public String getParentNamespaceName()
	{
		return "";
	}

	@Override
	public boolean isBraced()
	{
		return findChildByType(PhpTokenTypes.LBRACE) != null;
	}
}
