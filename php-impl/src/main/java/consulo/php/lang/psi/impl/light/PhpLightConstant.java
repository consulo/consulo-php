package consulo.php.lang.psi.impl.light;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;

/**
 * @author VISTALL
 * @since 2019-04-15
 */
public class PhpLightConstant extends LightElement implements Constant
{
	private final String myName;
	private final PhpType myPhpType;

	public PhpLightConstant(Project project, String name, PhpType phpType)
	{
		super(PsiManager.getInstance(project), PhpLanguage.INSTANCE);
		myName = name;
		myPhpType = phpType;
	}

	@Nullable
	@Override
	public PsiElement getValue()
	{
		return null;
	}

	@Override
	public boolean canNavigateToSource()
	{
		return true;
	}

	@Nullable
	@Override
	public String getValuePresentation()
	{
		return null;
	}

	@Override
	public boolean isCaseSensitive()
	{
		return false;
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
	public String getName()
	{
		return myName;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
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

	@Override
	public boolean isEquivalentTo(PsiElement another)
	{
		if(another instanceof PhpLightConstant)
		{
			if(getNameCS().equals(((PhpLightConstant) another).getNameCS()))
			{
				return true;
			}
		}
		return super.isEquivalentTo(another);
	}

	@Override
	public String toString()
	{
		return getClass() + ":" + getName();
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return myPhpType;
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

	@Override
	public boolean isWriteAccess()
	{
		return false;
	}
}
