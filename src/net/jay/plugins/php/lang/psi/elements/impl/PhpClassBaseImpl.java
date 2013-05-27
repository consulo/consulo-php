package net.jay.plugins.php.lang.psi.elements.impl;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;

/**
 * @author jay
 * @date Jun 3, 2008 5:49:43 PM
 */
public abstract class PhpClassBaseImpl extends PhpNamedElementImpl
{
	public PhpClassBaseImpl(ASTNode node)
	{
		super(node);
	}

	@NotNull
	public abstract Icon getIcon();

	public abstract ItemPresentation getPresentation();
}
