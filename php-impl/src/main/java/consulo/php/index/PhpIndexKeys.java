package consulo.php.index;

import com.intellij.psi.stubs.StubIndexKey;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpIndexKeys
{
	StubIndexKey<String, PhpClass> CLASSES = StubIndexKey.createIndexKey("php.classes");
	StubIndexKey<String, Function> FUNCTION_BY_NAME = StubIndexKey.createIndexKey("php.functions.by.name");
	StubIndexKey<String, PhpClass> FULL_FQ_CLASSES = StubIndexKey.createIndexKey("php.full.fq.classes");
	StubIndexKey<String, PhpNamespace> NAMESPACES = StubIndexKey.createIndexKey("php.namespaces");

	StubIndexKey<String, PhpDefine> DEFINES = StubIndexKey.createIndexKey("php.defines");
}
