package consulo.php.lang.psi.impl;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpClassReference;
import consulo.php.lang.psi.PhpConstantReference;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpFieldReference;
import consulo.php.lang.psi.PhpMethodReference;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpVariableReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date May 15, 2008 11:24:30 AM
 */
public class PhpFieldReferenceImpl extends PhpTypeOwnerImpl implements PhpFieldReference
{

	public PhpFieldReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitFieldReference(this);
	}

	private PsiElement getNameIdentifier()
	{
		if(getClassReference() != null)
		{
			return findChildByType(PhpTokenTypes.VARIABLE);
		}
		if(getObjectReference() != null)
		{
			return findChildByType(PhpTokenTypes.IDENTIFIER);
		}
		return null;
	}

	@Override
	public boolean canReadName()
	{
		return getNameIdentifier() != null;
	}

	@Override
	public String getFieldName()
	{
		if(canReadName())
		{
			String name = getNameIdentifier().getText();
			if(getClassReference() != null)
			{
				name = name.substring(1);
			}
			return name;
		}
		return null;
	}

	@Override
	public PhpClassReference getClassReference()
	{
		PhpElement reference = getFirstPsiChild();
		if(reference instanceof PhpClassReference)
		{
			return (PhpClassReference) reference;
		}
		return null;
	}

	@Override
	public PsiElement getObjectReference()
	{
		PsiElement object = getFirstPsiChild();
		if(object instanceof PhpFieldReference || object instanceof PhpVariableReference || object instanceof PhpMethodReference)
		{
			return object;
		}
		return null;
	}

	@Override
	@Nullable
	public PsiReference getReference()
	{
		if(canReadName())
		{
			return this;
		}
		return null;
	}

	@Override
	public PsiElement getElement()
	{
		return this;
	}

	@Override
	public TextRange getRangeInElement()
	{
		if(canReadName())
		{
			PsiElement nameNode = getNameIdentifier();
			int startOffset = nameNode.getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameNode.getTextLength());
		}
		return null;
	}

	@Override
	public Object[] getVariants()
	{
		/*final PsiElement objectReference = getObjectReference();
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
				toComplete.addAll(lightPhpClass.getFunctions());
				toComplete.addAll(lightPhpClass.getFields());

				final List<LookupElement> list = PhpVariantsUtil.getLookupItems(toComplete, context);
				return list.toArray(new LookupElement[list.size()]);
			}
		}        */
		return new Object[0];
	}

	@Override
	@Nullable
	public PsiElement resolve()
	{
		/*final PsiElement objectReference = getObjectReference();
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
		}   */
		return null;
	}

	/**
	 * Returns the name of the reference target element which does not depend on import statements
	 * and other context (for example, the full-qualified name of the class if the reference targets
	 * a Java class).
	 *
	 * @return the canonical text of the reference.
	 */
	@Override
	public String getCanonicalText()
	{
		return null;
	}

	@Override
	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getFieldName().equals(name))
		{
			final PhpConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			nameIdentifier.replace(constantReference.getNameIdentifier());
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
	@Override
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
	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof PhpField)
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
	@Override
	public boolean isSoft()
	{
		return false;
	}
}
