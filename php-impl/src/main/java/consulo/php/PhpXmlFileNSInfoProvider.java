package consulo.php;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jetbrains.php.lang.PhpFileType;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import com.intellij.xml.util.XmlUtil;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
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
