package org.consulo.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.PhpIcons;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpPsiElementFactory;
import org.consulo.php.lang.psi.elements.*;
import org.consulo.php.lang.psi.resolve.types.PhpType;
import org.consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 3, 2008 11:09:17 PM
 */
public class PhpMethodImpl extends FunctionImpl implements PhpMethod
{
	private PhpModifier modifier = null;

	public PhpMethodImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
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
	public Icon getIcon()
	{
		if(isStatic())
		{
			return PhpIcons.STATIC_METHOD;
		}
		return PhpIcons.METHOD;
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) psiElementVisitor).visitPhpMethod(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	public PhpModifier getModifier()
	{
		if(modifier == null)
		{
			modifier = new PhpModifier();
			if(isStatic())
			{
				modifier.setState(PhpModifier.State.STATIC);
			}
			if(isAbstract())
			{
				modifier.setAbstractness(PhpModifier.Abstractness.ABSTRACT);
			}
			else if(isFinal())
			{
				modifier.setAbstractness(PhpModifier.Abstractness.FINAL);
			}
		}
		return modifier;
	}

	public boolean isFinal()
	{
		final PhpElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PhpTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PhpTokenTypes.kwFINAL)
			{
				return true;
			}
		}
		return false;
	}

	public boolean isAbstract()
	{
		final PhpElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PhpTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PhpTokenTypes.kwABSTACT)
			{
				return true;
			}
		}
		final PhpClass phpInterface = PsiTreeUtil.getParentOfType(this, PhpClass.class);
		//noinspection RedundantIfStatement
		if(phpInterface != null && phpInterface.isInterface())
		{
			return true;
		}
		return false;
	}

	public boolean isStatic()
	{
		final PhpElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PhpTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PhpTokenTypes.kwSTATIC)
			{
				return true;
			}
		}
		return false;
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

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement psiElement, @NotNull PsiElement psiElement1) {
		for (PhpParameter phpParameter : getParameters()) {
			if(!processor.execute(phpParameter, resolveState)) {
				return false;
			}
		}
		return true;
	}
}
