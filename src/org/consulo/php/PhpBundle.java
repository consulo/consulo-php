package org.consulo.php;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpBundle
{

	private static Reference<ResourceBundle> ourBundle;

	@NonNls
	private static final String BUNDLE = "org.consulo.php.PhpBundle";

	public static String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params)
	{
		return CommonBundle.message(getBundle(), key, params);
	}

	private static ResourceBundle getBundle()
	{
		ResourceBundle bundle = null;
		if(ourBundle != null)
			bundle = ourBundle.get();
		if(bundle == null)
		{
			bundle = ResourceBundle.getBundle(BUNDLE);
			ourBundle = new SoftReference<ResourceBundle>(bundle);
		}
		return bundle;
	}
}
