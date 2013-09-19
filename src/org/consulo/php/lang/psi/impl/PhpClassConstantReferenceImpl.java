package org.consulo.php.lang.psi.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.completion.PhpVariantsUtil;
import org.consulo.php.completion.UsageContext;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 18, 2008 1:51:48 PM
 */
public class PhpClassConstantReferenceImpl extends PhpElementImpl implements PhpClassConstantReference
{

	public PhpClassConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitClassConstantReference(this);
	}

	private ASTNode getNameNode()
	{
		return getNode().findChildByType(PhpTokenTypes.IDENTIFIER);
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

	@Override
	public PsiReference getReference()
	{
		if(canReadName())
			return this;
		return null;
	}

	@Nullable
	public PhpClassReference getClassReference()
	{
		PhpElement reference = getFirstPsiChild();
		if(reference instanceof PhpClassReference)
		{
			return (PhpClassReference) reference;
		}
		return null;
	}

	@Override
	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return new ResolveResult[0];
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
			ASTNode nameNode = getNameNode();
			int startOffset = nameNode.getPsi().getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameNode.getTextLength());
		}
		return null;
	}

	@Override
	@Nullable
	public PsiElement resolve()
	{
		return null;
	}

	@Override
	public String getCanonicalText()
	{
		return null;
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		return false;
	}

	@Override
	public Object[] getVariants()
	{
		final PhpClassReference classReference = getClassReference();
		if(classReference != null)
		{
			final PsiElement psiElement = classReference.resolve();
			if(psiElement instanceof PhpClass)
			{
					final UsageContext context = new UsageContext();
					final PhpClass contextClass = PsiTreeUtil.getParentOfType(this, PhpClass.class);

					context.setClassForAccessFilter(contextClass);


					context.setCallingObjectClass(contextClass);

					final List<PhpNamedElement> toComplete = new ArrayList<PhpNamedElement>();
					for(PhpFunction method : contextClass.getFunctions())
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
		return new Object[0];


	}

	@Override
	public boolean isSoft()
	{
		return false;
	}
}
