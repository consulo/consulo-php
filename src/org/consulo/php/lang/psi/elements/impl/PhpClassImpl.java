package org.consulo.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.val;
import org.consulo.php.PHPIcons;
import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.consulo.php.lang.psi.elements.*;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;
import org.consulo.php.util.PhpPresentationUtil;
import org.consulo.php.psi.PhpStubElements;
import org.consulo.php.psi.impl.PhpStubbedNamedElementImpl;
import org.consulo.php.psi.impl.stub.PhpClassStub;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 8, 2008 1:54:50 PM
 */
public class PhpClassImpl extends PhpStubbedNamedElementImpl<PhpClassStub> implements PhpClass {
	public PhpClassImpl(ASTNode node) {
		super(node);
	}

	public PhpClassImpl(@NotNull PhpClassStub stub) {
		super(stub, PhpStubElements.CLASS);
	}

	@Override
	public PhpField[] getFields() {
		return findChildrenByClass(PhpField.class);
	}

	@Override
	public PhpMethod[] getMethods() {
		return findChildrenByClass(PhpMethod.class);
	}

	@Override
	public PhpClass getSuperClass() {
		val list = PsiTreeUtil.getChildOfType(this, ExtendsList.class);
		assert list != null;
		return list.getExtendsClass();
	}

	@Override
	public PhpClass[] getImplementedInterfaces() {
		val list = PsiTreeUtil.getChildOfType(this, ImplementsList.class);
		assert list != null;
		val interfaceList = list.getInterfaces();
		return interfaceList.toArray(new PhpClass[interfaceList.size()]);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public PhpMethod getConstructor() {
		PhpMethod newOne = null;
		PhpMethod oldOne = null;
		for (PhpMethod phpMethod : this.getMethods()) {
			if (phpMethod.getName().equals(CONSTRUCTOR)) {
				newOne = phpMethod;
			}
			if (phpMethod.getName().equals(this.getName())) {
				oldOne = phpMethod;
			}
		}
		if (newOne != null) {
			return newOne;
		}
		return oldOne;
	}

	@Override
	public void accept(@NotNull final PsiElementVisitor psiElementVisitor) {
		if (psiElementVisitor instanceof PHPElementVisitor) {
			((PHPElementVisitor) psiElementVisitor).visitPhpClass(this);
		} else {
			super.accept(psiElementVisitor);
		}
	}

	@Override
	@NotNull
	public Icon getIcon() {
		if (isAbstract()) {
			return PHPIcons.ABSTRACT_CLASS;
		}
		if (isFinal()) {
			return PHPIcons.FINAL_CLASS;
		}
		return PHPIcons.CLASS;
	}

	@Override
	public boolean isAbstract() {
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwABSTACT;
	}

	@Override
	public boolean isFinal() {
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwFINAL;
	}

	@Override
	public boolean isInterface() {
		return getNode().getFirstChildNode().getElementType() == PHPTokenTypes.kwINTERFACE;
	}

	@Override
	public PhpModifier getModifier() {
		PhpModifier modifier = new PhpModifier();
		if (isAbstract()) {
			modifier.setAbstractness(PhpModifier.Abstractness.ABSTRACT);
		}
		if (isFinal()) {
			modifier.setAbstractness(PhpModifier.Abstractness.FINAL);
		}
		return modifier;
	}

	@Override
	public ItemPresentation getPresentation() {
		return PhpPresentationUtil.getClassPresentation(this);
	}

	@Override
	public PHPPsiElement getFirstPsiChild() {
		return null;
	}

	@Override
	public PHPPsiElement getNextPsiSibling() {
		return null;
	}

	@Override
	public PHPPsiElement getPrevPsiSibling() {
		return null;
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
		for (PhpMethod phpMethod : getMethods()) {
			if(!processor.execute(phpMethod, state)) {
				return false;
			}
		}

		for (PhpField phpField : getFields()) {
			if(!processor.execute(phpField, state)) {
				return false;
			}
		}

		return super.processDeclarations(processor, state, lastParent, place);
	}
}
