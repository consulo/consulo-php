package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.PhpIcons;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.parser.PhpElementTypes;
import org.consulo.php.lang.psi.PhpPsiElementFactory;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.PhpModifier;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
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

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpField(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public Icon getIcon()
	{
		return PhpIcons.FIELD;
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
			return nameIdentifier.getText().substring(1);
		return null;
	}

	@Override
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

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public PhpModifier getModifier()
	{
		if(modifier == null)
		{
			PhpModifier modifier = new PhpModifier();
			final PhpElement element = ((PhpElement) getParent()).getFirstPsiChild();
			if(element.getNode().getElementType() == PhpElementTypes.MODIFIER_LIST)
			{
				final ASTNode[] nodes = element.getNode().getChildren(PhpTokenTypes.tsMODIFIERS);
				for(ASTNode node : nodes)
				{
					if(node.getElementType() == PhpTokenTypes.kwPUBLIC)
					{
						modifier.setAccess(PhpModifier.Access.PUBLIC);
					}
					else if(node.getElementType() == PhpTokenTypes.kwPROTECTED)
					{
						modifier.setAccess(PhpModifier.Access.PROTECTED);
					}
					else if(node.getElementType() == PhpTokenTypes.kwPRIVATE)
					{
						modifier.setAccess(PhpModifier.Access.PRIVATE);
					}
					else if(node.getElementType() == PhpTokenTypes.kwSTATIC)
					{
						modifier.setState(PhpModifier.State.STATIC);
					}
				}
			}
			this.modifier = modifier;
		}
		return modifier;
	}

	@Override
	public PhpDocComment getDocComment()
	{
		final PsiElement parent = getParent();
		if(parent instanceof PhpElement)
		{
			final PhpElement element = ((PhpElement) parent).getPrevPsiSibling();
			if(element instanceof PhpDocComment)
			{
				return (PhpDocComment) element;
			}
		}
		return null;
	}
}
