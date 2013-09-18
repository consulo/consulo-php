package net.jay.plugins.php.testCases;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 10.08.2007
 * Time: 11:07:55
 */
public class ActualTest extends TestCase {

	private File myTestFile;
	private FileSetTestCase testCase;


	public ActualTest(File testFile, TestSuite testCase) {
		myTestFile = testFile;
		if (testCase instanceof FileSetTestCase)
			this.testCase = (FileSetTestCase) testCase;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testCase.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		testCase.tearDown();
		super.tearDown();
	}

	@Override
	protected void runTest() throws Throwable {
		testCase.runTest(myTestFile);
	}

	@Override
	public int countTestCases() {
		return 1;
	}

	public String toString() {
		return myTestFile.getAbsolutePath();
	}

	protected void resetAllFields() {
		// Do nothing otherwise myTestFile will be nulled out before getName() is called.
	}

	@Override
	public String getName() {
		return myTestFile.getAbsolutePath();
	}
}
