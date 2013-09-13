package org.consulo.php.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ReflectionUtil;
import org.consulo.php.lang.PHPLanguage;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * @author VISTALL
 * @since 18.07.13.
 */
public class PhpInstancableTokenType extends IElementType {
	private final Constructor<? extends PHPPsiElement> myConstructor;

	public PhpInstancableTokenType(@NotNull @NonNls String debugName, @NotNull Class<? extends PHPPsiElement> clazz) {
		super(debugName, PHPLanguage.INSTANCE);
		try {
			myConstructor = clazz.getConstructor(ASTNode.class);
		} catch (NoSuchMethodException e) {
			throw new Error(e);
		}
	}

	public PHPPsiElement createPsi(ASTNode astNode) {
		return ReflectionUtil.createInstance(myConstructor, astNode);
	}
}
