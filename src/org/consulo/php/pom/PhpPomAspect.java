package org.consulo.php.pom;

import java.util.Collections;

import org.consulo.php.lang.psi.impl.PhpFileImpl;
import com.intellij.lang.ASTNode;
import com.intellij.pom.PomModel;
import com.intellij.pom.PomModelAspect;
import com.intellij.pom.event.PomModelEvent;
import com.intellij.pom.tree.TreeAspect;
import com.intellij.pom.tree.events.TreeChangeEvent;
import com.intellij.psi.PsiFile;

/**
 * @author jay
 * @date Jun 1, 2008 3:15:16 PM
 */
public class PhpPomAspect implements PomModelAspect
{

	private final PomModel model;
	private final TreeAspect aspect;

	public PhpPomAspect(final PomModel model, final TreeAspect treeAspect)
	{
		this.model = model;
		this.aspect = treeAspect;
		this.model.registerAspect(getClass(), this, Collections.singleton((PomModelAspect) aspect));
	}

	@Override
	public void update(PomModelEvent event)
	{
		if(!event.getChangedAspects().contains(aspect))
		{
			return;
		}

		final TreeChangeEvent changeSet = (TreeChangeEvent) event.getChangeSet(aspect);
		if(changeSet == null)
		{
			return;
		}

		final ASTNode rootElement = changeSet.getRootElement();
		final PsiFile file = (PsiFile) rootElement.getPsi();
		if(!(file instanceof PhpFileImpl))
		{
			return;
		}
		final PhpChangeSet phpChangeSet = new PhpChangeSet(model, (PhpFileImpl) file);
		event.registerChangeSet(this, phpChangeSet);
	}
}
