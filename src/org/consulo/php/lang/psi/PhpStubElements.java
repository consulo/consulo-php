package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.impl.stub.elements.PhpClassStubElement;
import org.consulo.php.lang.psi.impl.stub.elements.PhpFileStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements {
	PhpFileStubElement FILE = new PhpFileStubElement();
	PhpClassStubElement CLASS = new PhpClassStubElement();
}
