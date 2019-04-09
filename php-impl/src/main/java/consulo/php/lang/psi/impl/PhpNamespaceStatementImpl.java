package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.CharFilter;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotations.RequiredReadAction;
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

		ClassReference packageReference = getPackageReference();
		if(packageReference == null)
		{
			return "/";
		}
		return StringUtil.strip(packageReference.getText(), CharFilter.NOT_WHITESPACE_FILTER);
	}

	@Override
	public ClassReference getPackageReference()
	{
		return findNotNullChildByClass(ClassReference.class);
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Nonnull
	@Override
	public CharSequence getNameCS()
	{
		return null;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getFQN()
	{
		return null;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean isInternal()
	{
		return false;
	}

}
