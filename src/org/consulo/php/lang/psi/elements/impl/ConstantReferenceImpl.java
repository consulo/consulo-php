package org.consulo.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.completion.ClassUsageContext;
import org.consulo.php.lang.psi.elements.ConstantReference;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 30, 2008 1:44:07 AM
 */
public class ConstantReferenceImpl extends PhpNamedElementImpl implements ConstantReference
{

	public ConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpConstant(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	public PsiReference getReference()
	{
		return this;
	}

	public PsiElement getElement()
	{
		return this;
	}

	public TextRange getRangeInElement()
	{
		final PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null)
		{
			int startOffset = nameIdentifier.getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameIdentifier.getTextLength());
		}
		return null;
	}

	@Nullable
	public PsiElement resolve()
	{
		return null;
	}

	private ClassUsageContext getUsageContext()
	{
		ClassUsageContext context = new ClassUsageContext();
		context.setStatic(true);
		return context;
	}

	public Object[] getVariants()
	{
		/*DeclarationsIndex index = DeclarationsIndex.getInstance(this);
		if(index == null)
		{
			return new Object[0];
		}

		List<LightPhpElement> variants = new ArrayList<LightPhpElement>();
		for(String className : index.getAllClassNames())
		{
			variants.addAll(index.getClassesByName(className));
		}

		final List<LookupElement> list = PhpVariantsUtil.getLookupItemsForClasses(variants, getUsageContext());

		List<LightPhpFunction> functions = new ArrayList<LightPhpFunction>();
		for(String functionName : index.getAllFunctionNames())
		{
			functions.addAll(index.getFunctionsByName(functionName));
		}
		list.addAll(PhpVariantsUtil.getLookupItems(functions, null));
		return list.toArray(new LookupElement[list.size()]);     */
		return new Object[0];
	}

	public String getCanonicalText()
	{
		return null;
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

	/**
	 * Renames the element.
	 *
	 * @param name the new element name.
	 * @return the element corresponding to this element after the rename (either <code>this</code>
	 *         or a different element if the rename caused the element to be replaced).
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the modification is not supported or not possible for some reason.
	 */
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}
}
