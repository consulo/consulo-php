package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
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

	@Nonnull
	private List<PhpNamedElement> getElements()
	{
		List<PhpNamedElement> result = new ArrayList<>();
		PhpNamespaceStub greenStub = getGreenStub();
		if(greenStub != null)
		{
			List<StubElement> childrenStubs = greenStub.getChildrenStubs();
			for(StubElement childrenStub : childrenStubs)
			{
				if(childrenStub instanceof PhpClassStub)
				{
					result.add(((PhpClassStub) childrenStub).getPsi());
				}
			}
		}
		else
		{
			GroupStatement statements = getStatements();
			if(statements != null)
			{
				PsiElement[] elements = statements.getStatements();

				for(PsiElement element : elements)
				{
					if(element instanceof PhpClass)
					{
						result.add((PhpNamedElement) element);
					}
				}
			}
		}

		return result;
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place)
	{
		Collection<PhpNamespace> namespaces = PhpIndex.getInstance(getProject()).getNamespacesByName(getName());
		for(PhpNamespace namespace : namespaces)
		{
			PhpNamespaceStatementImpl impl = (PhpNamespaceStatementImpl) namespace;

			for(PhpNamedElement element : impl.getElements())
			{
				if(!processor.execute(element, state))
				{
					return false;
				}
			}
		}
		return true;
	}
}
