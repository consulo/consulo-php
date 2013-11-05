package org.consulo.php.lang.psi.impl.stub;

import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.PhpStubElements;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpFieldStub extends NamedStubBase<PhpField>
{
	public PhpFieldStub(StubElement parent, @Nullable StringRef name)
	{
		super(parent, PhpStubElements.FIELD, name);
	}
}
