package consulo.php.lang.psi.impl.light;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;

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
