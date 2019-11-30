package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.index.PhpIndexKeys;
import consulo.php.lang.psi.impl.PhpDefineImpl;
import consulo.php.lang.psi.impl.stub.PhpConstantStubImpl;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpDefineStubElementType extends PhpStubElementType<PhpConstantStub, PhpDefine>
{
	public PhpDefineStubElementType()
	{
		super("PHP_DEFINE");
	}

	@Nonnull
	@Override
	public PhpDefine createElement(@Nonnull ASTNode node)
	{
		return new PhpDefineImpl(node);
	}

	@Override
	public PhpDefine createPsi(@Nonnull PhpConstantStub phpConstantStub)
	{
		return new PhpDefineImpl(phpConstantStub, this);
	}

	@RequiredReadAction
	@Override
	public PhpConstantStub createStub(@Nonnull PhpDefine phpDefine, StubElement parent)
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
