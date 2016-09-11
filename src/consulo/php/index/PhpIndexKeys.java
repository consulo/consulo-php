package consulo.php.index;

import consulo.php.lang.psi.PhpClass;
import com.intellij.psi.stubs.StubIndexKey;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpIndexKeys
{
	StubIndexKey<String, PhpClass> CLASSES = StubIndexKey.createIndexKey("php.classes");
	StubIndexKey<String, PhpClass> FULL_FQ_CLASSES = StubIndexKey.createIndexKey("php.full.fq.classes");
}
