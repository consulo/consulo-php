package consulo.php.impl.lang.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.ObjectStubSerializerProvider;
import consulo.language.psi.stub.StubElementTypeHolder;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author VISTALL
 * @since 30-Aug-22
 */
@ExtensionImpl
public  class PhpStubElementsHolder extends StubElementTypeHolder<PhpStubElements>
{
	@Nullable
	@Override
	public String getExternalIdPrefix()
	{
		return null;
	}

	@Nonnull
	@Override
	public List<ObjectStubSerializerProvider> loadSerializers()
	{
		return allFromStaticFields(PhpStubElements.class, Field::get);
	}
}
