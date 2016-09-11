package consulo.php.pom;

import consulo.php.lang.psi.impl.PhpFileImpl;
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
	private PhpFileImpl changedFile;

	public PhpChangeSet(PomModel model, PhpFileImpl file)
	{
		this.model = model;
		changedFile = file;
	}

	public PhpFileImpl getChangedFile()
	{
		return changedFile;
	}

	@Override
	public PomModelAspect getAspect()
	{
		return model.getModelAspect(PhpPomAspect.class);
	}

	@Override
	public void merge(PomChangeSet blocked)
	{
	}
}
