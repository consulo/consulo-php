package consulo.php.impl.lang.psi.impl;

import consulo.application.util.function.CommonProcessors;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.util.IncorrectOperationException;
import consulo.application.util.function.Processor;
import com.jetbrains.php.PhpClassHierarchyUtils;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiReference;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.PhpPsiElementFactory;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author jay
 * @date May 15, 2008 11:24:30 AM
 */
public class PhpFieldReferenceImpl extends PhpTypedElementImpl implements FieldReference, PhpReferenceWithCompletion
{
	public PhpFieldReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitFieldReference(this);
	}

	@Override
	public PsiElement getNameIdentifier()
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

	@Nonnull
	@Override
	public PhpType getType()
	{
		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getType();
		}
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	public PhpType getDeclaredType()
	{
		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getDeclaredType();
		}
		return PhpType.EMPTY;
	}

	@Override
	public ClassReference getClassReference()
	{
		PhpPsiElement reference = getFirstPsiChild();
		if(reference instanceof ClassReference)
		{
			return (ClassReference) reference;
		}
		return null;
	}

	@Override
	public PsiElement getObjectReference()
	{
		PsiElement object = getFirstPsiChild();
		if(object instanceof FieldReference || object instanceof Variable || object instanceof MethodReference)
		{
			return object;
		}
		return null;
	}

	@Override
	@Nullable
	public PsiReference getReference()
	{
		return this;
	}

	@RequiredReadAction
	@Override
	public PsiElement getElement()
	{
		return this;
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public TextRange getRangeInElement()
	{
		if(canReadName())
		{
			PsiElement nameNode = getNameIdentifier();
			int startOffset = nameNode.getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameNode.getTextLength());
		}
		return TextRange.EMPTY_RANGE;
	}

	@RequiredReadAction
	@Override
	@Nullable
	public PsiElement resolve()
	{
		CommonProcessors.FindFirstProcessor<PhpNamedElement> processor = new CommonProcessors.FindFirstProcessor<>();
		processElements(processor, getFieldName());
		return processor.getFoundValue();
	}

	@RequiredReadAction
	@Override
	public void processForCompletion(@RequiredReadAction @Nonnull Processor<PhpNamedElement> elementProcessor)
	{
		processElements(elementProcessor, null);
	}

	private boolean processElements(Processor<PhpNamedElement> processor, @Nullable String fieldName)
	{
		PhpIndex phpIndex = PhpIndex.getInstance(getProject());

		PhpType phpType = PhpType.NULL;
		PsiElement firstChild = getFirstPsiChild();
		if(firstChild instanceof PhpTypedElement)
		{
			phpType = ((PhpTypedElement) firstChild).getType();
		}
		else if(firstChild instanceof ClassReference)
		{
			phpType = ((ClassReference) firstChild).resolveLocalType();
		}

		for(String type : phpType.getTypes())
		{
			Collection<PhpClass> classes = phpIndex.getClassesByFQN(type);

			for(PhpClass targetClass : classes)
			{
				if(fieldName != null)
				{
					Field field = targetClass.findFieldByName(fieldName, true);
					if(field != null)
					{
						if(!processor.process(field))
						{
							return false;
						}
					}
				}
				else
				{
					PhpClassHierarchyUtils.processFields(targetClass, targetClass, (field, subClass, baseClass) -> processor.process(field), false);

					for(Method method : targetClass.getMethods())
					{
						if(!processor.process(method))
						{
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Returns the name of the reference target element which does not depend on import statements
	 * and other context (for example, the full-qualified name of the class if the reference targets
	 * a Java class).
	 *
	 * @return the canonical text of the reference.
	 */
	@RequiredReadAction
	@Nonnull
	@Override
	public String getCanonicalText()
	{
		return null;
	}

	@RequiredWriteAction
	@Override
	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getFieldName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
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
	 * @throws IncorrectOperationException if the rebind cannot be handled for some reason.
	 */
	@RequiredWriteAction
	@Override
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	/**
	 * Checks if the reference targets the specified element.
	 *
	 * @param element the element to check target for.
	 * @return true if the reference targets that element, false otherwise.
	 */
	@RequiredReadAction
	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		return getManager().areElementsEquivalent(resolve(), element);
	}
}
