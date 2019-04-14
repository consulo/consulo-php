package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.CommonProcessors;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.completion.PhpVariantsUtil;
import consulo.php.completion.UsageContext;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 15, 2008 11:24:30 AM
 */
public class PhpFieldReferenceImpl extends PhpTypedElementImpl implements FieldReference
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
	@Nonnull
	@Override
	public Object[] getVariants()
	{
		UsageContext context = new UsageContext();
		final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
		if(contextClass != null)
		{
			context.setClassForAccessFilter(contextClass);
		}

		context.setCallingObjectClass(contextClass);

		List<Object> elements = new ArrayList<>();
		processElements(e -> {
			LookupElement lookupElement = PhpVariantsUtil.getLookupItem(e, context);
			elements.add(lookupElement);
			return true;
		}, null);

		return ArrayUtil.toObjectArray(elements);
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

			for(PhpClass aClass : classes)
			{
				if(fieldName != null)
				{
					Field field = aClass.findFieldByName(fieldName, true);
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
					for(Field field : aClass.getFields())
					{
						if(!processor.process(field))
						{
							return false;
						}
					}

					for(Method method : aClass.getMethods())
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
	 * @throws com.intellij.util.IncorrectOperationException if the rebind cannot be handled for some reason.
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

	/**
	 * Returns false if the underlying element is guaranteed to be a reference, or true
	 * if the underlying element is a possible reference which should not be reported as
	 * an error if it fails to resolve. For example, a text in an XML file which looks
	 * like a full-qualified Java class name is a soft reference.
	 *
	 * @return true if the refence is soft, false otherwise.
	 */
	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		return false;
	}
}
