package net.jay.plugins.php.utils;

import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public class PathUtils {

	@NonNls
	private static final String searchString1 = "classes/production/plugin";
	@NonNls
	private static final String searchString2 = "dist/testClasses";

	@Nullable
	public static String getDataPath(@Nonnull Class s) {
		String n = s.getName();
		int index = n.lastIndexOf(".");
		String path = "testData/" + n.substring(0, index).replace(".", "/");
		return path;
	}

	@Nullable
	public static String getClassDir(@Nonnull Class s) {
		String path = getClassPath(s);
// if tests are run, using ideaProjectRunner
		String dataPath = getClassDir(path, searchString1);
		if (dataPath != null) {
			return dataPath;
		}
// if tests are run using ant script
		dataPath = getClassDir(path, searchString2);
		if (dataPath != null) {
			return dataPath;
		}
		return path;
	}

	public static String getClassPath(@Nonnull final Class s) {
		String name = s.getName();
		name = name.substring(name.lastIndexOf('.') + 1) + ".class";
		final URL url = s.getResource(name);
		String path = url.getPath();
		return path;
//		System.out.println(path);
//		return path.substring(0, name.lastIndexOf("."));
	}

	@Nullable
	public static String getModuleDir(@Nonnull Class s) {
		String path = getClassPath(s);
// if tests are run, using ideaProjectRunner
		String moduleDir = getModuleDir(path, searchString1);
		if (moduleDir != null) {
			return moduleDir;
		}
// if tests are run using ant script
		moduleDir = getModuleDir(path, searchString2);
		if (moduleDir != null) {
			return moduleDir;
		}
		return path;
	}

	@Nullable
	private static String getModuleDir(@Nonnull String s, @Nonnull final String indicator) {
		int n = s.indexOf(indicator);
		if (n == -1) {
			return null;
		}
		return s.substring(0, n - 1);
	}

	@Nullable
	private static String getClassDir(@Nonnull String s, @Nonnull final String indicator) {
		int n = s.indexOf(indicator);
		if (n == -1) {
			return null;
		}
		s = "tests" + s.substring(n + indicator.length());
		s = s.substring(0, s.lastIndexOf('/'));
		return s;
	}

}
