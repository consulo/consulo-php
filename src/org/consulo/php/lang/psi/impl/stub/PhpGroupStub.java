package org.consulo.php.lang.psi.impl.stub;

import org.consulo.php.lang.psi.PhpGroup;
import org.consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpGroupStub extends StubBase<PhpGroup>
{
	public PhpGroupStub(StubElement parent)
	{
		super(parent, PhpStubElements.GROUP);
	}
}
