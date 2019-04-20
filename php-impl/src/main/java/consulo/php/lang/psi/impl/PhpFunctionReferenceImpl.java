package consulo.php.lang.psi.impl;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.annotations.RequiredWriteAction;
import consulo.php.index.PhpFunctionByNameIndex;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.resolve.PhpResolveResult;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 15, 2008 12:36:39 PM
 */
public class PhpFunctionReferenceImpl extends PhpElementImpl implements FunctionReference, PhpReferenceWithCompletion
{
	public PhpFunctionReferenceImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitFunctionReference(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent != getParameterList())
		{
			if(getParameterList() != null && !getParameterList().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

	@Override
	public PsiReference getReference()
	{
		return this;
	}

	@RequiredReadAction
	@Override
	public PsiElement getElement()
	{
		return this;
	}

	@Override
	public ASTNode getNameNode()
	{
		return getNode().findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> resolveLocal()
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public PhpType resolveLocalType()
	{
		return PhpType.EMPTY;
	}

	@Nonnull
	@Override
	public Collection<? extends PhpNamedElement> resolveGlobal(boolean incompleteCode)
	{
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public String getSignature()
	{
		return "";
	}

	@Nonnull
	@Override
	public String getNamespaceName()
	{
		return "";
	}

	@Nonnull
	@Override
	public String getImmediateNamespaceName()
	{
		return "";
	}

	@Override
	public boolean isAbsolute()
	{
		return findChildByType(PhpTokenTypes.SLASH) != null;
	}

	@Nullable
	@Override
	public String getFQN()
	{
		return null;
	}

	@Override
	public String getName()
	{
		ASTNode node = getNameNode();
		if(node == null)
		{
			return null;
		}
		return node.getText();
	}

	@Override
	public ParameterList getParameterList()
	{
		return PsiTreeUtil.getChildOfType(this, ParameterList.class);
	}

	@Nonnull
	@Override
	public PsiElement[] getParameters()
	{
		ParameterList parameterList = getParameterList();
		return parameterList == null ? PsiElement.EMPTY_ARRAY : parameterList.getParameters();
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public TextRange getRangeInElement()
	{
		ASTNode nameNode = getNameNode();
		if(nameNode == null)
		{
			return TextRange.EMPTY_RANGE;
		}
		int startOffset = nameNode.getPsi().getStartOffsetInParent();
		return new TextRange(startOffset, startOffset + nameNode.getTextLength());
	}

	/**
	 * Returns the element which is the target of the reference.
	 *
	 * @return the target element, or null if it was not possible to resolve the reference to a valid target.
	 */
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
		PhpIndex index = PhpIndex.getInstance(getProject());
		Collection<Function> functions = index.getFunctionsByName(getName());
		return functions.stream().map(PhpResolveResult::new).toArray(ResolveResult[]::new);
	}

	/**
	 * Returns the name of the reference target element which does not depend on import statements
	 * and other context (for example, the full-qualified name of the class if the reference targets
	 * a Java class).
	 *
	 * @return the canonical text of the reference.
	 */
	@Nonnull
	@RequiredReadAction
	@Override
	public String getCanonicalText()
	{
		return getText();
	}

	/**
	 * Called when the reference target element has been renamed, in order to change the reference
	 * text according to the new name.
	 *
	 * @param newElementName the new name of the target element.
	 * @return the new underlying element of the reference.
	 * @throws com.intellij.util.IncorrectOperationException
	 *          if the rename cannot be handled for some reason.
	 */
	@RequiredWriteAction
	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		return null;
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
	@RequiredWriteAction
	@Override
	public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	/**
	 * Checks if the reference targets the specified element.
	 *
	 * @param element the element to check target for.
	 * @return true if the reference targets that element, false otherwise.
	 */
	@RequiredReadAction
	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		return getManager().areElementsEquivalent(resolve(), element);
	}

	@RequiredReadAction
	@Override
	public void processForCompletion(@Nonnull @RequiredReadAction Processor<PhpNamedElement> elementProcessor)
	{
		Project project = getProject();
		Collection<String> keys = PhpFunctionByNameIndex.INSTANCE.getAllKeys(project);
		GlobalSearchScope scope = GlobalSearchScope.allScope(project);
		for(String key : keys)
		{
			ProgressManager.checkCanceled();
			Collection<Function> functions = PhpFunctionByNameIndex.INSTANCE.get(key, project, scope);
			for(Function function : functions)
			{
				if(!elementProcessor.process(function))
				{
					return;
				}
			}
		}
	}

	/**
	 * Returns false if the underlying element is guaranteed to be a reference, or true
	 * if the underlying element is a possible reference which should not be reported as
	 * an error if it fails to resolve. For example, a text in an XML file which looks
	 * like a full-qualified Java class name is a soft reference.
	 *
	 * @return true if the refence is soft, false otherwise.
	 */
	@RequiredReadAction
	@Override
	public boolean isSoft()
	{
		return false;
	}
}
