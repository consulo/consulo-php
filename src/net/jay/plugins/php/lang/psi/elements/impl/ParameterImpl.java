package net.jay.plugins.php.lang.psi.elements.impl;

import javax.swing.Icon;

import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.Parameter;
import net.jay.plugins.php.lang.psi.elements.Variable;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date Apr 3, 2008 10:32:38 PM
 */
public class ParameterImpl extends PhpNamedElementImpl implements Parameter
{
	public ParameterImpl(ASTNode node)
	{
		super(node);
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
			final Variable variable = PhpPsiElementFactory.getInstance(getProject()).createVariable(name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), variable.getNameNode());
		}
		return this;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpParameter(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Nullable
	public Icon getIcon()
	{
		return PHPIcons.PARAMETER;
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
