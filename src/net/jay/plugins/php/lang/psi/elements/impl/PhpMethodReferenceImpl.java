package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.resolve.PhpResolveProcessor;
import net.jay.plugins.php.lang.psi.resolve.ResolveUtil;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jay
 * @date May 15, 2008 12:35:36 PM
 */
public class PhpMethodReferenceImpl extends PhpTypedElementImpl implements PhpMethodReference
{

	public PhpMethodReferenceImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpMethodReference(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public boolean canReadName()
	{
		return getNameIdentifier() != null;
	}

	public String getMethodName()
	{
		if(canReadName())
		{
			return getNameIdentifier().getText();
		}
		return null;
	}

	public PsiReference getReference()
	{
		if(canReadName())
			return this;
		return null;
	}

	@Nullable
	public PhpClassReference getClassReference()
	{
		PHPPsiElement reference = getFirstPsiChild();
		if(reference instanceof PhpClassReference)
		{
			return (PhpClassReference) reference;
		}
		return null;
	}

	@Nullable
	public PsiElement getObjectReference()
	{
		PsiElement object = getFirstPsiChild();
		if(object instanceof FieldReference || object instanceof PhpVariableReference || object instanceof PhpMethodReference)
		{
			return object;
		}
		return null;
	}

	public PhpModifier getReferenceType()
	{
		PhpModifier modifier = new PhpModifier();
		//noinspection ConstantConditions
		if(getClassReference() != null && !getClassReference().getText().equals("parent"))
		{
			modifier.setState(PhpModifier.State.STATIC);
		}
		return modifier;
	}

	public PsiElement getElement()
	{
		return this;
	}

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

	@Nullable
	public PsiElement resolve()
	{
		ResolveResult[] results = multiResolve(false);
		if(results.length == 1)
		{
			return results[0].getElement();
		}
		return null;
	}

	//TODO multiresolve
	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		PhpResolveProcessor processor = new PhpResolveProcessor(this, getMethodName(), PhpResolveProcessor.ResolveKind.METHOD);
		ResolveUtil.treeWalkUp(this, processor);
		Collection<PsiElement> declarations = processor.getResult();

		List<ResolveResult> result = new ArrayList<ResolveResult>(declarations.size());
		for(final PsiElement element : declarations)
		{
			if(declarations.size() > 1 && element == this)
			{
				continue;
			}
			result.add(new PsiElementResolveResult(element, true));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	public Object[] getVariants()
	{
		/*final UsageContext context = new UsageContext();
		final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);

		if(contextClass != null)
		{
			context.setClassForAccessFilter(contextClass);
		}
		context.setModifier(getReferenceType());

		final PhpClassReference classReference = getClassReference();
		if(classReference != null)
		{
			final PsiElement element = classReference.resolve();
			if(element instanceof PhpClass && ((PhpClass) element).getName() != null)
			{
				DeclarationsIndex index = DeclarationsIndex.getInstance(this);
				if(index != null)
				{
					//noinspection ConstantConditions
					final List<LightPhpClass> classes = index.getClassesByName(((PhpClass) element).getName());
					if(classes.size() == 1)
					{
						final LightPhpClass klass = classes.get(0);
						context.setCallingObjectClass(klass);

						final List<LookupElement> list = PhpVariantsUtil.getLookupItems(klass.getMethods(), context);
						return list.toArray(new LookupElement[list.size()]);
					}
				}
			}
		}
		final PsiElement objectReference = getObjectReference();
		if(objectReference instanceof PhpTypedElement)
		{
			final PhpType type = ((PhpTypedElement) objectReference).getType();
			final LightPhpClass lightPhpClass = type.getType();
			if(lightPhpClass != null)
			{
				context.setCallingObjectClass(lightPhpClass);

				final List<LookupElement> list = PhpVariantsUtil.getLookupItems(lightPhpClass.getMethods(), context);
				return list.toArray(new LookupElement[list.size()]);
			}
		}          */
		return new Object[0];
	}

	public String getCanonicalText()
	{
		return null;
	}

	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getMethodName().equals(name))
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
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the rebind cannot be handled for some reason.
	 */
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof PhpMethod)
		{
			return element == resolve();
		}
		return false;
	}

	public boolean isSoft()
	{
		return false;
	}
}
