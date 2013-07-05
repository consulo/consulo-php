package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.cache.psi.LightElementUtil;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpField;
import net.jay.plugins.php.completion.PhpVariantsUtil;
import net.jay.plugins.php.completion.UsageContext;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date May 15, 2008 11:24:30 AM
 */
public class FieldReferenceImpl extends PhpTypedElementImpl implements FieldReference
{

	public FieldReferenceImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpFieldReference(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	private ASTNode getNameNode()
	{
		if(getClassReference() != null)
		{
			return this.getNode().findChildByType(PHPTokenTypes.VARIABLE);
		}
		if(getObjectReference() != null)
		{
			return this.getNode().findChildByType(PHPTokenTypes.IDENTIFIER);
		}
		return null;
	}

	public boolean canReadName()
	{
		return getNameNode() != null;
	}

	public String getFieldName()
	{
		if(canReadName())
		{
			String name = getNameNode().getText();
			if(getClassReference() != null)
			{
				name = name.substring(1);
			}
			return name;
		}
		return null;
	}

	public ClassReference getClassReference()
	{
		PHPPsiElement reference = getFirstPsiChild();
		if(reference instanceof ClassReference)
		{
			return (ClassReference) reference;
		}
		return null;
	}

	public PsiElement getObjectReference()
	{
		PsiElement object = getFirstPsiChild();
		if(object instanceof FieldReference || object instanceof Variable || object instanceof MethodReference)
		{
			return object;
		}
		return null;
	}

	public PhpModifier getReferenceType()
	{
		PhpModifier modifier = new PhpModifier();
		if(getClassReference() != null)
		{
			modifier.setState(PhpModifier.State.STATIC);
		}
		return modifier;
	}

	@Nullable
	public PsiReference getReference()
	{
		if(canReadName())
			return this;
		return null;
	}

	public PsiElement getElement()
	{
		return this;
	}

	public TextRange getRangeInElement()
	{
		if(canReadName())
		{
			ASTNode nameNode = getNameNode();
			int startOffset = nameNode.getPsi().getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameNode.getTextLength());
		}
		return null;
	}

	public Object[] getVariants()
	{
		final PsiElement objectReference = getObjectReference();
		if(objectReference instanceof PhpTypedElement)
		{
			final PhpType type = ((PhpTypedElement) objectReference).getType();
			final LightPhpClass lightPhpClass = type.getType();
			if(lightPhpClass != null)
			{
				final UsageContext context = new UsageContext();
				final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
				LightPhpClass lightContextClass = null;
				if(contextClass != null)
				{
					lightContextClass = LightElementUtil.findLightClassByPsi(contextClass);
				}
				if(lightContextClass != null)
				{
					context.setClassForAccessFilter(lightContextClass);
				}
				context.setModifier(getReferenceType());
				context.setCallingObjectClass(lightPhpClass);

				final List<LightPhpElement> toComplete = new ArrayList<LightPhpElement>();
				toComplete.addAll(lightPhpClass.getMethods());
				toComplete.addAll(lightPhpClass.getFields());

				final List<LookupElement> list = PhpVariantsUtil.getLookupItems(toComplete, context);
				return list.toArray(new LookupElement[list.size()]);
			}
		}
		return new Object[0];
	}

	@Nullable
	public PsiElement resolve()
	{
		final PsiElement objectReference = getObjectReference();
		if(objectReference instanceof PhpTypedElement)
		{
			final PhpType type = ((PhpTypedElement) objectReference).getType();
			final LightPhpClass lightPhpClass = type.getType();
			if(lightPhpClass != null)
			{
				for(LightPhpField field : lightPhpClass.getFields())
				{
					if(field.getName() != null && field.getName().equals(getFieldName()))
					{
						return field.getPsi(getProject());
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the name of the reference target element which does not depend on import statements
	 * and other context (for example, the full-qualified name of the class if the reference targets
	 * a Java class).
	 *
	 * @return the canonical text of the reference.
	 */
	public String getCanonicalText()
	{
		return null;
	}

	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getFieldName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), constantReference.getNameNode());
		}
		return this;
	}

	/**
	 * Changes the reference so that it starts to point to the specified element. This is called,
	 * for example, by the "Create Class from New" quickfix, to bind the (invalid) reference on
	 * which the quickfix was called to the newly created class.
	 *
	 * @param element the element which should become the target of the reference.
	 * @return the new underlying element of the reference.
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the rebind cannot be handled for some reason.
	 */
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	/**
	 * Checks if the reference targets the specified element.
	 *
	 * @param element the element to check target for.
	 * @return true if the reference targets that element, false otherwise.
	 */
	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof Field)
		{
			return element == resolve();
		}
		return false;
	}

	/**
	 * Returns false if the underlying element is guaranteed to be a reference, or true
	 * if the underlying element is a possible reference which should not be reported as
	 * an error if it fails to resolve. For example, a text in an XML file which looks
	 * like a full-qualified Java class name is a soft reference.
	 *
	 * @return true if the refence is soft, false otherwise.
	 */
	public boolean isSoft()
	{
		return false;
	}
}
