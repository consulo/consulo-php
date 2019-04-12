package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import consulo.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.FieldReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import consulo.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.ResolveUtil;
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
	@Nonnull
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
	@Override
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof Function)
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
