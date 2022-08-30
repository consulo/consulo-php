package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.application.util.function.Processor;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ResolveResult;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.util.IncorrectOperationException;
import consulo.php.impl.completion.ClassUsageContext;
import consulo.php.impl.index.PhpDefineIndex;
import consulo.php.impl.lang.psi.resolve.PhpResolveResult;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author jay
 * @since Jun 30, 2008 1:44:07 AM
 */
public class PhpConstantReferenceImpl extends PhpNamedElementImpl implements ConstantReference, PhpReferenceWithCompletion, PsiPolyVariantReference
{
	private static class OurResolver implements ResolveCache.PolyVariantResolver<PhpConstantReferenceImpl>
	{
		private static final OurResolver ourInstance = new OurResolver();

		@Nonnull
		@Override
		public ResolveResult[] resolve(@Nonnull PhpConstantReferenceImpl constantReference, boolean incompleteCode)
		{
			Collection<PhpDefine> phpDefines = PhpDefineIndex.INSTANCE.get(constantReference.getName(), constantReference.getProject(), constantReference.getResolveScope());

			PhpDefine define = ContainerUtil.getFirstItem(phpDefines);
			if(define != null)
			{
				return new ResolveResult[]{new PhpResolveResult(define)};
			}
			return ResolveResult.EMPTY_ARRAY;
		}
	}

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
		ResolveResult[] results = multiResolve(false);
		return results.length == 1 ? results[0].getElement() : null;
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(getProject()).resolveWithCaching(this, OurResolver.ourInstance, true, incompleteCode);
	}

	@Override
	public ClassUsageContext createClassUsageContext()
	{
		ClassUsageContext context = new ClassUsageContext();
		context.setStatic(true);
		return context;
	}

	@RequiredReadAction
	@Override
	public void processForCompletion(@Nonnull Processor<PhpNamedElement> elementProcessor)
	{
		Project project = getProject();
		PhpIndex index = PhpIndex.getInstance(project);
		for(String className : index.getAllClassFqns(null))
		{
			for(PhpClass phpClass : index.getClassesByFQN(className))
			{
				elementProcessor.process(phpClass);
			}
		}

		GlobalSearchScope scope = getResolveScope();

		Collection<String> keys = PhpDefineIndex.INSTANCE.getAllKeys(project);

		for(String key : keys)
		{
			Collection<PhpDefine> defines = PhpDefineIndex.INSTANCE.get(key, project, scope);

			for(PhpDefine define : defines)
			{
				elementProcessor.process(define);
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
	 * @throws IncorrectOperationException if the rename cannot be handled for some reason.
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

	/**
	 * Renames the element.
	 *
	 * @param name the new element name.
	 * @return the element corresponding to this element after the rename (either <code>this</code>
	 * or a different element if the rename caused the element to be replaced).
	 * @throws IncorrectOperationException if the modification is not supported or not possible for some reason.
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
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getNode();
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
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
