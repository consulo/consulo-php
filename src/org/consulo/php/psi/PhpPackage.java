package org.consulo.php.psi;

import com.intellij.util.ArrayFactory;
import org.consulo.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public interface PhpPackage extends PsiPackage {
	PhpPackage[] EMPTY_ARRAY = new PhpPackage[0];

	ArrayFactory<PhpPackage> ARRAY_FACTORY = new ArrayFactory<PhpPackage>() {
		@NotNull
		@Override
		public PhpPackage[] create(int i) {
			return i == 0 ? EMPTY_ARRAY : new PhpPackage[i];
		}
	} ;
}
