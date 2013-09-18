package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpFunctionCall;
import org.consulo.php.lang.psi.PhpParameterList;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date May 15, 2008 12:36:39 PM
 */
public class PhpFunctionCallImpl extends PhpElementImpl implements PhpFunctionCall {

	public PhpFunctionCallImpl(ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof PhpElementVisitor) {
			((PhpElementVisitor) visitor).visitPhpFunctionCall(this);
		} else {
			visitor.visitElement(this);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source) {
		if (lastParent != getParameterList()) {
			if (getParameterList() != null && !getParameterList().processDeclarations(processor, state, null, source)) {
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

	@Override
	public PsiReference getReference() {
		if (canReadName())
			return this;
		return null;
	}

	@Override
	public PsiElement getElement() {
		return this;
	}

	private ASTNode getNameNode() {
		return getNode().findChildByType(PhpTokenTypes.IDENTIFIER);
	}

	@Override
	public boolean canReadName() {
		return getNameNode() != null;
	}

	@Override
	public String getFunctionName() {
		if (canReadName()) {
			return getNameNode().getText();
		}
		return null;
	}

	@Override
	public PhpParameterList getParameterList() {
		return PsiTreeUtil.getChildOfType(this, PhpParameterList.class);
	}

	@Override
	public TextRange getRangeInElement() {
		if (canReadName()) {
			ASTNode nameNode = getNameNode();
			int startOffset = nameNode.getPsi().getStartOffsetInParent();
			return new TextRange(startOffset, startOffset + nameNode.getTextLength());
		}
		return null;
	}

	/**
	 * Returns the element which is the target of the reference.
	 *
	 * @return the target element, or null if it was not possible to resolve the reference to a valid target.
	 */
	@Override
	@Nullable
	public PsiElement resolve() {
		ResolveResult[] results = multiResolve(false);
		if (results.length == 1) {
			return results[0].getElement();
		}
		return null;
	}

	@Override
	@NotNull
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		/*DeclarationsIndex index = DeclarationsIndex.getInstance(this);
		List<LightPhpFunction> functions = index.getFunctionsByName(getFunctionName());
		ResolveResult[] result = new PhpResolveResult[functions.size()];
		for(int i = 0; i < functions.size(); i++)
		{
			LightPhpFunction function = functions.get(i);
			result[i] = new PhpResolveResult(function.getPsi(getProject()));
		}
		return result;  */
		return new ResolveResult[0];
	}

	/**
	 * Returns the name of the reference target element which does not depend on import statements
	 * and other context (for example, the full-qualified name of the class if the reference targets
	 * a Java class).
	 *
	 * @return the canonical text of the reference.
	 */
	@Override
	public String getCanonicalText() {
		return null;
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
	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
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
	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
		return null;
	}

	/**
	 * Checks if the reference targets the specified element.
	 *
	 * @param element the element to check target for.
	 * @return true if the reference targets that element, false otherwise.
	 */
	@Override
	public boolean isReferenceTo(PsiElement element) {
		return false;
	}

	/**
	 * Returns the array of String, {@link com.intellij.psi.PsiElement} and/or {@link com.intellij.psi.infos.CandidateInfo}
	 * instances representing all identifiers that are visible at the location of the reference. The contents
	 * of the returned array is used to build the lookup list for basic code completion. (The list
	 * of visible identifiers must not be filtered by the completion prefix string - the
	 * filtering is performed later by IDEA core.)
	 *
	 * @return the array of available identifiers.
	 */
	@Override
	public Object[] getVariants() {
		/*DeclarationsIndex index = DeclarationsIndex.getInstance(this);
		List<LightPhpFunction> variants = new ArrayList<LightPhpFunction>();
		for(String functionName : index.getAllFunctionNames())
		{
			variants.addAll(index.getFunctionsByName(functionName));
		}
		List<LookupElement> lookupItems = PhpVariantsUtil.getLookupItems(variants, null);
		return lookupItems.toArray(new Object[lookupItems.size()]);   */
		return new Object[0];
	}

	/**
	 * Returns false if the underlying element is guaranteed to be a reference, or true
	 * if the underlying element is a possible reference which should not be reported as
	 * an error if it fails to resolve. For example, a text in an XML file which looks
	 * like a full-qualified Java class name is a soft reference.
	 *
	 * @return true if the refence is soft, false otherwise.
	 */
	@Override
	public boolean isSoft() {
		return false;
	}
}
