package consulo.php.impl.lang.psi;

import consulo.php.impl.lang.psi.impl.stub.elements.PhpClassMethodStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpClassStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpDefineStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpFieldStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpFileStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpFunctionStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpNamespaceStubElementType;
import consulo.php.impl.lang.psi.impl.stub.elements.PhpParameterStubElementType;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements
{
	PhpFileStubElementType FILE = new PhpFileStubElementType();

	PhpClassStubElementType CLASS = new PhpClassStubElementType();
	PhpFieldStubElementType CLASS_FIELD = new PhpFieldStubElementType();
	PhpClassMethodStubElementType CLASS_METHOD = new PhpClassMethodStubElementType();

	PhpFunctionStubElementType FUNCTION = new PhpFunctionStubElementType();
	PhpNamespaceStubElementType NAMESPACE = new PhpNamespaceStubElementType();
	PhpParameterStubElementType PARAMETER = new PhpParameterStubElementType();

	PhpDefineStubElementType DEFINE = new PhpDefineStubElementType();
}
