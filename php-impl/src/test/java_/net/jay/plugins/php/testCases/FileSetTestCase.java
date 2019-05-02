package net.jay.plugins.php.testCases;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.jay.plugins.php.utils.FileScanner;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.util.text.StringUtil;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public abstract class FileSetTestCase extends TestSuite {

	@NonNls
	private static final String TEST_FILE_PATTERN = "(.*)\\.txt";
	private File[] myFiles;
	private Class testImpl;

	public FileSetTestCase(String path) {
		this(path, ActualTest.class);
	}

	public FileSetTestCase(String path, Class testImpl) {
		this.testImpl = testImpl;
		List<File> myFileList;
		try {
			System.out.println("Test directory found: " + path);
			myFileList = FileScanner.scan(path, getSearchPattern());
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("No test directory can be found: " + path);
		}
		myFiles = myFileList.toArray(new File[myFileList.size()]);
		addAllTests();
	}



	protected void setUp() {
	}

	protected void tearDown() {
	}

	private void addAllTests() {
		for (File f : myFiles) {
			if (f.isFile()) {
				addFileTest(f);
			}
		}
	}

	protected FileSetTestCase(File[] files) {
		myFiles = files;
		addAllTests();
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	protected String getSearchPattern() {
		return TEST_FILE_PATTERN;
	}


	private void addFileTest(File file) {
		if (!StringUtil.startsWithChar(file.getName(), '_') &&
			!"CVS".equals(file.getName())) {
//			final ActualTest t = new ActualTest(file);
			TestCase t;
			try {
				try {
					Constructor constructor = this.testImpl.getConstructor(File.class, TestSuite.class);
					t = (TestCase) constructor.newInstance(file, this);
					addTest(t);
					return;
				} catch (NoSuchMethodException e) {
				}
				t = (TestCase) this.testImpl.getConstructor(File.class).newInstance(file);
				addTest(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract void runTest(final File file) throws Throwable;

}

