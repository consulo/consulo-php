package net.jay.plugins.php.lang.psi.elements.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.PhpInterface;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import net.jay.plugins.php.util.PhpPresentationUtil;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date Apr 8, 2008 1:59:36 PM
 */
public class PhpInterfaceImpl extends PhpClassBaseImpl implements PhpInterface
{
	public PhpInterfaceImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpInterface(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	@NotNull
	public Icon getIcon()
	{
		return PHPIcons.INTERFACE;
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

	public ItemPresentation getPresentation()
	{
		return PhpPresentationUtil.getInterfacePresentation(this);
	}

	@NotNull
	public LightPhpElement getLightCopy(LightPhpElement parent)
	{
		LightPhpInterface klass = new LightPhpInterface(parent, getName());
		for(LightCopyContainer container : getChildrenForCache())
		{
			klass.addChild(container.getLightCopy(klass));
		}

		parent.registerChild(klass);
		return klass;
	}
}
