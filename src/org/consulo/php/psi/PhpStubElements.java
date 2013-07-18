package org.consulo.php.psi;

import org.consulo.php.psi.impl.stub.elements.PhpClassStubElement;
import org.consulo.php.psi.impl.stub.elements.PhpFileStubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public interface PhpStubElements {
	PhpFileStubElement FILE = new PhpFileStubElement();
	PhpClassStubElement CLASS = new PhpClassStubElement();
}
