package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpField;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import net.jay.plugins.php.lang.psi.elements.PhpVariableReference;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date May 5, 2008 9:12:10 AM
 */
public class PhpFieldImpl extends PhpNamedElementImpl implements PhpField
{

	private PhpModifier modifier = null;

	public PhpFieldImpl(ASTNode node)
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

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PHPTokenTypes.VARIABLE);
	}

	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
			return nameIdentifier.getText().substring(1);
		return null;
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getName().equals(name))
		{
			final PhpVariableReference variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
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
