package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.impl.stub.elements.PhpClassStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFieldStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFileStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFunctionStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements
{
	PhpFileStubElement FILE = new PhpFileStubElement();
	PhpClassStubElement CLASS = new PhpClassStubElement();
	PhpFieldStubElement FIELD = new PhpFieldStubElement();
	PhpFunctionStubElement FUNCTION = new PhpFunctionStubElement();
}
