package net.jay.plugins.php.testCases;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public abstract class PHPFileSetTestCase extends FileSetTestCase {

	protected Project myProject;
	private IdeaProjectTestFixture myFixture;


	public PHPFileSetTestCase(String path) {
		super(path);
	}

	public PHPFileSetTestCase(String dataPath, Class aClass) {
		super(dataPath, aClass);
	}

	@Override
	public void setUp() {
		super.setUp();

		TestFixtureBuilder<IdeaProjectTestFixture> fixtureBuilder = IdeaTestFixtureFactory.getFixtureFactory().createLightFixtureBuilder();
		myFixture = fixtureBuilder.getFixture();

		try {
			myFixture.setUp();
		} catch (Exception e) {
			//ignore
		}

		myProject = myFixture.getProject();
	}

	@Override
	public void tearDown() {
		try {
			myFixture.tearDown();
		} catch (Exception e) {
			//ignore
		}
		super.tearDown();
	}

}
