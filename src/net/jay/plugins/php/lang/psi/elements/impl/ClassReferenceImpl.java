package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.completion.ClassUsageContext;
import net.jay.plugins.php.completion.PhpVariantsUtil;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;
import net.jay.plugins.php.lang.psi.PhpPsiElementFactory;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.resolve.PhpResolveResult;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date May 11, 2008 9:56:57 PM
 */
public class ClassReferenceImpl extends PHPPsiElementImpl implements ClassReference
{

	public ClassReferenceImpl(ASTNode node)
	{
		super(node);
	}

	public ASTNode getNameNode()
	{
		return getNode().findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public String getReferenceName()
	{
		return getText();
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpClassReference(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	public PsiReference getReference()
	{
		return this;
	}

	@SuppressWarnings({"ConstantConditions"})
	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		boolean instantiation = getParent() instanceof NewExpression;

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
				Method constructor = klass.getConstructor();
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
			final PhpInterface anInterface = (PhpInterface) interfaces.get(i).getPsi(getProject());
			result[i + classes.size()] = new PhpResolveResult(anInterface);
		}
		return result;
	}

	public PsiElement getElement()
	{
		return this;
	}

	public TextRange getRangeInElement()
	{
		return new TextRange(0, getTextLength());
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

	public String getCanonicalText()
	{
		return null;
	}

	public PsiElement handleElementRename(String name) throws IncorrectOperationException
	{
		//noinspection ConstantConditions
		if(getNameNode() != null && !getReferenceName().equals(name))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), name);
			getNameNode().getTreeParent().replaceChild(getNameNode(), constantReference.getNameNode());
		}
		return this;
	}

	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	public boolean isReferenceTo(PsiElement element)
	{
		if(element instanceof PhpClass || element instanceof PhpInterface || element instanceof Method)
		{
			final PsiElement resolveResult = resolve();
			final boolean isReference = element == resolveResult;
			if(isReference)
			{
				return isReference;
			}
			if(element instanceof PhpClass && resolveResult instanceof Method)
			{
				return PsiTreeUtil.getParentOfType(resolveResult, PhpClass.class) == element;
			}
		}
		return false;
	}

	private ClassUsageContext getUsageContext()
	{
		ClassUsageContext context = new ClassUsageContext();
		//noinspection ConstantConditions
		final IElementType parent = getParent().getNode().getElementType();
		if(parent == PHPElementTypes.INSTANCEOF_EXPRESSION)
		{
			context.setInInstanceof(true);
		}
		if(parent == PHPElementTypes.EXTENDS_LIST)
		{
			context.setInExtends(true);
		}
		if(parent == PHPElementTypes.IMPLEMENTS_LIST)
		{
			context.setInImplements(true);
		}
		if(parent == PHPElementTypes.NEW_EXPRESSION)
		{
			context.setInNew(true);
		}
		return context;
	}

	public Object[] getVariants()
	{
		DeclarationsIndex index = DeclarationsIndex.getInstance(this);
		if(index == null)
		{
			return new Object[0];
		}

		List<LightPhpElement> variants = new ArrayList<LightPhpElement>();
		for(String className : index.getAllClassNames())
		{
			variants.addAll(index.getClassesByName(className));
		}
		for(String interfaceName : index.getAllInterfaceNames())
		{
			variants.addAll(index.getInterfacesByName(interfaceName));
		}
		final List<LookupElement> list = PhpVariantsUtil.getLookupItemsForClasses(variants, getUsageContext());
		return list.toArray(new LookupElement[list.size()]);
	}

	public boolean isSoft()
	{
		return false;
	}
}
