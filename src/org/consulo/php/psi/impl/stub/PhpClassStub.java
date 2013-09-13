package org.consulo.php.psi.impl.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.consulo.php.psi.PhpStubElements;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStub extends StubBase<PhpClass> {
	private final String myName;

	public PhpClassStub(StubElement parent, String name) {
		super(parent, PhpStubElements.CLASS);
		myName = name;
	}

	public String getName() {
		return myName;
	}
}
