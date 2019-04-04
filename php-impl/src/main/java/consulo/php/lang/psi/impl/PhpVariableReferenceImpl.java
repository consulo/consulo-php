package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpForeachStatement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.completion.PhpVariantsUtil;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpAssignmentExpression;
import consulo.php.lang.psi.PhpCatchStatement;
import consulo.php.lang.psi.PhpGlobal;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpSelfAssignmentExpression;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.PhpVariantsProcessor;
import consulo.php.lang.psi.resolve.ResolveUtil;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 3, 2008 9:59:26 PM
 */
public class PhpVariableReferenceImpl extends PhpNamedElementImpl implements Variable
{
	public PhpVariableReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public ASTNode getNameNode()
	{
		return null;
	}

	@Override
	public String getName()
	{
		if(canReadName())
		{
			return getNode().getText().substring(1);
		}
		return null;
	}

	@Nonnull
	@Override
	public CharSequence getNameCS()
	{
		return null;
	}

	@Override
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getFQN()
	{
		return null;
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return null;
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

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@Override
	public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		//noinspection ConstantConditions
		if(canReadName() && !getName().equals(name))
		{
			final Variable variable = PhpPsiElementFactory.createVariable(getProject(), name);
			nameIdentifier.replace(variable.getNameIdentifier());
		}
		return this;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitVariableReference(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState resolveState, PsiElement psiElement, @Nonnull PsiElement psiElement1)
	{
		if(!isDeclaration())
		{
			return true;
		}

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
		{
			return this;
		}
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
	@Nonnull
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
	public PsiElement bindToElement(@Nonnull PsiElement psiElement) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isReferenceTo(PsiElement psiElement)
	{
		if(psiElement instanceof Variable || psiElement instanceof Parameter)
		{
			return psiElement == resolve();
		}
		return false;
	}

	@Nonnull
	@Override
	public Object[] getVariants()
	{
		PhpVariantsProcessor processor = new PhpVariantsProcessor(this);
		ResolveUtil.treeWalkUp(this, processor);
		List<PhpPsiElement> variants = processor.getVariants();
		return PhpVariantsUtil.getLookupItemsForVariables(variants);
	}

	@Override
	public boolean isSoft()
	{
		return false;
	}
}
