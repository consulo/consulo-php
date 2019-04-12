package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;

/**
 * @author jay
 * @date Jun 4, 2008 11:38:51 AM
 */
abstract public class PhpNamedElementImpl extends PhpElementImpl implements PhpNamedElement
{

	public PhpNamedElementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return this;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Override
	public String getName()
	{
		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
		{
			return nameNode.getText();
		}
		return null;
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
		{
			return nameNode.getNode().getStartOffset();
		}
		return super.getTextOffset();
	}

	@Override
	public PhpDocComment getDocComment()
	{
		final PhpPsiElement element = getPrevPsiSibling();
		if(element instanceof PhpDocComment)
		{
			return (PhpDocComment) element;
		}
		return null;
	}

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
