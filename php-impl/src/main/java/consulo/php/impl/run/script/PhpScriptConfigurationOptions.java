package consulo.php.impl.run.script;

import com.dslplatform.json.CompiledJson;
import consulo.execution.configuration.RunConfigurationOptions;
import consulo.util.lang.StringUtil;
import consulo.util.xml.serializer.annotation.Attribute;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2026-10-23
 */
@CompiledJson
public class PhpScriptConfigurationOptions extends RunConfigurationOptions {
    private String myScriptPath;

    @Attribute("scriptPath")
    @Nullable
    public String getScriptPath() {
        return myScriptPath;
    }

    public void setScriptPath(@Nullable String scriptPath) {
        myScriptPath = StringUtil.nullize(scriptPath);
    }
}
