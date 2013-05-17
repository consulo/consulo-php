package net.jay.plugins.php;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import com.intellij.CommonBundle;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPBundle {

	private static Reference<ResourceBundle> ourBundle;

	@NonNls
	private static final String BUNDLE = "net.jay.plugins.php.PHPBundle";

	public static String message(@PropertyKey(resourceBundle = BUNDLE)String key, Object... params) {
		return CommonBundle.message(getBundle(), key, params);
	}

	private static ResourceBundle getBundle() {
		ResourceBundle bundle = null;
		if (ourBundle != null) bundle = ourBundle.get();
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(BUNDLE);
			ourBundle = new SoftReference<ResourceBundle>(bundle);
		}
		return bundle;
	}
}
