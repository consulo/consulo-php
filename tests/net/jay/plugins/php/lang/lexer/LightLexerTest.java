package net.jay.plugins.php.lang.lexer;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

public class LightLexerTest extends TestCase {

	private File myTestFile;
	private LexerTest testSuite;

	public LightLexerTest(File testFile, TestSuite testSuite) {
		myTestFile = testFile;
		if (testSuite instanceof LexerTest)
			this.testSuite = (LexerTest) testSuite;
	}

	protected void setUp() throws Exception {
		super.setUp();
		testSuite.setUp();
	}

	protected void tearDown() throws Exception {
		testSuite.tearDown();
		super.tearDown();
	}

	protected void runTest() throws Throwable {
		testSuite.runTest(myTestFile);
	}

	public int countTestCases() {
		return 1;
	}

	public String toString() {
		return myTestFile.getAbsolutePath();
	}

	protected void resetAllFields() {
		// Do nothing otherwise myTestFile will be nulled out before getName() is called.
	}

	public String getName() {
		return myTestFile.getAbsolutePath();
	}
}
