package consulo.php.lang.psi;

import consulo.php.lang.psi.impl.stub.elements.PhpClassMethodStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpClassStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpFieldStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpFileStubElement;
import consulo.php.lang.psi.impl.stub.elements.PhpFunctionStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements
{
	PhpFileStubElement FILE = new PhpFileStubElement();

	PhpClassStubElement CLASS = new PhpClassStubElement();
	PhpFieldStubElement CLASS_FIELD = new PhpFieldStubElement();
	PhpClassMethodStubElement CLASS_METHOD = new PhpClassMethodStubElement();

	PhpFunctionStubElement FUNCTION = new PhpFunctionStubElement();
}
