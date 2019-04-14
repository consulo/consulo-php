package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.completion.PhpVariantsUtil;
import consulo.php.completion.UsageContext;
import consulo.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 1:51:48 PM
 */
public class PhpClassConstantReferenceImpl extends PhpElementImpl implements ClassConstantReference
{
	public PhpClassConstantReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
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
		{
			return this;
		}
		return null;
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

	@Override
	@Nonnull
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
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
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
		final ClassReference classReference = getClassReference();
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
				for(Function method : contextClass.getOwnMethods())
				{
					toComplete.add(method);
				}
		  /*for (LightPhpField field : lightPhpClass.getFields()) {
			toComplete.add(field);
          }*/

				final LookupElement[] list = PhpVariantsUtil.getLookupItems(toComplete, context);
				return list;
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
