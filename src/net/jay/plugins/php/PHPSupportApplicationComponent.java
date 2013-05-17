package net.jay.plugins.php;

import com.intellij.codeInsight.completion.CompletionUtil;
import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import com.intellij.xml.util.XmlUtil;
import net.jay.plugins.php.completion.PhpCompletionData;
import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.inspections.PhpDynamicAsStaticMethodCall;
import net.jay.plugins.php.lang.inspections.PhpUndefinedMethodCall;
import net.jay.plugins.php.lang.inspections.PhpUndefinedVariable;
import net.jay.plugins.php.lang.inspections.classes.PhpUnimplementedMethodsInClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPSupportApplicationComponent implements ApplicationComponent, XmlFileNSInfoProvider, InspectionToolProvider {

	public PHPSupportApplicationComponent() {
	}

    public void initComponent() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                FileTypeManager.getInstance().registerFileType(PHPFileType.PHP, PHPFileType.EXTENTIONS);
                CompletionUtil.registerCompletionData(PHPFileType.PHP, new PhpCompletionData());
            }
        });
    }

	public void disposeComponent() {
	}

	@NotNull
	public String getComponentName() {
		return "PHPSupportApplicationComponent";
	}

  @Nullable
  public String[][] getDefaultNamespaces(@NotNull XmlFile xmlFile) {
    if (xmlFile.getFileType() == PHPFileType.PHP) return new String[][] {{"", XmlUtil.HTML_URI}};
    return null;
  }

  public Class[] getInspectionClasses() {
    return new Class[]{
      PhpUndefinedVariable.class,
      PhpUndefinedMethodCall.class,
      PhpDynamicAsStaticMethodCall.class,
      PhpUnimplementedMethodsInClass.class,
    };
  }
}
