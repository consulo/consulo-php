package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import net.jay.plugins.php.util.PhpPresentationUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Apr 8, 2008 1:54:50 PM
 */
public class PhpClassImpl extends PhpClassBaseImpl implements PhpClass
{
	public PhpClassImpl(ASTNode node)
	{
		super(node);
	}

	public Field[] getFields()
	{
		PsiElement[] fields = PsiTreeUtil.collectElements(this, new PsiElementFilter()
		{
			public boolean isAccepted(PsiElement element)
			{
				return element instanceof Field;
			}
		});
		Field[] result = new Field[fields.length];
		for(int i = 0; i < fields.length; i++)
		{
			result[i] = (Field) fields[i];
		}
		return result;
	}

	public Method[] getMethods()
	{
		List<Method> methods = new ArrayList<Method>();
		for(PsiElement element : this.getChildren())
		{
			if(element instanceof Method)
			{
				methods.add((Method) element);
			}
		}
		return methods.toArray(new Method[methods.size()]);
	}

	public PhpClass getSuperClass()
	{
		final ExtendsList list = PsiTreeUtil.getChildOfType(this, ExtendsList.class);
		assert list != null;
		return list.getExtendsClass();
	}

	public PhpInterface[] getImplementedInterfaces()
	{
		final ImplementsList list = PsiTreeUtil.getChildOfType(this, ImplementsList.class);
		assert list != null;
		final List<PhpInterface> interfaceList = list.getInterfaces();
		return interfaceList.toArray(new PhpInterface[interfaceList.size()]);
	}

	@SuppressWarnings({"ConstantConditions"})
	public Method getConstructor()
	{
		Method newOne = null;
		Method oldOne = null;
		for(Method method : this.getMethods())
		{
			if(method.getName().equals(CONSTRUCTOR))
			{
				newOne = method;
			}
			if(method.getName().equals(this.getName()))
			{
				oldOne = method;
			}
		}
		if(newOne != null)
		{
			return newOne;
		}
		return oldOne;
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), constantReference.getNameNode());
		}
		return this;
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpClass(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	@NotNull
	public Icon getIcon()
	{
		if(isAbstract())
		{
			return PHPIcons.ABSTRACT_CLASS;
		}
		if(isFinal())
		{
			return PHPIcons.FINAL_CLASS;
		}
		return PHPIcons.CLASS;
	}

	public boolean isAbstract()
	{
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwABSTACT;
	}

	public boolean isFinal()
	{
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwFINAL;
	}

	public boolean isInterface()
	{
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwINTERFACE;
	}

	public PhpModifier getModifier()
	{
		PhpModifier modifier = new PhpModifier();
		if(isAbstract())
		{
			modifier.setAbstractness(PhpModifier.Abstractness.ABSTRACT);
		}
		if(isFinal())
		{
			modifier.setAbstractness(PhpModifier.Abstractness.FINAL);
		}
		return modifier;
	}

	public ItemPresentation getPresentation()
	{
		return PhpPresentationUtil.getClassPresentation(this);
	}

	@NotNull
	public LightPhpElement getLightCopy(LightPhpElement parent)
	{
		LightPhpClass klass = new LightPhpClass(parent, getName());
		for(LightCopyContainer container : getChildrenForCache())
		{
			klass.addChild(container.getLightCopy(klass));
		}
		final ExtendsList list = PsiTreeUtil.getChildOfType(this, ExtendsList.class);
		assert list != null;
		if(list.getChildren().length == 1)
		{
			klass.setSuperClassName(list.getChildren()[0].getText());
		}
		final ImplementsList implementsList = PsiTreeUtil.getChildOfType(this, ImplementsList.class);
		assert implementsList != null;
		for(PsiElement el : implementsList.getChildren())
		{
			klass.addSuperInterface(el.getText());
		}
		klass.setModifier(getModifier());

		parent.registerChild(klass);
		return klass;
	}
}
