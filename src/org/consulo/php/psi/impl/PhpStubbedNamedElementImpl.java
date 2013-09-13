package org.consulo.php.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import lombok.val;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.PhpPsiElementFactory;
import org.consulo.php.lang.psi.elements.ConstantReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpStubbedNamedElementImpl<T extends StubElement> extends PhpStubElementImpl<T> implements PsiNameIdentifierOwner, PsiNamedElement{
	public PhpStubbedNamedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
		super(stub, nodeType);
	}

	public PhpStubbedNamedElementImpl(@NotNull ASTNode node) {
		super(node);
	}

	public PhpDocComment getDocComment()
	{
		val element = getPrevSibling();
		if(element instanceof PhpDocComment)
		{
			return (PhpDocComment) element;
		}
		return null;
	}

	@Override
	public String getName() {
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		PsiElement nameIdentifier = getNameIdentifier();
		if(nameIdentifier != null && !getName().equals(s))
		{
			final ConstantReference constantReference = PhpPsiElementFactory.createConstantReference(getProject(), s);
			nameIdentifier.replace(constantReference.getNameIdentifier());
		}
		return null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return findChildByType(PhpTokenTypes.IDENTIFIER);
	}
}

