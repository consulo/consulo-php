package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.cache.psi.LightElementUtil;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import net.jay.plugins.php.completion.PhpVariantsUtil;
import net.jay.plugins.php.completion.UsageContext;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.resolve.PhpResolveResult;
import net.jay.plugins.php.lang.psi.resolve.types.PhpType;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author jay
 * @date May 15, 2008 12:35:36 PM
 */
public class MethodReferenceImpl extends PhpTypedElementImpl implements MethodReference
{

	public MethodReferenceImpl(ASTNode node)
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

	public ASTNode getNameNode()
	{
		return getNode().findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public boolean canReadName()
	{
		return getNameNode() != null;
	}

	public String getMethodName()
	{
		if(canReadName())
		{
			return getNameNode().getText();
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
	public ClassReference getClassReference()
	{
		PHPPsiElement reference = getFirstPsiChild();
		if(reference instanceof ClassReference)
		{
			return (ClassReference) reference;
		}
		return null;
	}

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
			ASTNode nameNode = getNameNode();
			int startOffset = nameNode.getPsi().getStartOffsetInParent();
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
		final ClassReference classReference = getClassReference();
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
					for(LightPhpClass klass : classes)
					{
						for(LightPhpMethod method : klass.getMethods())
						{
							if(method.getName() != null && method.getName().equals(getMethodName()))
							{
								return new ResolveResult[]{new PhpResolveResult(method.getPsi(getProject()))};
							}
						}
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
				final List<LightPhpMethod> methods = lightPhpClass.getMethods();
				for(LightPhpMethod lightMethod : methods)
				{
					if(lightMethod.getName() != null && lightMethod.getName().equals(getMethodName()))
					{
						return new ResolveResult[]{new PhpResolveResult(lightMethod.getPsi(getProject()))};
					}
				}
			}
		}
		return new ResolveResult[0];
	}

	public Object[] getVariants()
	{
		final UsageContext context = new UsageContext();
		final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);
		LightPhpClass lightContextClass = null;
		if(contextClass != null)
		{
			lightContextClass = LightElementUtil.findLightClassByPsi(contextClass);
		}
		if(lightContextClass != null)
		{
			context.setClassForAccessFilter(lightContextClass);
		}
		context.setModifier(getReferenceType());

		final ClassReference classReference = getClassReference();
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
		}
		return new Object[0];
	}

	public String getCanonicalText()
	{
		return null;
	}

	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getMethodName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), constantReference.getNameNode());
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
		if(element instanceof Method)
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
