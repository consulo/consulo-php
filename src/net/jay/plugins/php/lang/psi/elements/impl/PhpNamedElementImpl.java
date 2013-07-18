package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.ConstantReference;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpNamedElement;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 4, 2008 11:38:51 AM
 */
abstract public class PhpNamedElementImpl extends PHPPsiElementImpl implements PhpNamedElement
{

	public PhpNamedElementImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier!= null && !getName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return this;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public String getName()
	{
		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
			return nameNode.getText();
		return null;
	}

	public int getTextOffset()
	{
		PsiElement nameNode = getNameIdentifier();
		if(nameNode != null)
			return nameNode.getNode().getStartOffset();
		return super.getTextOffset();
	}

	public PhpDocComment getDocComment()
	{
		final PHPPsiElement element = getPrevPsiSibling();
		if(element instanceof PhpDocComment)
		{
			return (PhpDocComment) element;
		}
		return null;
	}

	@NotNull
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
