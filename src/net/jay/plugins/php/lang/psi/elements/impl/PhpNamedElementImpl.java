package net.jay.plugins.php.lang.psi.elements.impl;

import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpNamedElement;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

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

	public ASTNode getNameNode()
	{
		return getNode().findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public String getName()
	{
		ASTNode nameNode = getNameNode();
		if(nameNode != null)
			return nameNode.getText();
		return null;
	}

	public int getTextOffset()
	{
		ASTNode nameNode = getNameNode();
		if(nameNode != null)
			return nameNode.getStartOffset();
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
