package org.consulo.php.index;

import com.intellij.psi.stubs.StubIndexKey;
import org.consulo.php.lang.psi.PhpClass;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpIndexKeys {
	StubIndexKey<String, PhpClass> CLASSES = StubIndexKey.createIndexKey("php.classes");
	StubIndexKey<String, PhpClass> FULL_FQ_CLASSES = StubIndexKey.createIndexKey("php.full.fq.classes");
}
