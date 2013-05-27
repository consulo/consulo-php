package net.jay.plugins.php.pom;

import java.util.Collections;

import net.jay.plugins.php.lang.psi.PHPFile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
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
		if(!(file instanceof PHPFile))
		{
			return;
		}
		final PhpChangeSet phpChangeSet = new PhpChangeSet(model, (PHPFile) file);
		event.registerChangeSet(this, phpChangeSet);
	}

	public void projectOpened()
	{
	}

	public void projectClosed()
	{
	}

	@NonNls
	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.PhpPomAspect";
	}

	public void initComponent()
	{
	}

	public void disposeComponent()
	{
	}
}
