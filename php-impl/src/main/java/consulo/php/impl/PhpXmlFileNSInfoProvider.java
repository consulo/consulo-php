package consulo.php.impl;

import com.intellij.xml.util.XmlUtil;
import com.jetbrains.php.lang.PhpFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.xml.psi.xml.XmlFile;
import consulo.xml.psi.xml.XmlFileNSInfoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
@ExtensionImpl
public class PhpXmlFileNSInfoProvider implements XmlFileNSInfoProvider
{
	@Nullable
	@Override
	public String[][] getDefaultNamespaces(@Nonnull XmlFile xmlFile)
	{
		if(xmlFile.getFileType() == PhpFileType.INSTANCE)
		{
			return new String[][]{
					{
							"",
							XmlUtil.HTML_URI
					}
			};
		}
		return null;
	}

	@Override
	public boolean overrideNamespaceFromDocType(@Nonnull XmlFile xmlFile)
	{
		return true;
	}
}
