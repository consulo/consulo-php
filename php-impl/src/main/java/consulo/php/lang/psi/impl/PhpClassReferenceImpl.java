package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ConstantReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.completion.ClassUsageContext;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.PhpResolveResult;
import consulo.php.lang.psi.resolve.ResolveUtil;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 11, 2008 9:56:57 PM
 */
public class PhpClassReferenceImpl extends PhpElementImpl implements ClassReference
{
	private static final class OurResolver implements ResolveCache.PolyVariantResolver<PhpClassReferenceImpl>
	{
		public static final OurResolver INSTANCE = new OurResolver();

		@Nonnull
		@Override
		public ResolveResult[] resolve(@Nonnull PhpClassReferenceImpl phpClassReference, boolean incompleteCode)
		{
			return phpClassReference.multiResolveImpl(incompleteCode);
		}
	}

	public PhpClassReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getReferenceElement()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Override
	public boolean isAbsolute()
	{
		return findChildByType(PhpTokenTypes.SLASH) != null;
	}

	@Nonnull
	@Override
	public PhpType resolveLocalType()
	{
		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getType();
		}
		return PhpType.EMPTY;
	}

	@Nullable
	@Override
	public ClassReference getQualifier()
	{
		return findChildByClass(ClassReference.class);
	}

	@Override
	public String getReferenceName()
	{
		PsiElement nameIdentifier = getReferenceElement();
		return nameIdentifier != null ? nameIdentifier.getText() : "";
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitClassReference(this);
	}

	@Override
	public PsiReference getReference()
	{
		return this;
	}

	@RequiredReadAction
	@Override
	@Nonnull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(getProject()).resolveWithCaching(this, OurResolver.INSTANCE, true, incompleteCode);
	}

	@RequiredReadAction
	@Nonnull
	private ResolveResult[] multiResolveImpl(boolean incompleteCode)
	{
		ResolveKind resolveKind = findResolveKind();
		String referenceName = getReferenceName();
		String name = referenceName;
		ClassReference qualifier = getQualifier();

		switch(resolveKind)
		{
			case TO_STATIC_CLASS:
				PhpClass thisStaticClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
				if(thisStaticClass != null)
				{
					return new ResolveResult[]{new PhpResolveResult(thisStaticClass)};
				}
				return ResolveResult.EMPTY_ARRAY;
			case TO_NAMESPACE:
				String namespaceText = getElement().getText();
				if(!StringUtil.startsWith(namespaceText, "\\"))
				{
					namespaceText = "\\" + namespaceText;
				}

				Collection<PhpNamespace> namespacesByName = PhpIndex.getInstance(getProject()).getNamespacesByName(namespaceText);

				if(namespacesByName.isEmpty())
				{
					return ResolveResult.EMPTY_ARRAY;
				}

				List<ResolveResult> results = new ArrayList<>();
				for(PhpNamespace namespace : namespacesByName)
				{
					results.add(new PsiElementResolveResult(namespace, true));
				}
				return ContainerUtil.toArray(results, ResolveResult.ARRAY_FACTORY);
			case TO_FQ_CLASS:
				if(PhpClass.SELF.equals(name))
				{
					PhpClass parentOfType = PsiTreeUtil.getParentOfType(this, PhpClass.class);

					if(parentOfType != null)
					{
						return new ResolveResult[]{new PsiElementResolveResult(parentOfType, true)};
					}
					else
					{
						return ResolveResult.EMPTY_ARRAY;
					}
				}
				else if(PhpClass.PARENT.equals(name))
				{
					PhpClass parentOfType = PsiTreeUtil.getParentOfType(this, PhpClass.class);

					if(parentOfType != null)
					{
						PhpClass superClass = parentOfType.getSuperClass();

						if(superClass != null)
						{
							return new ResolveResult[]{new PsiElementResolveResult(superClass, true)};
						}
						else
						{
							return ResolveResult.EMPTY_ARRAY;
						}
					}
					else
					{
						return ResolveResult.EMPTY_ARRAY;
					}
				}

				StringBuilder builder = new StringBuilder();
				builder.append("\\");

				if(qualifier != null)
				{
					PsiElement resolved = qualifier.resolve();
					if(!(resolved instanceof PhpNamespace))
					{
						return ResolveResult.EMPTY_ARRAY;
					}

					builder.append(((PhpNamespace) resolved).getName());
					builder.append("\\");
				}

				builder.append(name);

				String fullyClassName = builder.toString().replace(" ", "");
				Collection<PhpClass> phpClasses = PhpIndex.getInstance(getProject()).getClassesByFQN(fullyClassName);
				List<ResolveResult> resultList = new SmartList<>();
				for(PhpClass phpClass : phpClasses)
				{
					resultList.add(new PsiElementResolveResult(phpClass, true));
				}
				return resultList.isEmpty() ? ResolveResult.EMPTY_ARRAY : resultList.toArray(new ResolveResult[resultList.size()]);
			case TO_CLASS:
				if(PhpClass.SELF.equals(name))
				{
					final PhpClass phpClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
					if(phpClass != null)
					{
						return new ResolveResult[]{new PhpResolveResult(phpClass)};
					}
					return ResolveResult.EMPTY_ARRAY;
				}

				if(PhpClass.PARENT.equals(name))
				{
					final PhpClass phpClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
					if(phpClass != null)
					{
						final PhpClass superClass = phpClass.getSuperClass();
						if(superClass != null)
						{
							return new ResolveResult[]{new PhpResolveResult(superClass)};
						}
					}
					return ResolveResult.EMPTY_ARRAY;
				}

				PhpResolveProcessor processor = new PhpResolveProcessor(this, referenceName, PhpResolveProcessor.ElementKind.CLASS);
				boolean absolute = isAbsolute();
				if(absolute)
				{
					if(!StringUtil.startsWith(referenceName, "\\"))
					{
						referenceName = "\\" + referenceName;
					}

					Collection<PhpClass> classes = PhpIndex.getInstance(getProject()).getClassesByFQN(referenceName);
					for(PhpClass aClass : classes)
					{
						processor.execute(aClass, ResolveState.initial());
					}
				}
				else
				{
					ResolveUtil.treeWalkUp(this, processor);
				}

				if(!processor.getResult().isEmpty())
				{
					return processor.getResult().stream().map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
				}

				if(!isAbsolute() && qualifier == null)
				{
					Collection<PhpClass> classes = PhpIndex.getInstance(getProject()).getClassesByFQN("\\" + getReferenceName());
					return classes.stream().map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
				}
			default:
				return ResolveResult.EMPTY_ARRAY;
		}
	}

	@Nonnull
	public ResolveKind findResolveKind()
	{
		PsiElement staticKeyword = findChildByType(PhpTokenTypes.STATIC_KEYWORD);
		if(staticKeyword != null)
		{
			return ResolveKind.TO_STATIC_CLASS;
		}

		PsiElement parent = getParent();
		if(parent instanceof PhpClassReferenceImpl)
		{
			PhpUseImpl use = PsiTreeUtil.getParentOfType(this, PhpUseImpl.class);
			if(use != null)
			{
				return ResolveKind.TO_NAMESPACE;
			}
		}
		else if(parent instanceof PhpUseImpl)
		{
			return ResolveKind.TO_FQ_CLASS;
		}

		return ResolveKind.TO_CLASS;
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
		PsiElement referenceElement = getReferenceElement();
		if(referenceElement == null)
		{
			return getElement().getTextRange();
		}
		return new TextRange(referenceElement.getStartOffsetInParent(), referenceElement.getStartOffsetInParent() + referenceElement.getTextLength());
	}

	@RequiredReadAction
	@Override
	@Nullable
	public PsiElement resolve()
	{
		if(findResolveKind() == ResolveKind.TO_NAMESPACE)
		{
			ResolveResult[] results = multiResolve(false);
			return results.length > 0 ? results[0].getElement() : null;
		}

		ResolveResult[] results = multiResolve(false);
		if(results.length == 1)
		{
			return results[0].getElement();
		}
		return null;
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
	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getReferenceElement();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getReferenceName().equals(name))
		{
			ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return this;
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

	private ClassUsageContext getUsageContext()
	{
		ClassUsageContext context = new ClassUsageContext();
		//noinspection ConstantConditions
		IElementType parent = getParent().getNode().getElementType();
		if(parent == PhpElementTypes.INSTANCEOF_EXPRESSION)
		{
			context.setInInstanceof(true);
		}
		if(parent == PhpElementTypes.EXTENDS_LIST)
		{
			context.setInExtends(true);
		}
		if(parent == PhpElementTypes.IMPLEMENTS_LIST)
		{
			context.setInImplements(true);
		}
		if(parent == PhpElementTypes.NEW_EXPRESSION)
		{
			context.setInNew(true);
		}
		return context;
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public Object[] getVariants()
	{
		/*Collection<PhpClass> classes = PhpIndexUtil.getClasses(this);
		final List<LookupElement> list = PhpVariantsUtil.getLookupItemsForClasses(classes, getUsageContext());
		return list.toArray(new LookupElement[list.size()]);   */
		return new Object[0];
	}

	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		return false;
	}

	public enum ResolveKind
	{
		TO_NAMESPACE,
		TO_FQ_CLASS,
		TO_CLASS,
		TO_STATIC_CLASS,

		TO_UNKNOWN
	}
}
