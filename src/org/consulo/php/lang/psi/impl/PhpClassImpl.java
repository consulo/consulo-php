package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.val;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.*;
import org.consulo.php.lang.psi.impl.stub.PhpClassStub;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.consulo.php.util.PhpPresentationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	@NotNull
	@Override
	public PhpField[] getFields() {
		return findChildrenByClass(PhpField.class);
	}

	@NotNull
	@Override
	public PhpClass[] getClasses() {
		return findChildrenByClass(PhpClass.class);
	}

	@Override
	@NotNull
	public PhpFunction[] getFunctions() {
		return findChildrenByClass(PhpFunction.class);
	}

	@Override
	public String getNamespace() {
		PhpClassStub stub = getStub();
		if(stub != null) {
			return stub.getNamespace();
		}

		PsiElement parent = getParent();
		if(parent instanceof PhpGroup) {
			for (PsiElement psiElement : ((PhpGroup) parent).getStatements()) {
				if(psiElement instanceof PhpNamespaceStatement) {
					return ((PhpNamespaceStatement) psiElement).getNamespace();
				}
			}
		}
		return null;
	}

	@Override
	public PhpClass getSuperClass() {
		val list = PsiTreeUtil.getChildOfType(this, PhpExtendsList.class);
		assert list != null;
		return list.getExtendsClass();
	}

	@Override
	public PhpClass[] getImplementedInterfaces() {
		val list = PsiTreeUtil.getChildOfType(this, PhpImplementsList.class);
		assert list != null;
		val interfaceList = list.getInterfaces();
		return interfaceList.toArray(new PhpClass[interfaceList.size()]);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public PhpFunction getConstructor() {
		PhpFunction newOne = null;
		PhpFunction oldOne = null;
		for (PhpFunction phpMethod : this.getFunctions()) {
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
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitClass(this);
	}

	@Override
	public boolean isInterface() {
		return findChildByType(PhpTokenTypes.INTERFACE_KEYWORD) != null;
	}

	@Override
	public boolean isTrait() {
		return findChildByType(PhpTokenTypes.TRAIT_KEYWORD) != null;
	}

	@Override
	public ItemPresentation getPresentation() {
		return PhpPresentationUtil.getClassPresentation(this);
	}

	@Override
	public PhpElement getFirstPsiChild() {
		return null;
	}

	@Override
	public PhpElement getNextPsiSibling() {
		return null;
	}

	@Override
	public PhpElement getPrevPsiSibling() {
		return null;
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
		for (PhpFunction phpMethod : getFunctions()) {
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

	@Nullable
	@Override
	public PhpModifierList getModifierList() {
		return findChildByClass(PhpModifierList.class);
	}

	@Override
	public boolean hasModifier(@NotNull IElementType type) {
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(type);
	}

	@Override
	public boolean hasModifier(@NotNull TokenSet tokenSet) {
		PhpModifierList modifierList = getModifierList();
		return modifierList != null && modifierList.hasModifier(tokenSet);
	}
}
