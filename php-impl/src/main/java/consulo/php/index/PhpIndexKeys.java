package consulo.php.index;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.intellij.psi.stubs.StubIndexKey;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpIndexKeys
{
	StubIndexKey<String, PhpClass> CLASSES = StubIndexKey.createIndexKey("php.classes");
	StubIndexKey<String, PhpClass> FULL_FQ_CLASSES = StubIndexKey.createIndexKey("php.full.fq.classes");
	StubIndexKey<String, PhpNamespace> NAMESPACES = StubIndexKey.createIndexKey("php.namespaces");
}
