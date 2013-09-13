package org.consulo.php;

import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import com.intellij.xml.util.XmlUtil;
import org.consulo.php.lang.PHPFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpXmlFileNSInfoProvider implements XmlFileNSInfoProvider {
	@Nullable
	@Override
	public String[][] getDefaultNamespaces(@NotNull XmlFile xmlFile) {
		if(xmlFile.getFileType() == PHPFileType.INSTANCE)
			return new String[][]{
					{
							"",
							XmlUtil.HTML_URI
					}
			};
		return null;
	}
}
