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

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testSuite.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		testSuite.tearDown();
		super.tearDown();
	}

	@Override
	protected void runTest() throws Throwable {
		testSuite.runTest(myTestFile);
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
