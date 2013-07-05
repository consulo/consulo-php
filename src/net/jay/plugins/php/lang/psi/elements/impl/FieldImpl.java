package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpField;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date May 5, 2008 9:12:10 AM
 */
public class FieldImpl extends PhpNamedElementImpl implements Field
{

	private PhpModifier modifier = null;

	public FieldImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpField(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public Icon getIcon()
	{
		return PHPIcons.FIELD;
	}

	public ASTNode getNameNode()
	{
		return getNode().findChildByType(PHPTokenTypes.VARIABLE);
	}

	public String getName()
	{
		ASTNode nameNode = getNameNode();
		if(nameNode != null)
			return nameNode.getText().substring(1);
		return null;
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getName().equals(name))
		{
			final Variable variable = PhpPsiElementFactory.createVariable(getProject(), name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), variable.getNameNode());
		}
		return this;
	}

	@NotNull
	public LightPhpElement getLightCopy(LightPhpElement parent)
	{
		LightPhpField field = new LightPhpField(parent, getName());
		for(LightCopyContainer container : getChildrenForCache())
		{
			field.addChild(container.getLightCopy(field));
		}
		field.setModifier(getModifier());

		final PhpDocComment docComment = getDocComment();
		if(docComment != null)
		{
			final PhpDocVarTag varTag = docComment.getVarTag();
			if(varTag != null)
			{
				field.setType(varTag.getType());
			}
		}

		parent.registerChild(field);
		return field;
	}

	@SuppressWarnings({"ConstantConditions"})
	public PhpModifier getModifier()
	{
		if(modifier == null)
		{
			PhpModifier modifier = new PhpModifier();
			final PHPPsiElement element = ((PHPPsiElement) getParent()).getFirstPsiChild();
			if(element.getNode().getElementType() == PHPElementTypes.MODIFIER_LIST)
			{
				final ASTNode[] nodes = element.getNode().getChildren(PHPTokenTypes.tsMODIFIERS);
				for(ASTNode node : nodes)
				{
					if(node.getElementType() == PHPTokenTypes.kwPUBLIC)
					{
						modifier.setAccess(PhpModifier.Access.PUBLIC);
					}
					else if(node.getElementType() == PHPTokenTypes.kwPROTECTED)
					{
						modifier.setAccess(PhpModifier.Access.PROTECTED);
					}
					else if(node.getElementType() == PHPTokenTypes.kwPRIVATE)
					{
						modifier.setAccess(PhpModifier.Access.PRIVATE);
					}
					else if(node.getElementType() == PHPTokenTypes.kwSTATIC)
					{
						modifier.setState(PhpModifier.State.STATIC);
					}
				}
			}
			this.modifier = modifier;
		}
		return modifier;
	}

	public PhpDocComment getDocComment()
	{
		final PsiElement parent = getParent();
		if(parent instanceof PHPPsiElement)
		{
			final PHPPsiElement element = ((PHPPsiElement) parent).getPrevPsiSibling();
			if(element instanceof PhpDocComment)
			{
				return (PhpDocComment) element;
			}
		}
		return null;
	}
}
