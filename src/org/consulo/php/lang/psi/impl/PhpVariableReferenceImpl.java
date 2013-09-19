package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import lombok.val;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.resolve.PhpResolveProcessor;
import org.consulo.php.lang.psi.resolve.PhpVariantsProcessor;
import org.consulo.php.lang.psi.resolve.ResolveUtil;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jay
 * @date Apr 3, 2008 9:59:26 PM
 */
public class PhpVariableReferenceImpl extends PhpNamedElementImpl implements PhpVariableReference
{

	public PhpVariableReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public String getName()
	{
		if(canReadName())
			return getNode().getText().substring(1);
		return null;
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(canReadName() && !getName().equals(name))
		{
			final PhpVariableReference variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitVariableReference(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement psiElement, @NotNull PsiElement psiElement1)
	{
		if(!isDeclaration())
			return true;

		return super.processDeclarations(processor, resolveState, psiElement, psiElement1);
	}

	@Override
	public boolean isDeclaration()
	{
		if((getParent() instanceof PhpAssignmentExpression) && !(getParent() instanceof PhpSelfAssignmentExpression))
		{
			return ((PhpAssignmentExpression) getParent()).getVariable() == this;
		}
		if(getParent() instanceof PhpForeachStatement)
		{
			PhpForeachStatement foreach = (PhpForeachStatement) getParent();
			return (foreach.getKey() == this) || (foreach.getValue() == this);
		}
		if(getParent() instanceof PhpCatchStatement)
		{
			return ((PhpCatchStatement) getParent()).getException() == this;
		}
		if(getParent() instanceof PhpGlobal)
		{
			return ArrayUtil.find(((PhpGlobal) getParent()).getVariables(), this) > -1;
		}
		return false;
	}

	/**
	 * This method determines if variable is actually variable variable ($$a for instance),
	 * or just plain $a variable
	 *
	 * @return true if variable's name is set and readable, false otherwise
	 */
	@Override
	public boolean canReadName()
	{
		return getNode().getChildren(null).length == 1 && getNode().getFirstChildNode().getElementType() == PhpTokenTypes.VARIABLE;
	}

	@Override
	public PsiReference getReference()
	{
		if(canReadName())
			return this;
		return null;
	}

	@Override
	public PsiElement getElement()
	{
		return this;
	}

	@Override
	public TextRange getRangeInElement()
	{
		return new TextRange(1, getTextLength());
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
	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		PhpResolveProcessor processor = new PhpResolveProcessor(this, getName(), PhpResolveProcessor.ResolveKind.FIELD_OR_PARAMETER);
		ResolveUtil.treeWalkUp(this, processor);
		Collection<PsiElement> declarations = processor.getResult();

		List<ResolveResult> result = new ArrayList<ResolveResult>(declarations.size());
		for(final PsiElement element : declarations)
		{
			if(declarations.size() > 1 && element == this)
			{
				continue;
			}
			result.add(new PsiElementResolveResult(element, true));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	@Override
	public String getCanonicalText()
	{
		return getName();
	}

	@Override
	public PsiElement handleElementRename(String s) throws IncorrectOperationException
	{
		return setName(s);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isReferenceTo(PsiElement psiElement)
	{
		if(psiElement instanceof PhpVariableReference || psiElement instanceof PhpParameter)
		{
			return psiElement == resolve();
		}
		return false;
	}

	@Override
	public Object[] getVariants()
	{
		PhpVariantsProcessor processor = new PhpVariantsProcessor(this);
		ResolveUtil.treeWalkUp(this, processor);
		val variants = processor.getVariants();
		//      final List<LookupItem> list = PhpVariantsUtil.getLookupItemsForVariables(variants);
		//      return list.toArray(new LookupItem[list.size()]);
		return variants.toArray(new Object[variants.size()]);
	}

	@Override
	public boolean isSoft()
	{
		return false;
	}
}
