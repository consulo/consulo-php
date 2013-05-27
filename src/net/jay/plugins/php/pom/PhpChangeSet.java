package net.jay.plugins.php.pom;

import net.jay.plugins.php.lang.psi.PHPFile;

import com.intellij.pom.PomModel;
import com.intellij.pom.PomModelAspect;
import com.intellij.pom.event.PomChangeSet;

/**
 * @author jay
 * @date Jun 1, 2008 3:33:12 PM
 */
public class PhpChangeSet implements PomChangeSet
{

	private PomModel model;
	private PHPFile changedFile;

	public PhpChangeSet(PomModel model, PHPFile file)
	{
		this.model = model;
		changedFile = file;
	}

	public PHPFile getChangedFile()
	{
		return changedFile;
	}

	public PomModelAspect getAspect()
	{
		return model.getModelAspect(PhpPomAspect.class);
	}

	public void merge(PomChangeSet blocked)
	{
	}
}
