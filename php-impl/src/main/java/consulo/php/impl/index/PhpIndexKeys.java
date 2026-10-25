package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpClassNameIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpConstantNameIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpNamespaceIndex;
import consulo.language.psi.stub.StubIndexKey;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpIndexKeys
{
	StubIndexKey<String, PhpClass> CLASSES = PhpClassNameIndex.KEY;
	StubIndexKey<String, Function> FUNCTION_BY_NAME = StubIndexKey.createIndexKey("php.functions.by.name");
	StubIndexKey<String, PhpClass> FULL_FQ_CLASSES = StubIndexKey.createIndexKey("php.full.fq.classes");
	StubIndexKey<String, PhpNamespace> NAMESPACES = PhpNamespaceIndex.KEY;

	StubIndexKey<String, PhpDefine> DEFINES = PhpConstantNameIndex.KEY;
	StubIndexKey<String, PhpClass> SUPERTYPES = StubIndexKey.createIndexKey("php.supertypes");
}
