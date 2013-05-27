package net.jay.plugins.php.cache;

import net.jay.plugins.php.pom.PhpChangeSet;
import net.jay.plugins.php.pom.PhpPomAspect;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.PomModel;
import com.intellij.pom.PomModelAspect;
import com.intellij.pom.event.PomChangeSet;
import com.intellij.pom.event.PomModelEvent;
import com.intellij.pom.event.PomModelListener;

/**
 * @author jay
 * @date May 30, 2008 5:14:22 PM
 */
public abstract class PhpPomModelListener implements PomModelListener
{
	private Module module;
	private PomModel pomModel;
	private ProjectFileIndex fileIndex;

	public PhpPomModelListener(final Module module, final PomModel pomModel)
	{
		this.module = module;
		this.pomModel = pomModel;
		fileIndex = ProjectRootManager.getInstance(this.module.getProject()).getFileIndex();
	}

	public boolean isAspectChangeInteresting(PomModelAspect aspect)
	{
		return aspect instanceof PhpPomAspect;
	}

	public void modelChanged(final PomModelEvent event)
	{
		final PomChangeSet changeSet = event.getChangeSet(pomModel.getModelAspect(PhpPomAspect.class));
		if(changeSet != null)
		{
			final VirtualFile file = ((PhpChangeSet) changeSet).getChangedFile().getVirtualFile();
			if(file == null)
			{
				return;
			}
			if(fileIndex.getModuleForFile(file) == module)
			{
				processEvent(file);
			}
		}
	}

	protected abstract void processEvent(final VirtualFile file);
}
