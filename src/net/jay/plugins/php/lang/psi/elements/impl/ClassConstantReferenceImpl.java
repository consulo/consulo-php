package net.jay.plugins.php.lang.psi.elements.impl;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.cache.psi.LightElementUtil;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpElement;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import net.jay.plugins.php.completion.PhpVariantsUtil;
import net.jay.plugins.php.completion.UsageContext;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.elements.ClassConstantReference;
import net.jay.plugins.php.lang.psi.elements.ClassReference;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

/**
 * @author jay
 * @date Jun 18, 2008 1:51:48 PM
 */
public class ClassConstantReferenceImpl extends PHPPsiElementImpl implements ClassConstantReference
{

	public ClassConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpClassConstantReference(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	private ASTNode getNameNode()
	{
		return getNode().findChildByType(PHPTokenTypes.IDENTIFIER);
	}

	public boolean canReadName()
	{
		return getNameNode() != null;
	}

	public String getConstantName()
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

	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return new ResolveResult[0];
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
		return null;
	}

	public String getCanonicalText()
	{
		return null;
	}

	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		return null;
	}

	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	public boolean isReferenceTo(PsiElement element)
	{
		return false;
	}

	public Object[] getVariants()
	{
		final ClassReference classReference = getClassReference();
		if(classReference != null)
		{
			final PsiElement psiElement = classReference.resolve();
			if(psiElement instanceof PhpClass)
			{
				final LightPhpClass lightPhpClass = LightElementUtil.findLightClassByPsi((PhpClass) psiElement);
				if(lightPhpClass != null)
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
					final PhpModifier modifier = new PhpModifier();
					if(!classReference.getText().equals("parent"))
					{
						modifier.setState(PhpModifier.State.STATIC);
					}
					context.setModifier(modifier);
					context.setCallingObjectClass(lightPhpClass);

					final List<LightPhpElement> toComplete = new ArrayList<LightPhpElement>();
					for(LightPhpMethod method : lightPhpClass.getMethods())
					{
						toComplete.add(method);
					}
		  /*for (LightPhpField field : lightPhpClass.getFields()) {
            toComplete.add(field);
          }*/

					final List<LookupElement> list = PhpVariantsUtil.getLookupItems(toComplete, context);
					return list.toArray(new LookupElement[list.size()]);
				}
			}
		}
		return new Object[0];


	}

	public boolean isSoft()
	{
		return false;
	}
}
