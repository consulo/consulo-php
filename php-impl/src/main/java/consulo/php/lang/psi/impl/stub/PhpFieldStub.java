package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpStubElements;
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
