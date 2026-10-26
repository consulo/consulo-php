package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.index.PhpIndexKeys;
import com.jetbrains.php.lang.psi.elements.impl.ConstantImpl;
import com.jetbrains.php.lang.psi.elements.Constant;
import consulo.php.impl.lang.psi.impl.stub.PhpConstantStubImpl;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpConstantStubElementType extends PhpStubElementType<PhpConstantStub, Constant>
{
	public PhpConstantStubElementType()
	{
		super("PHP_CONSTANT");
	}

	@Nonnull
	@Override
	public Constant createElement(@Nonnull ASTNode node)
	{
		return new ConstantImpl(node);
	}

	@Override
	public Constant createPsi(@Nonnull PhpConstantStub phpConstantStub)
	{
		return new ConstantImpl(phpConstantStub, this);
	}

	@RequiredReadAction
	@Override
	public PhpConstantStub createStub(@Nonnull Constant phpDefine, StubElement parent)
	{
		return new PhpConstantStubImpl(parent, this, phpDefine.getName(), phpDefine.getValuePresentation());
	}

	@Override
	public void serialize(@Nonnull PhpConstantStub phpConstantStub, @Nonnull StubOutputStream outputStream) throws IOException
	{
		outputStream.writeName(phpConstantStub.getName());
		outputStream.writeName(phpConstantStub.getValuePresentation());
	}

	@Nonnull
	@Override
	public PhpConstantStub deserialize(@Nonnull StubInputStream stubInputStream, StubElement parent) throws IOException
	{
		StringRef nameRef = stubInputStream.readName();
		StringRef valuePresentation = stubInputStream.readName();
		return new PhpConstantStubImpl(parent, this, StringRef.toString(nameRef), StringRef.toString(valuePresentation));
	}

	@Override
	public void indexStub(@Nonnull PhpConstantStub phpConstantStub, @Nonnull IndexSink indexSink)
	{
		String name = phpConstantStub.getName();
		if(!StringUtil.isEmpty(name))
		{
			indexSink.occurrence(PhpIndexKeys.DEFINES, name);
		}
	}
}
