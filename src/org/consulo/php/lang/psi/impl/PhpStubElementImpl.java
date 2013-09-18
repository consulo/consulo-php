package org.consulo.php.lang.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpStubElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T> {
	public PhpStubElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
		super(stub, nodeType);
	}

	public PhpStubElementImpl(@NotNull ASTNode node) {
		super(node);
	}
}
