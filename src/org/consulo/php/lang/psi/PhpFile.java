package org.consulo.php.lang.psi;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpFile extends PsiFile, PhpElement, PhpMemberHolder {
	@NotNull
	PhpGroup[] getGroups();
}
