package org.consulo.php.pom;

import org.consulo.php.lang.psi.PhpFile;

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
	private PhpFile changedFile;

	public PhpChangeSet(PomModel model, PhpFile file)
	{
		this.model = model;
		changedFile = file;
	}

	public PhpFile getChangedFile()
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
