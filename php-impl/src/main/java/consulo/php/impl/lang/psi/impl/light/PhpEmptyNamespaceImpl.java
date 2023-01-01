package consulo.php.impl.lang.psi.impl.light;

import consulo.application.util.function.Processor;
import consulo.language.psi.PsiManager;
import consulo.language.psi.stub.IStubElementType;
import consulo.project.Project;
import consulo.language.psi.PsiElement;
import consulo.language.impl.psi.LightElement;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.ast.ASTNode;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-05-02
 */
public class PhpEmptyNamespaceImpl extends LightElement implements PhpNamespace
{
	private final String myName;

	public PhpEmptyNamespaceImpl(Project project, String name)
	{
		super(PsiManager.getInstance(project), PhpLanguage.INSTANCE);
		myName = name;
	}

	@Override
	public String toString()
	{
		return getClass() + ":" + myName;
	}

	@Nullable
	@Override
	public GroupStatement getStatements()
	{
		return null;
	}

	@Override
	public boolean isBraced()
	{
		return false;
	}

	@Override
	public IStubElementType getElementType()
	{
		return null;
	}

	@Override
	public PhpNamespaceStub getStub()
	{
		return null;
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Nullable
	@Override
	public PhpDocComment getDocComment()
	{
		return null;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return myName;
	}

	@Override
	public String getName()
	{
		return myName;
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

	@RequiredReadAction
	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return null;
	}

	@RequiredWriteAction
	@Override
	public PsiElement setName(@Nonnull @NonNls String name) throws IncorrectOperationException
	{
		return null;
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpType.EMPTY;
	}

	@Override
	public PhpPsiElement getFirstPsiChild()
	{
		return null;
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		return null;
	}

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		return null;
	}
}
