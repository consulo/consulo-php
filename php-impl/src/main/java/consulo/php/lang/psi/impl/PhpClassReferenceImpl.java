package consulo.php.lang.psi.impl;

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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SmartList;
import consulo.php.PhpConstants;
import consulo.php.completion.ClassUsageContext;
import consulo.php.index.PhpFullFqClassIndex;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpClassReference;
import consulo.php.lang.psi.PhpConstantReference;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpNamespaceStatement;
import consulo.php.lang.psi.PhpPackage;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.psi.PsiPackage;
import consulo.psi.PsiPackageManager;

/**
 * @author jay
 * @date May 11, 2008 9:56:57 PM
 */
public class PhpClassReferenceImpl extends PhpElementImpl implements PhpClassReference
{
	public PhpClassReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getReferenceElement()
	{
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Nullable
	@Override
	public PhpClassReference getQualifier()
	{
		return findChildByClass(PhpClassReference.class);
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

	@Override
	@SuppressWarnings({"ConstantConditions"})
	@Nonnull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		PsiElement parent = getParent();
		ResolveKind resolveKind = findResolveKind();
		String name = getReferenceName();
		PhpClassReference qualifier = getQualifier();
		StringBuilder builder = null;

		switch(resolveKind)
		{

			case TO_NAMESPACE:
				builder = new StringBuilder();


				if(qualifier != null)
				{
					PsiElement resolve = qualifier.resolve();
					if(!(resolve instanceof PhpPackage))
					{
						return ResolveResult.EMPTY_ARRAY;
					}

					builder.append(((PhpPackage) resolve).getQualifiedName());
					builder.append(".");
				}
				else
				{
					String text = getText();
					if(text.length() > 0 && text.charAt(0) != '\\')
					{

					}
				}

				builder.append(name);

				String packageName = builder.toString();

				PsiPackage aPackage = PsiPackageManager.getInstance(getProject()).findPackage(packageName, PhpModuleExtension.class);
				if(aPackage != null)
				{
					return new ResolveResult[]{new PsiElementResolveResult(aPackage, true)};
				}
				else
				{
					return ResolveResult.EMPTY_ARRAY;
				}
			case TO_FQ_CLASS:
				if(PhpConstants.SELF.equals(name))
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
				else if(PhpConstants.PARENT.equals(name))
				{
					PhpClass parentOfType = PsiTreeUtil.getParentOfType(this, PhpClass.class);

					if(parentOfType != null)
					{
						PhpClass superClass = parentOfType.getSuperClass();

						if(superClass != null)
						{
							return new ResolveResult[]{new PsiElementResolveResult(parentOfType, true)};
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

				builder = new StringBuilder();

				if(qualifier != null)
				{
					PsiElement resolve = qualifier.resolve();
					if(!(resolve instanceof PhpPackage))
					{
						return ResolveResult.EMPTY_ARRAY;
					}

					builder.append(((PhpPackage) resolve).getQualifiedName());
					builder.append(".");
				}

				builder.append(name);

				String fullyClassName = builder.toString().replace(" ", "");
				Collection<PhpClass> phpClasses = PhpFullFqClassIndex.INSTANCE.get(fullyClassName, getProject(), getResolveScope());
				List<ResolveResult> resultList = new SmartList<ResolveResult>();
				for(PhpClass phpClass : phpClasses)
				{
					resultList.add(new PsiElementResolveResult(phpClass, true));
				}
				return resultList.isEmpty() ? ResolveResult.EMPTY_ARRAY : resultList.toArray(new ResolveResult[resultList.size()]);
			default:
				return ResolveResult.EMPTY_ARRAY;
		}


		/*boolean instantiation = getParent() instanceof NewExpression;

		if(getReferenceName().equals("self"))
		{
			final PhpClass phpClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
			if(phpClass != null)
			{
				return new ResolveResult[]{new PhpResolveResult(phpClass)};
			}
		}
		if(getReferenceName().equals("parent"))
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
		}

		DeclarationsIndex index = DeclarationsIndex.getInstance(this);
		if(index == null)
		{
			return ResolveResult.EMPTY_ARRAY;
		}
		List<LightPhpClass> classes = index.getClassesByName(getReferenceName());
		List<LightPhpInterface> interfaces = index.getInterfacesByName(getReferenceName());
		ResolveResult[] result = new ResolveResult[classes.size() + interfaces.size()];
		for(int i = 0; i < classes.size(); i++)
		{
			final PsiElement element;
			final PhpClass klass = (PhpClass) classes.get(i).getPsi(getProject());
			if(klass != null && instantiation)
			{
				PhpMethod constructor = klass.getConstructor();
				if(constructor == null)
				{
					element = klass;
				}
				else
				{
					element = constructor;
				}
			}
			else
			{
				element = klass;
			}
			result[i] = new PhpResolveResult(element);
		}
		for(int i = 0; i < interfaces.size(); i++)
		{
			final PhpClass anInterface = (PhpClass) interfaces.get(i).getPsi(getProject());
			result[i + classes.size()] = new PhpResolveResult(anInterface);
		}
		return result; */
	}

	public ResolveKind findResolveKind()
	{
		PsiElement parent = getParent();
		if(parent instanceof PhpClassReferenceImpl)
		{
			ResolveKind resolveKind = ((PhpClassReferenceImpl) parent).findResolveKind();
			if(resolveKind == ResolveKind.TO_FQ_CLASS)
			{
				return ResolveKind.TO_NAMESPACE;
			}

			return resolveKind;
		}
		else if(parent instanceof PhpNamespaceStatement)
		{
			return ResolveKind.TO_NAMESPACE;
		}

		return ResolveKind.TO_FQ_CLASS;
	}

	@Override
	public PsiElement getElement()
	{
		return this;
	}

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

	@Override
	public String getCanonicalText()
	{
		return getText();
	}

	@Override
	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getReferenceElement();
		//noinspection ConstantConditions
		if(nameIdentifier != null && !getReferenceName().equals(name))
		{
			PhpConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return this;
	}

	@Override
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		PsiElement resolve = resolve();
		if(resolve == element)
		{
			return true;
		}

		if(element instanceof PhpClass || element instanceof PhpFunction)
		{
			if(element instanceof PhpClass && resolve instanceof PhpFunction)
			{
				return PsiTreeUtil.getParentOfType(resolve, PhpClass.class) == element;
			}
		}
		return false;
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

	@Override
	public Object[] getVariants()
	{
		/*Collection<PhpClass> classes = PhpIndexUtil.getClasses(this);
		final List<LookupElement> list = PhpVariantsUtil.getLookupItemsForClasses(classes, getUsageContext());
		return list.toArray(new LookupElement[list.size()]);   */
		return new Object[0];
	}

	@Override
	public boolean isSoft()
	{
		return false;
	}

	public enum ResolveKind
	{
		TO_NAMESPACE,
		TO_FQ_CLASS,

		TO_UNKNOWN
	}
}
