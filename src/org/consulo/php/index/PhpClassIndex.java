package org.consulo.php.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassIndex extends StringStubIndexExtension<PhpClass> {
	public static PhpClassIndex INSTANCE = new PhpClassIndex();

	@NotNull
	@Override
	public StubIndexKey<String, PhpClass> getKey() {
		return PhpIndexKeys.CLASSES;
	}
}
