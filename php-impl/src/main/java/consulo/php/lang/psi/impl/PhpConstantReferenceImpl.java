package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.completion.ClassUsageContext;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 30, 2008 1:44:07 AM
 */
public class PhpConstantReferenceImpl extends PhpNamedElementImpl implements ConstantReference, PhpReferenceWithCompletion
{
	public PhpConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitConstant(this);
	}

	@Override
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

	@Nonnull
	@RequiredReadAction
	@Override
	public TextRange getRangeInElement()
	{
		final PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
		{
			int startOffset = nameIdentifier.getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameIdentifier.getTextLength());
		}
		return TextRange.EMPTY_RANGE;
	}

	@RequiredReadAction
	@Override
	@Nullable
	public PsiElement resolve()
	{
		return null;
	}

	@Override
	public ClassUsageContext createClassUsageContext()
	{
		ClassUsageContext context = new ClassUsageContext();
		context.setStatic(true);
		return context;
	}

	@Override
	public void processForCompletion(@Nonnull Processor<PhpNamedElement> elementProcessor)
	{
		PhpIndex index = PhpIndex.getInstance(getProject());
		for(String className : index.getAllClassFqns(null))
		{
			for(PhpClass phpClass : index.getClassesByFQN(className))
			{
				elementProcessor.process(phpClass);
			}
		}
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public String getCanonicalText()
	{
		return getText();
	}

	/**
	 * Called when the reference target element has been renamed, in order to change the reference
	 * text according to the new name.
	 *
	 * @param newElementName the new name of the target element.
	 * @return the new underlying element of the reference.
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the rename cannot be handled for some reason.
	 */
	@RequiredWriteAction
	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		return null;
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

	/**
	 * Renames the element.
	 *
	 * @param name the new element name.
	 * @return the element corresponding to this element after the rename (either <code>this</code>
	 *         or a different element if the rename caused the element to be replaced).
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the modification is not supported or not possible for some reason.
	 */
	@RequiredWriteAction
	@Override
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		return null;
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean isInternal()
	{
		return false;
	}
}
