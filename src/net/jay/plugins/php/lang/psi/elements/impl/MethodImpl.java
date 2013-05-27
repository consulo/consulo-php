package net.jay.plugins.php.lang.psi.elements.impl;

import javax.swing.Icon;

import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.ConstantReference;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpInterface;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date Apr 3, 2008 11:09:17 PM
 */
public class MethodImpl extends FunctionImpl implements Method
{
	private PhpModifier modifier = null;

	public MethodImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.getInstance(getProject()).createConstantReference(name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), constantReference.getNameNode());
		}
		return this;
	}

	@Nullable
	public Icon getIcon()
	{
		if(isStatic())
		{
			return PHPIcons.STATIC_METHOD;
		}
		return PHPIcons.METHOD;
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpMethod(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	@NotNull
	public LightPhpElement getLightCopy(LightPhpElement parent)
	{
		LightPhpMethod method = new LightPhpMethod(parent, getName());
		for(LightCopyContainer container : getChildrenForCache())
		{
			method.addChild(container.getLightCopy(method));
		}
		method.setModifier(getModifier());

		final PhpDocComment docComment = getDocComment();
		if(docComment != null)
		{
			final PhpDocReturnTag tag = docComment.getReturnTag();
			if(tag != null)
			{
				method.setTypeString(tag.getTypeString());
			}
		}

		parent.registerChild(method);
		return method;
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
		final PHPPsiElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PHPTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PHPTokenTypes.kwFINAL)
			{
				return true;
			}
		}
		return false;
	}

	public boolean isAbstract()
	{
		final PHPPsiElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PHPTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PHPTokenTypes.kwABSTACT)
			{
				return true;
			}
		}
		final PhpInterface phpInterface = PsiTreeUtil.getParentOfType(this, PhpInterface.class);
		//noinspection RedundantIfStatement
		if(phpInterface != null)
		{
			return true;
		}
		return false;
	}

	public boolean isStatic()
	{
		final PHPPsiElement modifierList = getFirstPsiChild();
		//noinspection ConstantConditions
		final ASTNode[] nodes = modifierList.getNode().getChildren(PHPTokenTypes.tsMODIFIERS);
		for(ASTNode node : nodes)
		{
			if(node.getElementType() == PHPTokenTypes.kwSTATIC)
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

}
