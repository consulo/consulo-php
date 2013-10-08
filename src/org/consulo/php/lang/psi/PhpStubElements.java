package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.impl.stub.elements.PhpClassStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFileStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFunctionStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpGroupStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements
{
	PhpFileStubElement FILE = new PhpFileStubElement();
	PhpGroupStubElement GROUP = new PhpGroupStubElement();
	PhpClassStubElement CLASS = new PhpClassStubElement();
	PhpFunctionStubElement FUNCTION = new PhpFunctionStubElement();
}
