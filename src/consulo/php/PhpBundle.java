package consulo.php;

import org.jetbrains.annotations.PropertyKey;
import com.intellij.AbstractBundle;

public class PhpBundle extends AbstractBundle
{
	private static final PhpBundle ourInstance = new PhpBundle();

	private PhpBundle()
	{
		super("messages.PhpBundle");
	}

	public static String message(@PropertyKey(resourceBundle = "messages.PhpBundle") String key)
	{
		return ourInstance.getMessage(key);
	}

	public static String message(@PropertyKey(resourceBundle = "messages.PhpBundle") String key, Object... params)
	{
		return ourInstance.getMessage(key, params);
	}
}
