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
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.PhpClassHierarchyUtils;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.completion.PhpVariantsUtil;
import consulo.php.completion.UsageContext;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.resolve.PhpResolveResult;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 15, 2008 12:35:36 PM
 */
public class PhpMethodReferenceImpl extends PhpTypedElementImpl implements MethodReference
{
	public PhpMethodReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitMethodReference(this);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Override
	public String getNamespaceName()
	{
		return null;
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
	@Nullable
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
	public boolean isStatic()
	{
		if(getClassReference() != null && !getClassReference().getText().equals(PhpClass.PARENT))
		{
			return true;
		}
		return false;
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
		return null;
	}

	@RequiredReadAction
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

	@RequiredReadAction
	@Override
	@Nonnull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<>();

		process(element -> result.add(new PhpResolveResult(element)), getMethodName());

		return result.toArray(new ResolveResult[result.size()]);
	}

	@RequiredReadAction
	private void process(@RequiredReadAction Processor<PhpNamedElement> processor, @Nullable String name)
	{
		PhpPsiElement firstChild = getFirstPsiChild();
		PhpType phpType = null;
		if(firstChild instanceof Variable)
		{
			PsiElement element = ((Variable) firstChild).resolve();
			if(element instanceof PhpTypedElement)
			{
				phpType = ((PhpTypedElement) element).getInferredType();
			}
			else
			{
				phpType = ((Variable) firstChild).getType();
			}
		}
		else if(firstChild instanceof PhpTypedElement)
		{
			phpType = ((PhpTypedElement) firstChild).getType();
		}
		else if(firstChild instanceof ClassReference)
		{
			phpType = ((ClassReference) firstChild).resolveLocalType();
		}

		List<PhpClass> owners = new ArrayList<>();
		if(phpType != null)
		{
			PhpIndex phpIndex = PhpIndex.getInstance(getProject());
			for(String type : phpType.getTypes())
			{
				Collection<PhpClass> classesByFQN = phpIndex.getClassesByFQN(type);
				owners.addAll(classesByFQN);
			}
		}
		else
		{
			PhpClass phpClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
			if(phpClass != null)
			{
				owners.add(phpClass);
			}
		}

		for(PhpClass owner : owners)
		{
			if(name != null)
			{
				Method methodByName = owner.findMethodByName(name);
				if(methodByName != null)
				{
					if(!processor.process(methodByName))
					{
						return;
					}
				}
			}
			else
			{
				PhpClassHierarchyUtils.processMethods(owner, owner, (method, subClass, baseClass) -> processor.process(method), false);
			}
		}
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public Object[] getVariants()
	{
		List<LookupElement> elements = new ArrayList<>();
		process(element -> {
			elements.add(PhpVariantsUtil.getLookupItem(element, new UsageContext()));
			return true;
		}, null);
		return ArrayUtil.toObjectArray(elements);
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public String getCanonicalText()
	{
		return getText();
	}

	@RequiredWriteAction
	@Override
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
	 * @throws IncorrectOperationException if the rebind cannot be handled for some reason.
	 */
	@RequiredWriteAction
	@Override
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@RequiredReadAction
	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		return getManager().areElementsEquivalent(resolve(), element);
	}

	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		return false;
	}
}
