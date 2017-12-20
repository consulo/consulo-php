package consulo.php;

import consulo.php.lang.PhpFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
	public String[][] getDefaultNamespaces(@NotNull XmlFile xmlFile)
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
	public boolean overrideNamespaceFromDocType(@NotNull XmlFile xmlFile)
	{
		return true;
	}
}
