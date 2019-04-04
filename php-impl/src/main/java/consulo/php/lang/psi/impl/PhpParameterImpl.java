package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 3, 2008 10:32:38 PM
 */
public class PhpParameterImpl extends PhpNamedElementImpl implements Parameter
{
	public PhpParameterImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Override
	public String getName()
	{
		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
		{
			return nameNode.getText().substring(1);
		}
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

	@Override
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final Variable variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitParameter(this);
	}

	@Override
	@Nonnull
	public PhpType getType()
	{
		PhpType type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		if(type == null)
		{
			PhpTypeAnnotatorVisitor.process(this);
		}
		type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		assert type != null;
		return type;
	}
}
