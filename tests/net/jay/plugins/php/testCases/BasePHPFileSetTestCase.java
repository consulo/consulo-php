package net.jay.plugins.php.testCases;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import junit.framework.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public abstract class BasePHPFileSetTestCase extends PHPFileSetTestCase {

	public BasePHPFileSetTestCase(String path) {
		super(path);
	}

	public BasePHPFileSetTestCase(String dataPath, Class aClass) {
		super(dataPath, aClass);
	}

	protected abstract String transform(String testName, String[] data) throws Exception;

	public void runTest(final File myTestFile) throws Throwable {
		String content = new String(FileUtil.loadFileText(myTestFile));
		Assert.assertNotNull(content);

		List<String> input = new ArrayList<String>();

		int separatorIndex;

		content = StringUtil.replace(content, "\r", "");

		while ((separatorIndex = content.indexOf("---")) >= 0) {
			input.add(content.substring(0, separatorIndex).trim());
			content = content.substring(separatorIndex);
			while (StringUtil.startsWithChar(content, '-') ||
				StringUtil.startsWithChar(content, '\n')) content =
				content.substring(1);
		}

		String result = content;

		Assert.assertTrue("No data found in source file", input.size() > 0);

		while (StringUtil.startsWithChar(result, '-') ||
			StringUtil.startsWithChar(result, '\n') ||
			StringUtil.startsWithChar(result, '\r')) {
			result = result.substring(1);
		}
		final String transformed;
		String testName = myTestFile.getName();
		final int dotIdx = testName.indexOf('.');
		if (dotIdx >= 0) {
			testName = testName.substring(0, dotIdx);
		}

		transformed = StringUtil.replace(transform(testName,
			input.toArray(new String[input.size()])), "\r", "");
		result = StringUtil.replace(result, "\r", "");

		Assert.assertEquals(result.trim(), transformed.trim());
	}


}
