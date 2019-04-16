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
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Catch;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpForeachStatement;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpAssignmentExpression;
import consulo.php.lang.psi.PhpGlobal;
import consulo.php.lang.psi.PhpPsiElementFactory;
import consulo.php.lang.psi.PhpSelfAssignmentExpression;
import consulo.php.lang.psi.impl.light.PhpLightConstant;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.PhpResolveResult;
import consulo.php.lang.psi.resolve.PhpVariantsProcessor;
import consulo.php.lang.psi.resolve.ResolveUtil;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 3, 2008 9:59:26 PM
 */
public class PhpVariableReferenceImpl extends PhpNamedElementImpl implements Variable, PhpReferenceWithCompletion
{
	private static class OurResolver implements ResolveCache.PolyVariantResolver<PhpVariableReferenceImpl>
	{
		private static final OurResolver INSTANCE = new OurResolver();

		@Nonnull
		@Override
		public ResolveResult[] resolve(@Nonnull PhpVariableReferenceImpl reference, boolean b)
		{
			if(Variable.SUPERGLOBALS.contains(reference.getNameCS()))
			{
				return new ResolveResult[]{new PhpResolveResult(new PhpLightConstant(reference.getProject(), reference.getName(), PhpType.STRING))};
			}

			String name = reference.getName();
			if(Variable.$THIS.equals(name))
			{
				PhpClass phpClass = PsiTreeUtil.getParentOfType(reference, PhpClass.class);
				if(phpClass != null)
				{
					return new ResolveResult[]{new PsiElementResolveResult(phpClass, true)};
				}
				return ResolveResult.EMPTY_ARRAY;
			}

			PhpResolveProcessor processor = new PhpResolveProcessor(reference, name, PhpResolveProcessor.ElementKind.FIELD, PhpResolveProcessor.ElementKind.PARAMETER);
			ResolveUtil.treeWalkUp(reference, processor);
			Collection<PsiElement> declarations = processor.getResult();

			List<ResolveResult> result = new ArrayList<>(declarations.size());
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
	}

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
	public void processDocs(Processor<PhpDocComment> processor)
	{

	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
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

	@RequiredReadAction
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(PhpTokenTypes.VARIABLE);
	}

	@RequiredWriteAction
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
		if(getParent() instanceof Catch)
		{
			return ((Catch) getParent()).getException() == this;
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
		return new TextRange(0, getTextLength());
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

	@RequiredReadAction
	@Override
	@Nonnull
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(getProject()).resolveWithCaching(this, OurResolver.INSTANCE, true, incompleteCode);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getType();
		}
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	public PhpType getInferredType()
	{
		if(isDeclaration())
		{
			PsiElement parent = getParent();
			if(parent instanceof PhpAssignmentExpression)
			{
				PsiElement variable = ((PhpAssignmentExpression) parent).getVariable();
				if(variable == this)
				{
					PsiElement value = ((PhpAssignmentExpression) parent).getValue();
					if(value instanceof PhpTypedElement)
					{
						return ((PhpTypedElement) value).getType();
					}
				}
			}
		}
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getDeclaredType()
	{
		PsiElement element = resolve();
		if(element instanceof PhpTypedElement)
		{
			return ((PhpTypedElement) element).getDeclaredType();
		}
		return PhpType.EMPTY;
	}

	@Override
	public void processForCompletion(@RequiredReadAction Processor<PhpNamedElement> processor)
	{
		PhpVariantsProcessor variantsProcessor = new PhpVariantsProcessor(this);
		ResolveUtil.treeWalkUp(this, variantsProcessor);
		for(PhpNamedElement element : variantsProcessor.getVariants())
		{
			processor.process(element);
		}
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public String getCanonicalText()
	{
		return getName();
	}

	@RequiredWriteAction
	@Override
	public PsiElement handleElementRename(String s) throws IncorrectOperationException
	{
		return setName(s);
	}

	@RequiredWriteAction
	@Override
	public PsiElement bindToElement(@Nonnull PsiElement psiElement) throws IncorrectOperationException
	{
		return null;
	}

	@RequiredReadAction
	@Override
	public boolean isReferenceTo(PsiElement psiElement)
	{
		return getManager().areElementsEquivalent(resolve(), psiElement);
	}

	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		return false;
	}
}
