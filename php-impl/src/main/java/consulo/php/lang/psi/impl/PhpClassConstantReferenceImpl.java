package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.CommonProcessors;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.completion.PhpVariantsUtil;
import consulo.php.completion.UsageContext;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 1:51:48 PM
 */
public class PhpClassConstantReferenceImpl extends PhpElementImpl implements ClassConstantReference
{
	private static final class OurResolver implements ResolveCache.PolyVariantResolver<PhpClassConstantReferenceImpl>
	{
		private static final OurResolver INSTANCE = new OurResolver();

		@Nonnull
		@Override
		@RequiredReadAction
		public ResolveResult[] resolve(@Nonnull PhpClassConstantReferenceImpl constantReference, boolean incompleteCode)
		{
			List<ResolveResult> results = new ArrayList<>();
			constantReference.process(element -> {
				results.add(new PsiElementResolveResult(element, true));
				return true;
			}, constantReference.getName());
			return ContainerUtil.toArray(results, ResolveResult.ARRAY_FACTORY);
		}
	}

	private static final TokenSet ourNameTokens = TokenSet.create(PhpTokenTypes.IDENTIFIER, PhpTokenTypes.kwCLASS);

	public PhpClassConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitClassConstantReference(this);
	}

	@Override
	@RequiredReadAction
	@Nullable
	public PsiElement getNameIdentifier()
	{
		return findChildByType(ourNameTokens);
	}

	@RequiredReadAction
	public boolean canReadName()
	{
		return getNameIdentifier() != null;
	}

	@Override
	@Nonnull
	@RequiredReadAction
	public String getName()
	{
		return getNameIdentifier().getText();
	}

	@Override
	public PsiReference getReference()
	{
		return this;
	}

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

	@RequiredReadAction
	@Override
	@Nonnull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(getProject()).resolveWithCaching(this, OurResolver.INSTANCE, true, incompleteCode);
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
		if(canReadName())
		{
			PsiElement element = getNameIdentifier();
			int startOffset = element.getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + element.getTextLength());
		}
		return getTextRange();
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

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(PsiUtilCore.getElementType(nameIdentifier) == PhpTokenTypes.kwCLASS)
		{
			return PhpType.STRING;
		}

		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getType();
		}
		return PhpType.EMPTY;
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public String getCanonicalText()
	{
		return getText();
	}

	@RequiredWriteAction
	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		return null;
	}

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
	private void process(@Nonnull Processor<PhpNamedElement> processor, @Nullable String name)
	{
		final ClassReference classReference = getClassReference();
		if(classReference != null)
		{
			final PsiElement psiElement = classReference.resolve();
			if(psiElement instanceof PhpClass)
			{
				processClass(name, (PhpClass) psiElement, processor);
			}
		}
	}

	private boolean processClass(@Nullable String name, @Nonnull PhpClass phpClass, @Nonnull Processor<PhpNamedElement> processor)
	{
		for(Function method : phpClass.getOwnMethods())
		{
			if(name != null)
			{
				if(Comparing.equal(name, method.getName()) && !processor.process(method))
				{
					return false;
				}
			}
			else
			{
				if(!processor.process(method))
				{
					return false;
				}
			}
		}

		for(Field field : phpClass.getOwnFields())
		{
			if(name != null)
			{
				if(Comparing.equal(name, field.getName()) && !processor.process(field))
				{
					return false;
				}
			}
			else
			{
				if(!processor.process(field))
				{
					return false;
				}
			}
		}

		PhpClass superClass = phpClass.getSuperClass();
		if(superClass != null)
		{
			if(!processClass(name, phpClass, processor))
			{
				return false;
			}
		}

		PhpClass[] implementedInterfaces = phpClass.getImplementedInterfaces();
		for(PhpClass implementedInterface : implementedInterfaces)
		{
			if(!processClass(name, implementedInterface, processor))
			{
				return false;
			}
		}
		return true;
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public Object[] getVariants()
	{
		CommonProcessors.CollectProcessor<PhpNamedElement> elements = new CommonProcessors.CollectProcessor<>();

		final UsageContext context = new UsageContext();
		final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);

		context.setClassForAccessFilter(contextClass);

		context.setCallingObjectClass(contextClass);

		process(elements, null);

		return PhpVariantsUtil.getLookupItems(elements.getResults(), context);
	}

	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if(PsiUtilCore.getElementType(nameIdentifier) == PhpTokenTypes.kwCLASS)
		{
			return true;
		}
		return false;
	}
}
