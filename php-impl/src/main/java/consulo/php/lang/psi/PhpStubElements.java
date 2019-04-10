package consulo.php.lang.psi;

import consulo.php.lang.psi.impl.stub.elements.PhpClassMethodStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpClassStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpFieldStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpFileStubElementType;
import consulo.php.lang.psi.impl.stub.elements.PhpFunctionStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpNamespaceStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements
{
	PhpFileStubElementType FILE = new PhpFileStubElementType();

	PhpClassStubElement CLASS = new PhpClassStubElement();
	PhpFieldStubElement CLASS_FIELD = new PhpFieldStubElement();
	PhpClassMethodStubElement CLASS_METHOD = new PhpClassMethodStubElement();

	PhpFunctionStubElement FUNCTION = new PhpFunctionStubElement();
	PhpNamespaceStubElement NAMESPACE = new PhpNamespaceStubElement();
}
