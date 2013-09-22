package org.consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpConstantReference;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpFieldReference;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpMethodReference;
import org.consulo.php.lang.psi.PhpPsiElementFactory;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.consulo.php.lang.psi.resolve.PhpResolveProcessor;
import org.consulo.php.lang.psi.resolve.ResolveUtil;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date May 15, 2008 12:35:36 PM
 */
public class PhpMethodReferenceImpl extends PhpTypeOwnerImpl implements PhpMethodReference
{

	public PhpMethodReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitMethodReference(this);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Override
	public boolean canReadName()
	{
		return getNameIdentifier() != null;
	}

	@Override
	public String getMethodName()
	{
		if(canReadName())
		{
			return getNameIdentifier().getText();
		}
		return null;
	}

	@Override
	public PsiReference getReference()
	{
		if(canReadName())
		{
			return this;
		}
		return null;
	}

	@Override
	@Nullable
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
	@Nullable
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
	public boolean isStatic()
	{
		if(getClassReference() != null && !getClassReference().getText().equals("parent"))
		{
			return true;
		}
		return false;
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
	@Override
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

	@Override
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

						final List<LookupElement> list = PhpVariantsUtil.getLookupItems(klass.getFunctions(), context);
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

				final List<LookupElement> list = PhpVariantsUtil.getLookupItems(lightPhpClass.getFunctions(), context);
				return list.toArray(new LookupElement[list.size()]);
			}
		}          */
		return new Object[0];
	}

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
		if(nameIdentifier != null && !getMethodName().equals(name))
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

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof PhpFunction)
		{
			return element == resolve();
		}
		return false;
	}

	@Override
	public boolean isSoft()
	{
		return false;
	}
}
